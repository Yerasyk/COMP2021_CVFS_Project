package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;
/**
 * Represents an abstract criterion used to evaluate files based on specific conditions.
 * Subclasses of this class define concrete criteria with specific matching logic.
 */
public abstract class Criterion implements Serializable {
    private String name;

    /**
     * Constructs a new criterion with the specified name.
     *
     * @param criName The name of the criterion. Must consist of exactly two English letters or be "IsDocument".
     * @throws StateChangeCommandFailed If the name does not follow the required format.
     */
    public Criterion(String criName){
        setName(criName);
    }
    private void setName(String criName){
        if (!(criName.matches("^[a-zA-Z]{2}$") || criName.equals("IsDocument"))) {
            throw new StateChangeCommandFailed("criName must contain exactly two English letters.");
        }
        this.name=criName;
    }

    /**
     * Retrieves the name of the criterion.
     *
     * @return The name of the criterion.
     */
    public String getName() {
        return name;
    }

    /**
     * Abstract method to determine whether the criterion matches a given file.
     * Subclasses must provide an implementation for this method.
     *
     * @param file The file to evaluate.
     * @return {@code true} if the file matches the criterion, {@code false} otherwise.
     */
    public abstract boolean matches(File file);

    @Override
    public abstract String toString();
}
