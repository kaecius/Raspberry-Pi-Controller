package es.canadillas.daniel.raspberrypicontroller.dao;

import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.model.Host;

/**
 * Created by dani on 14/08/2017.
 */

public interface DataAccess {

     List<Host> getHosts();
     Host getHost(int id);
     void addHost(String host, String user, String password, int port);
     void deleteHost(Host host);
     void editHost(Host host);
}
