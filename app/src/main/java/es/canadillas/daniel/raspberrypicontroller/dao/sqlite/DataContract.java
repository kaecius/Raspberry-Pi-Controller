package es.canadillas.daniel.raspberrypicontroller.dao.sqlite;

import android.provider.BaseColumns;

/**
 * Created by dani on 14/08/2017.
 */

public final class DataContract {

    private DataContract(){

    }

    public static class DataEntry implements BaseColumns{
        public static final String DATA_TABLE_NAME = "ssh_info";
        public static final String HOST_COLUMN_NAME = "host";
        public static final String HASH_COLUMN_NAME = "hash";
        public static final String SALT_COLUMN_NAME = "salt";
    }

}
