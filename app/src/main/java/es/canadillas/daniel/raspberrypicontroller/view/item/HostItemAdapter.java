package es.canadillas.daniel.raspberrypicontroller.view.item;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.R;
import es.canadillas.daniel.raspberrypicontroller.dao.DataAccess;
import es.canadillas.daniel.raspberrypicontroller.dao.DataAccessImpl;
import es.canadillas.daniel.raspberrypicontroller.model.Host;
import es.canadillas.daniel.raspberrypicontroller.view.dialog.HostDialog;

/**
 * Created by dani on 16/08/2017.
 */

public class HostItemAdapter extends BaseAdapter  implements HostDialog.HostDialogListener{

    private Context context;
    private List<Host> hosts;

    public HostItemAdapter(Context context, List<Host> hosts) {
        this.context = context;
        this.hosts = hosts;
    }

    @Override
    public int getCount() {
        return this.hosts.size();
    }

    @Override
    public Object getItem(int i) {
        return  this.hosts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        final int itemPos = i;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.host_card,viewGroup,false);
        }
        ImageButton btnBorrar = rowView.findViewById(R.id.btnDelete);
        ImageButton btnEditar = rowView.findViewById(R.id.btnEdit);
        TextView txtHost = rowView.findViewById(R.id.txtHostName);
        txtHost.setText(this.hosts.get(i).getHostUrl());
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataAccess dao = DataAccessImpl.getInstance();
                dao.deleteHost(HostItemAdapter.this.hosts.get(itemPos));
                hosts.remove(itemPos);
                HostItemAdapter.this.notifyDataSetChanged();
                Toast.makeText(HostItemAdapter.this.context,"Borrado!",Toast.LENGTH_SHORT).show();
            }
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HostDialog hostDialog  = new HostDialog();
                hostDialog.setListener(HostItemAdapter.this);
                hostDialog.show(((Activity) context).getFragmentManager(), "HostDialogFragment");
            }
        });
        return rowView;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //TODO hacer editar
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }
}
