package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the state of the CVFS, including the virtual disk and the criterion map.
 * This class provides functionality to create deep copies of the disk and criteria map,
 * ensuring that undo and redo operations do not interfere with the current state.
 */
public class CVFSState{
    private final VirtualDisk diskState;
    private final Map<String, Criterion> criterionMapState;

    /**
     * Constructs a new CVFSState with the specified virtual disk and criterion map.
     * Performs a deep clone of both the disk and the map to ensure immutability of the saved state.
     *
     * @param disk         The current state of the virtual disk.
     * @param criterionMap The current state of the criterion map.
     */
    public CVFSState(VirtualDisk disk, Map<String, Criterion> criterionMap){
        diskState= (VirtualDisk) deepClone(disk);
        criterionMapState=new HashMap<>(criterionMap);
    }

    /**
     * Retrieves the saved state of the virtual disk.
     *
     * @return The virtual disk at the time this state was saved.
     */
    public VirtualDisk getDiskState(){
        return diskState;
    }

    /**
     * Retrieves the saved state of the criterion map.
     *
     * @return The criterion map at the time this state was saved.
     */
    public Map<String, Criterion> getCriterionMapState(){
        return criterionMapState;
    }

    /**
     * Performs a deep clone of a given serializable object.
     * Uses serialization to create a byte-by-byte copy of the object, ensuring that
     * all nested objects are also cloned.
     *
     * @param object The object to be deep cloned.
     * @param <T>    The type of the object being cloned, which must implement {@link Serializable}.
     * @return A deep copy of the provided object, or {@code null} if an error occurs during cloning.
     */
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
