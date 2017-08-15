package es.canadillas.daniel.raspberrypicontroller.dao;

import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.model.Host;

/**
 * Created by dani on 14/08/2017.
 */

public interface DataAccess {

     List<Host> getHosts();
     boolean isValid(String password,Host host);
     Host getHost(int id);
    void addHost(String host, String user, String password, String hash, String salt);
}
