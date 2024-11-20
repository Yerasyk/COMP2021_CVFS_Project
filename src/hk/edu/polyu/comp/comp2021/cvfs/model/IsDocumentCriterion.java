package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;

/**
 * A built-in criterion that checks whether a file is a document.
 * This criterion is used to filter files of type {@link Document}.
 */
public class IsDocumentCriterion extends Criterion implements Serializable {
    /**
     * Constructs a new `IsDocumentCriterion` with the name "IsDocument".
     */
    public IsDocumentCriterion(){
        super("IsDocument");
    }

    /**
     * Checks if the specified file matches the criterion, i.e., if it is an instance of {@link Document}.
     *
     * @param file The file to evaluate.
     * @return {@code true} if the file is a document, {@code false} otherwise.
     */
    @Override
    public boolean matches(File file) {
        return file instanceof Document;
    }

    /**
     * Returns a string representation of the criterion, including its name.
     *
     * @return A string representing the criterion.
     */
    @Override
    public String toString() {
        return "Built-in-Criterion {name= '"+getName()+"'}";
    }
}
