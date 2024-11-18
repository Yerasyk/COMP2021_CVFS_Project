package hk.edu.polyu.comp.comp2021.cvfs.model;

public class BinaryCriterion extends Criterion{
    private final Criterion criterionF;
    private final Criterion criterionS;
    private final String logicOp;

    public BinaryCriterion(String criName, Criterion criterion1, String logOp, Criterion criterion2){
        super(criName);
        criterionF = criterion1;
        criterionS = criterion2;
        if(!(logOp.equals("&&")||logOp.equals("||"))){
            throw new IllegalArgumentException("Logical operator must be either \"&&\" or \"||\"");
        }
        logicOp = logOp;
    }

    @Override
    public boolean matches(File file) {
        if(logicOp.equals("&&")) {
            return criterionF.matches(file) && criterionS.matches(file);
        }else{
            return criterionF.matches(file) || criterionS.matches(file);
        }
    }

    @Override
    public String toString() {
        return "BinaryCri {name= '"+getName()+"', criterion1= '"+criterionF.getName()+"', criterion2= '"+ criterionS+"'}";
    }
}
