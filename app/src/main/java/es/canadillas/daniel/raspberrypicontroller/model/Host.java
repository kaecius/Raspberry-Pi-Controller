package es.canadillas.daniel.raspberrypicontroller.model;

import android.database.Cursor;

import java.io.Serializable;


public class Host implements Serializable {

    private int id;
    private String hostUrl;
    private String user;
    private String password;
    private int port;


    public Host() {
        this.id = -1;
    }

    public Host(int id, String hostUrl, String user, String password, int port) {
        this.id = id;
        this.hostUrl = hostUrl;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    public Host(Cursor c) {
        this.id = c.getInt(0);
        this.hostUrl = c.getString(1);
        this.user = c.getString(2);
        this.password = c.getString(3);
        this.port = c.getInt(4);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
