package es.canadillas.daniel.raspberrypicontroller.controller;

import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.dao.DataAccess;
import es.canadillas.daniel.raspberrypicontroller.dao.DataAccessImpl;
import es.canadillas.daniel.raspberrypicontroller.model.Host;

/**
 * Created by dani on 14/08/2017.
 */

public class SshController {
    private static final SshController ourInstance = new SshController();

    private DataAccess mDao = DataAccessImpl.getInstance();

    public static SshController getInstance() {
        return ourInstance;
    }

    public void addHost(String hostStr, String user, String password) throws NumberFormatException {
        String host = hostStr;
        int port = 22;
        if (hostStr.contains(":")){
            host = hostStr.substring(0,hostStr.lastIndexOf(":")-1);
            port = Integer.parseInt(hostStr.substring(hostStr.lastIndexOf(":")+1));
            System.out.println(host);
        }
        mDao.addHost(host,user,password, port);
    }

    public List<Host> getHosts() {
        return mDao.getHosts();
    }
}
