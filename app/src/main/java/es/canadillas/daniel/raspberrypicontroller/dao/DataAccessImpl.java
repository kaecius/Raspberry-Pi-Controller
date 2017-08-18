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
                DataContract.DataEntry.PASS_COLUMN_NAME,
                DataContract.DataEntry.PORT_COLUMN_NAME
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
    public Host getHost(int id) {
        return null;
    }

    @Override
    public void addHost(String host, String user, String password,int port) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataContract.DataEntry.HOST_COLUMN_NAME , host);
        values.put(DataContract.DataEntry.USER_COLUMN_NAME , user);
        values.put(DataContract.DataEntry.PASS_COLUMN_NAME , password);
        values.put(DataContract.DataEntry.PORT_COLUMN_NAME, port);
        db.insert(DataContract.DataEntry.DATA_TABLE_NAME,null,values);
    }

    @Override
    public void deleteHost(Host host) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = DataContract.DataEntry._ID + "= ?";
        String[] selectionArgs = { String.valueOf(host.getId()) };
        db.delete(DataContract.DataEntry.DATA_TABLE_NAME,selection,selectionArgs);
    }


    public void editHost(Host host){

    }

}
