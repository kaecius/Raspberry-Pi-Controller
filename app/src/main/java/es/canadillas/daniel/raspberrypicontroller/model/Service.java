package es.canadillas.daniel.raspberrypicontroller.model;

/**
 * Created by dani on 15/08/2017.
 */

public class Service {

    private String name;
    private boolean isActivated;

    public Service(String name, boolean isActivated) {
        this.name = name;
        this.isActivated = isActivated;
    }

    public Service(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

}
