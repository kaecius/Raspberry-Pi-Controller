package es.canadillas.daniel.raspberrypicontroller.dao;

import java.util.List;

import es.canadillas.daniel.raspberrypicontroller.model.Host;

/**
 * Created by dani on 14/08/2017.
 */

interface DataAccess {

    public List<Host> getHosts();
    public boolean isValid(Host host);
    public Host getHost(int id);


}
