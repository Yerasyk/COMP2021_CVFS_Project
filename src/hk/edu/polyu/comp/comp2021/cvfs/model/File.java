package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;

/**
 * Represents an abstract file in the virtual file system.
 * This class serves as the base for specific types of files, such as documents and directories.
 * Each file has a name and a default size.
 */
public abstract class File implements Serializable {
    private String name;
    /**
     * The default size of a file in bytes.
     */
    protected final int DEFAULT_SIZE=40;

    /**
     * Constructs a new file with the specified name.
     *
     * @param name The name of the file. Must not exceed 10 characters and can only contain letters and digits.
     * @throws StateChangeCommandFailed If the file name exceeds 10 characters or contains invalid characters.
     */
    public File(String name){
        this.setName(name);
    }

    /**
     * Sets the name of the file. The name must not exceed 10 characters and can only contain letters and digits.
     *
     * @param name The new name of the file.
     * @throws StateChangeCommandFailed If the file name exceeds 10 characters or contains invalid characters.
     */
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

    /**
     * Retrieves the name of the file.
     *
     * @return The name of the file.
     */
    public String getName(){
        return name;
    }

    /**
     * Retrieves the size of the file in bytes.
     * By default, this returns the base size defined in {@code DEFAULT_SIZE}.
     *
     * @return The size of the file in bytes.
     */
    public int getSize(){
        return DEFAULT_SIZE;
    }

    @Override
    public abstract String toString();
}
