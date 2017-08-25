package es.canadillas.daniel.raspberrypicontroller.dao.sqlite;

import android.provider.BaseColumns;


public final class DataContract {

    private DataContract() {

    }

    public static class DataEntry implements BaseColumns {
        public static final String DATA_TABLE_NAME = "ssh_info";
        public static final String HOST_COLUMN_NAME = "host";
        public static final String USER_COLUMN_NAME = "user";
        public static final String PASS_COLUMN_NAME = "password";
        public static final String PORT_COLUMN_NAME = "port";
    }

}
