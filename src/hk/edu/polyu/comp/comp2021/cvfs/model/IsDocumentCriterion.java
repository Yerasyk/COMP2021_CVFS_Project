package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;

public class IsDocumentCriterion extends Criterion implements Serializable {
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
