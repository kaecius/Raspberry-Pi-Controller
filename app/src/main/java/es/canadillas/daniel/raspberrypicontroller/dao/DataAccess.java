package es.canadillas.daniel.raspberrypicontroller.dao;

import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.model.Host;


public interface DataAccess {

    List<Host> getHosts();

    Host getHost(int id);

    void addHost(String host, String user, String password, int port);

    void deleteHost(Host host);

    boolean editHost(Host host);
}
