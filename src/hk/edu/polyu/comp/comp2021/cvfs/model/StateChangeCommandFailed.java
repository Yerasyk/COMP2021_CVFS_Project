package hk.edu.polyu.comp.comp2021.cvfs.model;

public class StateChangeCommandFailed extends RuntimeException{
    public StateChangeCommandFailed(String errorMessage){
        super(errorMessage);
    }
}
