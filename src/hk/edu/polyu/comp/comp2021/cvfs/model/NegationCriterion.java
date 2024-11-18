package hk.edu.polyu.comp.comp2021.cvfs.model;

public class NegationCriterion extends Criterion {
    private final Criterion innerCriterion;

    public NegationCriterion(String criName, Criterion criterion){
        super(criName);
        innerCriterion = criterion;
    }

    @Override
    public boolean matches(File file) {
        return !innerCriterion.matches(file);
    }

    @Override
    public String toString() {
        return "NegationCri {name= '"+getName()+"', negated= '"+innerCriterion.getName()+"'}";
    }
}
