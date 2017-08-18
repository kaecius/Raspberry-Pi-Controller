package es.canadillas.daniel.raspberrypicontroller.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.R;
import es.canadillas.daniel.raspberrypicontroller.controller.SshController;
import es.canadillas.daniel.raspberrypicontroller.model.Host;
import es.canadillas.daniel.raspberrypicontroller.view.dialog.HostDialog;
import es.canadillas.daniel.raspberrypicontroller.view.item.HostItemAdapter;

public class MainActivity extends AppCompatActivity implements HostDialog.HostDialogListener {

    private SshController mSshController;
    private HostDialog mhostDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSshController = SshController.getInstance();
        new AsyncTask<Void,Void,Void>(){

            private ListView lsHosts;
            private List<Host> hosts;

            @Override
            protected Void doInBackground(Void... voids) {
                lsHosts = (ListView) findViewById(R.id.lsHosts);
                hosts = mSshController.getHosts();
                publishProgress();
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
                if (hosts != null){
                    lsHosts.setAdapter(new HostItemAdapter(MainActivity.this,hosts));
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean returnable = true;

        switch (item.getItemId()){

            case R.id.action_add:
                    showHostDialog();
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return returnable;
    }

    public void showHostDialog() {
        mhostDialog = new HostDialog();
        mhostDialog.setListener(this);
        mhostDialog.show(getFragmentManager(), "HostDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();
        Toast.makeText(this,"possitive",Toast.LENGTH_SHORT).show();
        EditText txtHost =  dialogView.findViewById(R.id.edTxtHost);
        EditText txtUser =  dialogView.findViewById(R.id.edTxtUser);
        EditText txtPassword =  dialogView.findViewById(R.id.edTxtPassword);

        String hostStr = txtHost.getText().toString();
        String userStr = txtUser.getText().toString();
        String passwordStr = txtPassword.getText().toString();
        //TODO mostrar mensaje o no cerrar cuando falla
        if(!hostStr.isEmpty() && !userStr.isEmpty() && !passwordStr.isEmpty()){
            try{
                mSshController.addHost(hostStr,userStr,passwordStr);
                Toast.makeText(this,"New host introduced",Toast.LENGTH_SHORT).show();
            }catch (NumberFormatException ex){
                Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show();
            }
        }else{
            if (hostStr.isEmpty()){
                txtHost.setTextColor(0xAAff0000);
            }
            if (userStr.isEmpty()){
                txtUser.setTextColor(0xAAff0000);
            }
            if (passwordStr.isEmpty()){
                txtPassword.setTextColor(0xAAff0000);
            }
        }
        for(Host h : mSshController.getHosts()){
            System.out.println("Host :" + h.getHostUrl());
        }
        HostItemAdapter hostItemAdapter = ((HostItemAdapter) ((ListView) findViewById(R.id.lsHosts)).getAdapter());
        hostItemAdapter.setHosts(mSshController.getHosts());
        hostItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(this,"Negagative",Toast.LENGTH_SHORT).show();
    }
}
