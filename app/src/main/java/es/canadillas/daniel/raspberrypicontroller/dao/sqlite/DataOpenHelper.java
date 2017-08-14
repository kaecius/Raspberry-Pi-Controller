package es.canadillas.daniel.raspberrypicontroller.dao.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dani on 14/08/2017.
 */

public class DataOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String COMMA_SEP = ",";
    private static final String TEXT_TYPE = " TEXT";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DataContract.DataEntry.DATA_TABLE_NAME + "( "
            + DataContract.DataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DataContract.DataEntry.HOST_COLUMN_NAME + TEXT_TYPE + COMMA_SEP
            + DataContract.DataEntry.HASH_COLUMN_NAME + TEXT_TYPE + COMMA_SEP
            + DataContract.DataEntry.SALT_COLUMN_NAME + TEXT_TYPE + COMMA_SEP
            + " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DataContract.DataEntry.DATA_TABLE_NAME;

    public DataOpenHelper(Context context){
        super(context, DataContract.DataEntry.DATA_TABLE_NAME,null,DataOpenHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataOpenHelper.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
