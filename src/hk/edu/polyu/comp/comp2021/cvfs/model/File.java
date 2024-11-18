package hk.edu.polyu.comp.comp2021.cvfs.model;

public class File {
    private String name;

    public File(String name){
        this.setName(name);
    }

    public void setName(String name) throws IllegalArgumentException{
        if (!name.matches("^[a-zA-Z0-9]*$")) {
            throw new IllegalArgumentException("File name can only contain letters and digits.");
        }
        if (name.length() > 10) {
            throw new IllegalArgumentException("File name must not exceed 10 characters.");
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
