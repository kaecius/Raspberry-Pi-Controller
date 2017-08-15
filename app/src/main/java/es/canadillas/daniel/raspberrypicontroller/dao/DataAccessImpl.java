package es.canadillas.daniel.raspberrypicontroller.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.config.App;
import es.canadillas.daniel.raspberrypicontroller.dao.sqlite.DataContract;
import es.canadillas.daniel.raspberrypicontroller.dao.sqlite.DataOpenHelper;
import es.canadillas.daniel.raspberrypicontroller.model.Host;
import es.canadillas.daniel.raspberrypicontroller.util.Security;

/**
 * Created by dani on 14/08/2017.
 */

public class DataAccessImpl implements DataAccess {
    private static final DataAccessImpl ourInstance = new DataAccessImpl();

    private DataOpenHelper mDbHelper = new DataOpenHelper(App.getContext());

    public static DataAccessImpl getInstance() {
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
                DataContract.DataEntry.USER_COLUMN_NAME,
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
    public boolean isValid(String password, Host host) {
        boolean isValid = false;
        if (!password.isEmpty()){
            String salt = getSalt(host);
            String tempPassHashed = Security.toHash(password,salt);
            if (tempPassHashed.equals(host.getHash())){
                isValid = true;
            }
        }
        return isValid;
    }

    @Override
    public Host getHost(int id) {
        return null;
    }

    @Override
    public void addHost(String host, String user, String password, String hash, String salt) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataContract.DataEntry.HOST_COLUMN_NAME , host);
        values.put(DataContract.DataEntry.USER_COLUMN_NAME , user);
        values.put(DataContract.DataEntry.HASH_COLUMN_NAME , hash);
        values.put(DataContract.DataEntry.SALT_COLUMN_NAME , salt);
        db.insert(DataContract.DataEntry.DATA_TABLE_NAME,null,values);
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
