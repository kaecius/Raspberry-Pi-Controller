package es.canadillas.daniel.raspberrypicontroller.activity;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import es.canadillas.daniel.raspberrypicontroller.R;
import es.canadillas.daniel.raspberrypicontroller.controller.SshController;
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
        Toast.makeText(this,"possitive",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(this,"Negagative",Toast.LENGTH_SHORT).show();
    }
}
