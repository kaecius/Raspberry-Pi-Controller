package es.canadillas.daniel.raspberrypicontroller.activity;

import android.content.ClipData;
import android.os.AsyncTask;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import es.canadillas.daniel.raspberrypicontroller.R;
import es.canadillas.daniel.raspberrypicontroller.model.Host;
import es.canadillas.daniel.raspberrypicontroller.model.HostConnection;

public class ServicesActivity extends AppCompatActivity {

    private Host mHost;
    private HostConnection mHostConnection;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        mHost = (Host) getIntent().getExtras().get("host");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mHost.getHostUrl());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        Toast.makeText(this,"Checking Connection",Toast.LENGTH_LONG).show();
        initConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.services_menu, menu);
        this.menu = menu;
        refreshHostStatus();
        // return true so that the menu pop up is opened
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_refresh:
                refreshHostStatus();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshHostStatus() {
            new AsyncTask<Void,Void,Void>(){

                boolean connected = false;
                boolean checked = false;
                @Override
                protected Void doInBackground(Void... voids) {
                    publishProgress();
                    if (HostConnection.testConnection(mHost)){
                        connected = true;
                    }
                    checked = true;
                    publishProgress();
                    return null;
                }

                @Override
                protected void onProgressUpdate(Void... values) {
                    MenuItem status;
                    MenuItem refresh;
                    int i;
                    for (i = 0; i < menu.size() && menu.getItem(i).getItemId() != R.id.action_status; i++);
                    status = menu.getItem(i);
                    for (i = 0; i < menu.size() && menu.getItem(i).getItemId() != R.id.action_refresh; i++);
                    refresh = menu.getItem(i);
                    if (checked){
                        if (connected){
                            status.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.green_circle,null));
                        }else{
                            status.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.red_circle,null));
                        }
                        refresh.setEnabled(true);
                    }else{
                        refresh.setEnabled(false);
                        status.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.yellow_circle,null));
                    }
                    super.onProgressUpdate(values);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void  initConnection(){
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    try{
                        mHostConnection = new HostConnection(mHost);
                    }catch (Throwable t){
                        publishProgress();
                        finish();
                    }
                    return null;
                }
                @Override
                protected void onProgressUpdate(Void... values) {
                    if (mHostConnection == null){
                        Toast.makeText(ServicesActivity.this,"Error while establishing connection",Toast.LENGTH_LONG).show();
                    }
                    super.onProgressUpdate(values);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


}
