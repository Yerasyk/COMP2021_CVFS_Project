package hk.edu.polyu.comp.comp2021.cvfs.model;

public class SimpleCriterion extends Criterion{
    private String attrName;
    private String operator;
    private String value;

    public SimpleCriterion(String criName, String attrName, String op, String val){
        super(criName);
        this.setOthers(attrName, op, val);
    }

    private void setOthers(String attrName, String op, String val) throws IllegalArgumentException{
        switch (attrName){
            case "name":
                if(!op.equals("contains")) {
                    throw new IllegalArgumentException("If attrName=name, then op must \"contains\"");
                }
                if(!val.matches("^\"[^\"]*\"$")){
                    throw new IllegalArgumentException("If attrName=name, then value must be a string in double quotes.");
                }
                break;
            case "type":
                if(!op.equals("equals")){
                    throw new IllegalArgumentException("If attrName=type, then op must be \"equals\".");
                }
                if(!val.matches("^\"[^\"]*\"$")){
                    throw new IllegalArgumentException("If attrName=name, then value must be a string in double quotes.");
                }
                break;
            case "size":
                if (!op.matches("^(>|<|>=|<=|==|!=)$")) {
                    throw new IllegalArgumentException("If attrName=size, then op must be one of >, <, >=, <=, ==, or !=.");
                }
                if (!val.matches("^-?\\d+$")) {
                    throw new IllegalArgumentException("If attrName=size, then value must be an integer.");
                }
                break;
            default:
                throw new IllegalArgumentException("attrName must be either name, size, type.");
        }
        this.attrName=attrName;
        this.operator =op;
        this.value =val;
    }

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

    @Override
    public String toString() {
        return "SimpleCri {name= '"+getName()+"', attrName= '"+attrName+"', op= '"+ operator +"', val= '"+ value+"'}";
    }
}
