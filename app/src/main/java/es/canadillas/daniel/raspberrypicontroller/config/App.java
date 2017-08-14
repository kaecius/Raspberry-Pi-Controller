package es.canadillas.daniel.raspberrypicontroller.config;

import android.app.Application;
import android.content.Context;

/**
 * Created by dani on 14/08/2017.
 */

public class App extends Application {
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
