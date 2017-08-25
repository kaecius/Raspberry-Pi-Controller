package es.canadillas.daniel.raspberrypicontroller.model;


public class Service {

    private String name;
    private boolean isActivated;

    public Service(String name, boolean isActivated) {
        this.name = name;
        this.isActivated = isActivated;
    }

    public Service() {

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
