package es.canadillas.daniel.raspberrypicontroller.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.App;
import es.canadillas.daniel.raspberrypicontroller.dao.sqlite.DataContract;
import es.canadillas.daniel.raspberrypicontroller.dao.sqlite.DataOpenHelper;
import es.canadillas.daniel.raspberrypicontroller.model.Host;

/**
 * Created by dani on 14/08/2017.
 */

class DataAccessImpl implements DataAccess {
    private static final DataAccessImpl ourInstance = new DataAccessImpl();

    private DataOpenHelper mDbHelper = new DataOpenHelper(App.getContext());

    static DataAccessImpl getInstance() {
        return ourInstance;
    }

    private DataAccessImpl() {
    }

    @Override
    public List<Host> getHosts() {
        List<Host> hosts = new ArrayList<Host>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                DataContract.DataEntry._ID,
                DataContract.DataEntry.HOST_COLUMN_NAME,
                DataContract.DataEntry.HASH_COLUMN_NAME
        };
        String sortOrder =
                DataContract.DataEntry._ID + " ASC";

        Cursor c = db.query(DataContract.DataEntry.DATA_TABLE_NAME,projection,null,null,null,null,sortOrder);
        c.moveToFirst();
        do{
            hosts.add(new Host(c));
        }while(c.moveToNext());
        return hosts;
    }

    @Override
    public boolean isValid(Host host) {

        return false;
    }

    @Override
    public Host getHost(int id) {
        return null;
    }

    private String getSalt(Host host){
        String hash = "";

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                DataContract.DataEntry.SALT_COLUMN_NAME
        };
        String selection = DataContract.DataEntry._ID + " = ?";
        String[] selectionArgs = {
            String.valueOf(host.getId())
        };
        Cursor c = db.query(DataContract.DataEntry.DATA_TABLE_NAME,projection,selection,selectionArgs,null,null,null);
        if (c.moveToFirst()){
            hash = c.getString(0);
        }
        return hash;
    }

}
