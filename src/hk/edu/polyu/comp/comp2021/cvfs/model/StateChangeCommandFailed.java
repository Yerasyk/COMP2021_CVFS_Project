package hk.edu.polyu.comp.comp2021.cvfs.model;

/**
 * Represents a runtime exception that occurs when a state-changing command fails in the CVFS system.
 * This exception is thrown to indicate errors in operations such as creating, renaming, or deleting files, and e.t.c.
 */
public class StateChangeCommandFailed extends RuntimeException{
    /**
     * Constructs a new {@code StateChangeCommandFailed} exception with the specified error message.
     *
     * @param errorMessage The detail message explaining the cause of the exception.
     */
    public StateChangeCommandFailed(String errorMessage){
        super(errorMessage);
    }
}
