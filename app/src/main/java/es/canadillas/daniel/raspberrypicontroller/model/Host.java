package es.canadillas.daniel.raspberrypicontroller.model;

import android.database.Cursor;

import es.canadillas.daniel.raspberrypicontroller.dao.sqlite.DataContract;

/**
 * Created by dani on 14/08/2017.
 */

public class Host {

    private int id;
    private String name;
    private String hash;

    public Host(){
        this.id = -1;
    }

    public Host(int id, String name, String hash){

    }

    public Host(Cursor c) {
        this.id = c.getInt(0);
        this.name = c.getString(1);
        this.hash = c.getString(2);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
