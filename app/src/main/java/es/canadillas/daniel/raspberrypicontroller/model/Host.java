package es.canadillas.daniel.raspberrypicontroller.model;

import android.database.Cursor;

/**
 * Created by dani on 14/08/2017.
 */

public class Host {

    private int id;
    private String hostUrl;
    private String user;
    private String hash;

    public Host(){
        this.id = -1;
    }

    public Host(int id, String hostUrl,String user, String hash){
            this.id = id;
        this.hostUrl = hostUrl;
        this.user = user;
        this.hash = hash;
    }

    public Host(Cursor c) {
        this.id = c.getInt(0);
        this.hostUrl = c.getString(1);
        this.user = c.getString(2);
        this.hash = c.getString(3);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
