package es.canadillas.daniel.raspberrypicontroller.view.item;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.R;
import es.canadillas.daniel.raspberrypicontroller.activity.ServicesActivity;
import es.canadillas.daniel.raspberrypicontroller.controller.SshController;
import es.canadillas.daniel.raspberrypicontroller.dao.DataAccess;
import es.canadillas.daniel.raspberrypicontroller.dao.DataAccessImpl;
import es.canadillas.daniel.raspberrypicontroller.model.Host;
import es.canadillas.daniel.raspberrypicontroller.view.dialog.HostDialog;


public class HostItemAdapter extends BaseAdapter implements HostDialog.HostDialogListener {

    private Context context;
    private List<Host> hosts;
    private int actualHostId;
    private SshController sshController;

    public HostItemAdapter(Context context, List<Host> hosts) {
        this.context = context;
        this.hosts = hosts;
        this.sshController = SshController.getInstance();
    }

    @Override
    public int getCount() {
        return this.hosts.size();
    }

    @Override
    public Object getItem(int i) {
        return this.hosts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        Host host = this.hosts.get(i);
        final int itemPos = i;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.host_card, viewGroup, false);
        }

        ImageButton btnBorrar = rowView.findViewById(R.id.btnDelete);
        ImageButton btnEditar = rowView.findViewById(R.id.btnEdit);
        TextView txtHost = rowView.findViewById(R.id.txtHostName);
        TextView txtID = rowView.findViewById(R.id.txtID);

        txtHost.setText(host.getHostUrl());
        txtID.setText(String.valueOf(host.getId()));

        rowView.setOnClickListener(clickedView -> {
            Host clickedHost = HostItemAdapter.this.hosts.get(itemPos);
            Intent intent = new Intent(context, ServicesActivity.class);
            intent.putExtra("host", clickedHost);
            context.startActivity(intent);
        });

        btnBorrar.setOnClickListener(clickedView -> {
            DataAccess dao = DataAccessImpl.getInstance();
            dao.deleteHost(HostItemAdapter.this.hosts.get(itemPos));
            hosts.remove(itemPos);
            HostItemAdapter.this.notifyDataSetChanged();
            Toast.makeText(HostItemAdapter.this.context, context.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
        });
        btnEditar.setOnClickListener(clickedView -> {
            LinearLayout hostListView = (LinearLayout) clickedView.getParent().getParent();
            String idStr = ((TextView) hostListView.findViewById(R.id.txtID)).getText().toString();
            actualHostId = Integer.parseInt(idStr);
            HostDialog hostDialog = new HostDialog();
            hostDialog.setListener(HostItemAdapter.this);
            hostDialog.show(((Activity) context).getFragmentManager(), "HostDialogFragment");
            //TODO rellenar los input con la informacion del host a editar
        });
        return rowView;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();
        EditText txtHost = dialogView.findViewById(R.id.edTxtHost);
        EditText txtUser = dialogView.findViewById(R.id.edTxtUser);
        EditText txtPassword = dialogView.findViewById(R.id.edTxtPassword);

        String hostStr = txtHost.getText().toString();
        String userStr = txtUser.getText().toString();
        String passwordStr = txtPassword.getText().toString();

        if (!hostStr.isEmpty() && !userStr.isEmpty() && !passwordStr.isEmpty()) {
            Host hostModified;
            int i;
            for (i = 0; i < hosts.size() && hosts.get(i).getId() != actualHostId; i++) ;
            if (i < hosts.size()) {
                hostModified = hosts.get(i);
                hostModified.setHostUrl(sshController.getHostFromHostStirng(hostStr));
                hostModified.setPort(sshController.getPortFromHostString(hostStr));
                hostModified.setUser(userStr);
                hostModified.setPassword(passwordStr);
                if (sshController.editHost(hostModified)) {
                    hosts.set(i, hostModified);
                    HostItemAdapter.this.notifyDataSetChanged();
                } else {
                    Toast.makeText(dialog.getDialog().getContext(), context.getString(R.string.error_editing), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(dialog.getDialog().getContext(), context.getString(R.string.error_editing), Toast.LENGTH_SHORT).show();
            }
        }
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
