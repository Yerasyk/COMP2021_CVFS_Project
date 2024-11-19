package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;

public class File implements Serializable {
    private String name;

    public File(String name){
        this.setName(name);
    }

    public void setName(String name){
        if (name.length() > 10) {
            throw new StateChangeCommandFailed("File name must not exceed 10 characters.");
        }
        if (!name.matches("^[a-zA-Z0-9]*$")) {
            throw new StateChangeCommandFailed("File name can only contain letters and digits.");
        }
        // Ensure it is not empty; assign a default name if necessary
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getSize(){
        return 40;
    }
}
