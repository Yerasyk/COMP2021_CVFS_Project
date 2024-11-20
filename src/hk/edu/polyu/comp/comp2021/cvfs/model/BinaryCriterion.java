package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;
/**
 * Represents a binary criterion that combines two other criteria using a logical operator.
 * The logical operator can be either AND ("&&") or OR ("||").
 */
public class BinaryCriterion extends Criterion implements Serializable {
    private final Criterion criterionF;
    private final Criterion criterionS;
    private final String logicOp;

    /**
     * Constructs a BinaryCriterion with the specified name, two criteria, and a logical operator.
     *
     * @param criName   The name of the binary criterion (must be exactly two letters).
     * @param criterion1 The first criterion to be combined.
     * @param logOp     The logical operator used to combine the criteria ("&&" or "||").
     * @param criterion2 The second criterion to be combined.
     * @throws StateChangeCommandFailed If the logical operator is invalid.
     */
    public BinaryCriterion(String criName, Criterion criterion1, String logOp, Criterion criterion2){
        super(criName);
        criterionF = criterion1;
        criterionS = criterion2;
        if(!(logOp.equals("&&")||logOp.equals("||"))){
            throw new StateChangeCommandFailed("Logical operator must be either \"&&\" or \"||\"");
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
