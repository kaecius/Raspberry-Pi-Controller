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
        String host = getHostFromHostStirng(hostStr);
        int port = getPortFromHostString(hostStr);
        mDao.addHost(host,user,password, port);
    }

    public List<Host> getHosts() {
        return mDao.getHosts();
    }

    public boolean editHost(Host host){
        return mDao.editHost(host);
    }

    public int getPortFromHostString(String hostString){
        int port = 22;
        if (hostString.contains(":")){
            port = Integer.parseInt(hostString.substring(hostString.lastIndexOf(":")+1));
        }
        return port;
    }

    public String getHostFromHostStirng(String hostString){
        String host = hostString;
        if (hostString.contains(":")){
            host = hostString.substring(0,hostString.lastIndexOf(":"));
        }
        return host;
    }

}
