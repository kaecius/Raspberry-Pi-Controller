package es.canadillas.daniel.raspberrypicontroller.controller;

import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.dao.DataAccess;
import es.canadillas.daniel.raspberrypicontroller.dao.DataAccessImpl;
import es.canadillas.daniel.raspberrypicontroller.model.Host;
import es.canadillas.daniel.raspberrypicontroller.util.Security;

/**
 * Created by dani on 14/08/2017.
 */

public class SshController {
    private static final SshController ourInstance = new SshController();

    private DataAccess mDao = DataAccessImpl.getInstance();

    public static SshController getInstance() {
        return ourInstance;
    }

    public void addHost(String host, String user, String password) {
        String salt = Security.generateSalt();
        String hash = Security.toHash(password,salt);
        mDao.addHost(host,user,password,hash,salt);
    }

    public List<Host> getHosts() {
        return mDao.getHosts();
    }
}
