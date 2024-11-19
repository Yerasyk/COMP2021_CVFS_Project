package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CVFSState{
    private final VirtualDisk diskState;
    private final Map<String, Criterion> criterionMapState;

    public CVFSState(VirtualDisk disk, Map<String, Criterion> criterionMap){
        diskState= (VirtualDisk) deepClone(disk);
        criterionMapState=new HashMap<>(criterionMap);
    }

    public VirtualDisk getDiskState(){
        return diskState;
    }

    public Map<String, Criterion> getCriterionMapState(){
        return criterionMapState;
    }

    public static <T extends Serializable> Object deepClone(Object object) {
        try { //https://stackoverflow.com/a/61546041 - code of deep cloning object
            ByteArrayOutputStream baOs = new ByteArrayOutputStream();
            ObjectOutputStream oOs = new ObjectOutputStream(baOs);
            oOs.writeObject(object);
            ByteArrayInputStream baIs = new ByteArrayInputStream(baOs.toByteArray());
            ObjectInputStream oIs = new ObjectInputStream(baIs);
            return oIs.readObject();
        }
        catch (Exception e) {
            System.out.println("Try again, error happened during remembering state.");
            return null;
        }
    }

}
