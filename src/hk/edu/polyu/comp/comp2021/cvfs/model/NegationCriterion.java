package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;

/**
 * Represents a negation criterion that inverts the result of another criterion.
 * This criterion matches files that do not match the given inner criterion.
 */
public class NegationCriterion extends Criterion implements Serializable {
    private final Criterion innerCriterion;

    /**
     * Constructs a new negation criterion with the specified name and the inner criterion to negate.
     *
     * @param criName   The name of the negation criterion.
     * @param criterion The criterion to negate.
     */

    public NegationCriterion(String criName, Criterion criterion){
        super(criName);
        innerCriterion = criterion;
    }

    /**
     * Checks if the specified file matches the negation criterion.
     * The file matches if and only if it does not match the inner criterion.
     *
     * @param file The file to evaluate.
     * @return {@code true} if the file does not match the inner criterion, {@code false} otherwise.
     */
    @Override
    public boolean matches(File file) {
        return !innerCriterion.matches(file);
    }

    /**
     * Returns a string representation of the negation criterion, including its name and the name of the negated criterion.
     *
     * @return A string representing the negation criterion.
     */
    @Override
    public String toString() {
        return "NegationCri {name= '"+getName()+"', negated= '"+innerCriterion.getName()+"'}";
    }
}
