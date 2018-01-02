package es.canadillas.daniel.raspberrypicontroller.view.item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.R;
import es.canadillas.daniel.raspberrypicontroller.model.HostConnection;
import es.canadillas.daniel.raspberrypicontroller.model.Service;


public class ServiceItemAdapter extends BaseAdapter {

    private Context context;
    private List<Service> services;
    private HostConnection hostConnection;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;


    public ServiceItemAdapter(Context context, List<Service> services, HostConnection hostConnection) {
        this.context = context;
        this.services = services;
        this.hostConnection = hostConnection;
    }

    @Override
    public int getCount() {
        return this.services.size();
    }

    @Override
    public Object getItem(int i) {
        return this.services.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        View rowView = view;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.service_card, viewGroup, false);
        }
        Switch swActivated = rowView.findViewById(R.id.swtichActivated);
        swActivated.setOnCheckedChangeListener(null);
        swActivated.setChecked(services.get(i).isActivated());
        swActivated.setText(services.get(i).isActivated() ? rowView.getResources().getString(R.string.service_activated) : rowView.getResources().getString(R.string.service_disabled));
        TextView txt = rowView.findViewById(R.id.txtServiceName);
        txt.setText(services.get(i).getName());
        onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean isChecked) {
                final Switch sw = (Switch) compoundButton;
                new AsyncTask<Void, Void, Void>() {

                    String result;
                    boolean activated;

                    @Override
                    protected Void doInBackground(Void... voids) {
                        publishProgress();
                        if (isChecked) {
                            result = hostConnection.startService(services.get(i));
                        } else {
                            result = hostConnection.stopService(services.get(i));
                        }
                        activated = hostConnection.isServiceActivated(services.get(i));
                        publishProgress();
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Void... values) {
                        super.onProgressUpdate(values);
                        sw.setOnCheckedChangeListener(null);
                        if (result != null) {
                            if (!hostConnection.testConnection()) {
                                activated = !isChecked;
                                Toast.makeText(context, context.getString(R.string.error_service_upgrade), Toast.LENGTH_LONG).show();
                            }
                            services.get(i).setActivated(activated);
                            //sw.setChecked(activated);
                            ServiceItemAdapter.this.notifyDataSetChanged();
                            sw.setText(activated ? viewGroup.getResources().getString(R.string.service_activated) : viewGroup.getResources().getString(R.string.service_disabled));
                            sw.setEnabled(true);
                        } else {
                            sw.setEnabled(false);
                            sw.setText(viewGroup.getResources().getString((isChecked) ? R.string.activating_service : R.string.disabling_service));
                        }
                        sw.setOnCheckedChangeListener(onCheckedChangeListener);
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };
        swActivated.setOnCheckedChangeListener(onCheckedChangeListener);

        return rowView;
    }
}

