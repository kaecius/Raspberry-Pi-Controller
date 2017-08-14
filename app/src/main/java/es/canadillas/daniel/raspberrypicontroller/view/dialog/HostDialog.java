package es.canadillas.daniel.raspberrypicontroller.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import es.canadillas.daniel.raspberrypicontroller.R;

/**
 * Created by dani on 14/08/2017.
 */

public class HostDialog extends DialogFragment {

    public interface HostDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    HostDialogListener hostDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_host_dialog,null));
        builder.setPositiveButton(R.string.host_form_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hostDialogListener.onDialogPositiveClick(HostDialog.this);
            }
        });
        builder.setNegativeButton(R.string.host_form_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hostDialogListener.onDialogNegativeClick(HostDialog.this);
            }
        });
        AlertDialog hostDialogFragment = builder.create();
        hostDialogFragment.setCanceledOnTouchOutside(false);
        return hostDialogFragment;
    }

    public void setListener (HostDialogListener hostDialogListener){
        this.hostDialogListener =  hostDialogListener;
    }

}
