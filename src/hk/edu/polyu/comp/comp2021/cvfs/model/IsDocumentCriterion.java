package hk.edu.polyu.comp.comp2021.cvfs.model;

public class IsDocumentCriterion extends Criterion{
    public IsDocumentCriterion(){
        super("IsDocument");
    }

    @Override
    public boolean matches(File file) {
        return file instanceof Document;
    }

    @Override
    public String toString() {
        return "Built-in-Criterion {name= '"+getName()+"'}";
    }
}
