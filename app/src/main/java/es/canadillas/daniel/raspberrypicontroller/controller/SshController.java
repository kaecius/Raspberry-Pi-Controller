package es.canadillas.daniel.raspberrypicontroller.controller;

import es.canadillas.daniel.raspberrypicontroller.dao.DataAccess;
import es.canadillas.daniel.raspberrypicontroller.dao.DataAccessImpl;

/**
 * Created by dani on 14/08/2017.
 */

public class SshController {
    private static final SshController ourInstance = new SshController();

    private DataAccess mDao = DataAccessImpl.getInstance();

    public static SshController getInstance() {
        return ourInstance;
    }
}
