package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;

/**
 * Represents a simple criterion that evaluates files based on specific attributes.
 * The attributes can be name, type, or size, with corresponding operators and values.
 */
public class SimpleCriterion extends Criterion implements Serializable {
    private String attrName;
    private String operator;
    private String value;

    /**
     * Constructs a new simple criterion with the specified name, attribute name, operator, and value.
     *
     * @param criName  The name of the criterion.
     * @param attrName The attribute name (e.g., name, type, size).
     * @param op       The operator used for comparison (e.g., contains, equals, >, <).
     * @param val      The value to compare the attribute against.
     * @throws StateChangeCommandFailed If the combination of attribute name, operator, and value is invalid.
     */
    public SimpleCriterion(String criName, String attrName, String op, String val){
        super(criName);
        this.setOthers(attrName, op, val);
    }

    /**
     * Validates and sets the attribute name, operator, and value for the criterion.
     * Ensures that the combination of these parameters adheres to the rules for each attribute type.
     *
     * @param attrName The attribute name.
     * @param op       The operator.
     * @param val      The value.
     * @throws StateChangeCommandFailed If the combination of parameters is invalid.
     */
    private void setOthers(String attrName, String op, String val){
        switch (attrName){
            case "name":
                if(!op.equals("contains")) {
                    throw new StateChangeCommandFailed("If attrName=name, then op must \"contains\"");
                }
                if(!val.matches("^\"[^\"]*\"$")){
                    throw new StateChangeCommandFailed("If attrName=name, then value must be a string in double quotes.");
                }
                break;
            case "type":
                if(!op.equals("equals")){
                    throw new StateChangeCommandFailed("If attrName=type, then op must be \"equals\".");
                }
                if(!val.matches("^\"[^\"]*\"$")){
                    throw new StateChangeCommandFailed("If attrName=type, then value must be a string in double quotes.");
                }
                break;
            case "size":
                if (!op.matches("^(>|<|>=|<=|==|!=)$")) {
                    throw new StateChangeCommandFailed("If attrName=size, then op must be one of >, <, >=, <=, ==, or !=.");
                }
                if (!val.matches("^-?\\d+$")) {
                    throw new StateChangeCommandFailed("If attrName=size, then value must be an integer.");
                }
                break;
            default:
                throw new StateChangeCommandFailed("attrName must be either name, size, type.");
        }
        this.attrName=attrName;
        this.operator =op;
        this.value =val;
    }

    /**
     * Evaluates whether the specified file matches the criterion.
     *
     * @param file The file to evaluate.
     * @return {@code true} if the file matches the criterion, {@code false} otherwise.
     */
    @Override
    public boolean matches(File file) {
            switch (this.attrName){
                case "name":
                    return file.getName().contains(value.replace("\"", ""));
                case "type":
                    if (file instanceof Document) {
                        return ((Document) file).getType().equals(value.replace("\"", ""));
                    }
                    return false;
                case "size":
                    int fileSize= file.getSize();
                    int value = Integer.valueOf(this.value);
                    switch (this.operator){
                        case "<":
                            return fileSize<value;
                        case "<=":
                            return fileSize<=value;
                        case ">":
                            return fileSize>value;
                        case ">=":
                            return fileSize>=value;
                        case "==":
                            return fileSize==value;
                        case "!=":
                            return fileSize!=value;
                    }
            }
        return false;
    }

    /**
     * Returns a string representation of the simple criterion, including its name, attribute, operator, and value.
     *
     * @return A string representing the simple criterion.
     */
    @Override
    public String toString() {
        return "SimpleCri {name= '"+getName()+"', attrName= '"+attrName+"', op= '"+ operator +"', val= '"+ value+"'}";
    }
}
