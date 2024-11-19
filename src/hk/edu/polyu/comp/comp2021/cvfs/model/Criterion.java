package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;

public abstract class Criterion implements Serializable {
    private String name;

    public Criterion(String criName){
        setName(criName);
    }
    private void setName(String criName){
        if (!(criName.matches("^[a-zA-Z]{2}$") || criName.equals("IsDocument"))) {
            throw new StateChangeCommandFailed("criName must contain exactly two English letters.");
        }
        this.name=criName;
    }

    public String getName() {
        return name;
    }

    public abstract boolean matches(File file);

    @Override
    public abstract String toString();
}
