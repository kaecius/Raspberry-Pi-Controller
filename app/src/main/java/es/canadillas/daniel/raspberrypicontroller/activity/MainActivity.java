package es.canadillas.daniel.raspberrypicontroller.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.hosts_bar_title);
        mSshController = SshController.getInstance();
        AsyncTask<Void,Void,Void> asyncTask = new LoadHosts().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        asyncTask = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.action_add:
                showHostDialog();
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void showHostDialog() {
        HostDialog mhostDialog = new HostDialog();
        mhostDialog.setListener(this);
        mhostDialog.show(getFragmentManager(), "HostDialogFragment");
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
        //TODO mostrar mensaje o no cerrar cuando falla
        if (!hostStr.isEmpty() && !userStr.isEmpty() && !passwordStr.isEmpty()) {
            try {
                mSshController.addHost(hostStr, userStr, passwordStr);
            } catch (NumberFormatException ex) {
                Toast.makeText(this, getString(R.string.error_new_host), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.error_new_host), Toast.LENGTH_SHORT).show();
        }
        HostItemAdapter hostItemAdapter = ((HostItemAdapter) ((ListView) findViewById(R.id.lsHosts)).getAdapter());
        hostItemAdapter.setHosts(mSshController.getHosts());
        hostItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadHosts extends AsyncTask<Void,Void,Void>{

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
            if (hosts != null) {
                lsHosts.setAdapter(new HostItemAdapter(MainActivity.this, hosts));
            }
        }
    }

}
