package es.canadillas.daniel.raspberrypicontroller.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.R;
import es.canadillas.daniel.raspberrypicontroller.controller.SshController;
import es.canadillas.daniel.raspberrypicontroller.model.Host;
import es.canadillas.daniel.raspberrypicontroller.view.dialog.HostDialog;

public class MainActivity extends AppCompatActivity implements HostDialog.HostDialogListener {

    private SshController mSshController;
    private HostDialog mhostDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSshController = SshController.getInstance();
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

        String host = txtHost.getText().toString();
        String user = txtUser.getText().toString();
        String password = txtPassword.getText().toString();
    //TODO mostrar mensaje o no cerrar cuando falla
        if(!host.isEmpty() && !user.isEmpty() && !password.isEmpty()){
            mSshController.addHost(host,user,password);
            Toast.makeText(this,"New host introduced",Toast.LENGTH_SHORT).show();
        }else{
            if (host.isEmpty()){
                txtHost.setTextColor(0xAAff0000);
            }
            if (user.isEmpty()){
                txtUser.setTextColor(0xAAff0000);
            }
            if (password.isEmpty()){
                txtPassword.setTextColor(0xAAff0000);
            }
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(this,"Negagative",Toast.LENGTH_SHORT).show();
    }
}
