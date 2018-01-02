package es.canadillas.daniel.raspberrypicontroller.activity;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import es.canadillas.daniel.raspberrypicontroller.R;
import es.canadillas.daniel.raspberrypicontroller.model.Host;
import es.canadillas.daniel.raspberrypicontroller.model.HostConnection;
import es.canadillas.daniel.raspberrypicontroller.model.Service;
import es.canadillas.daniel.raspberrypicontroller.view.item.ServiceItemAdapter;

public class ServicesActivity extends AppCompatActivity {

    private Host mHost;
    private HostConnection mHostConnection;
    private Menu menu;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        mHost = (Host) getIntent().getExtras().get("host");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mHost.getHostUrl());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        initConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.services_menu, menu);
        this.menu = menu;
        refreshHostStatus();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_refresh:
                refreshHostStatus();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private void refreshHostStatus() {
        new AsyncTask<Void, Void, Void>() {

            MenuItem status;
            MenuItem refresh;
            ProgressBar indeterminate = (ProgressBar) findViewById(R.id.barIndeterminate);
            TextView noServices = (TextView) findViewById(R.id.txtNoServices);
            ListView lsServices = (ListView) findViewById(R.id.lsServices);
            boolean checked = false;


            @Override
            protected Void doInBackground(Void... voids) {
                ServicesActivity.this.isConnected = false;
                publishProgress();
                if (HostConnection.testConnection(mHost)) {
                    ServicesActivity.this.isConnected = true;
                }
                checked = true;
                publishProgress();
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                int i;
                for (i = 0; i < menu.size() && menu.getItem(i).getItemId() != R.id.action_status; i++)
                    ;
                status = menu.getItem(i);
                for (i = 0; i < menu.size() && menu.getItem(i).getItemId() != R.id.action_refresh; i++)
                    ;
                refresh = menu.getItem(i);


                indeterminate.setVisibility(checked ? View.GONE : View.VISIBLE);
                lsServices.setVisibility(View.GONE);
                noServices.setVisibility(View.GONE);

                if (checked) {
                    if (ServicesActivity.this.isConnected) {
                        status.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.green_circle, null));
                    } else {
                        status.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.red_circle, null));
                    }
                    refresh.setEnabled(true);
                    showServicesOnUI();
                } else {
                    refresh.setEnabled(false);
                    status.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.yellow_circle, null));
                }
                super.onProgressUpdate(values);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    private void showServicesOnUI() {
        TextView noServices = (TextView) findViewById(R.id.txtNoServices);
        ListView lsServices = (ListView) findViewById(R.id.lsServices);
        noServices.setVisibility(isConnected ? View.GONE : View.VISIBLE);
        if (isConnected) {
            new AsyncTask<Void, Void, Void>() {

                List<Service> services;
                ProgressBar indeterminate = (ProgressBar) findViewById(R.id.barIndeterminate);

                @Override
                protected Void doInBackground(Void... voids) {
                    publishProgress();
                    try {
                        services = mHostConnection.getServices();
                    } catch (Throwable t) {
                        services = new ArrayList<Service>();
                    }
                    publishProgress();
                    return null;
                }

                @Override
                protected void onProgressUpdate(Void... values) {
                    if (services != null && !services.isEmpty()) {
                        lsServices.setAdapter(new ServiceItemAdapter(ServicesActivity.this, services, mHostConnection));
                        indeterminate.setVisibility(View.GONE);
                        lsServices.setVisibility(View.VISIBLE);
                    } else {
                        if (services != null && services.isEmpty()) {
                            Toast.makeText(ServicesActivity.this, getString(R.string.error_loading_services), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            lsServices.setVisibility(View.GONE);
                            indeterminate.setVisibility(View.VISIBLE);
                        }
                    }
                    super.onProgressUpdate(values);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void initConnection() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mHostConnection = new HostConnection(mHost);
                } catch (Throwable t) {
                    publishProgress();
                    finish();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                if (mHostConnection == null) {
                    Toast.makeText(ServicesActivity.this, getString(R.string.error_establishing_connection), Toast.LENGTH_LONG).show();
                }
                super.onProgressUpdate(values);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


}
