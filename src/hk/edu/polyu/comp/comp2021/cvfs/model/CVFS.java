package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.util.*;
import java.io.*;

/**
 * Central class representing the Command Virtual File System (CVFS).
 * Handles operations such as creating files, directories, criteria, and managing virtual disk state.
 */
public class CVFS {
    private final VirtualDisk OPTIONAL=null;
    private VirtualDisk disk;
    private Map<String, Criterion> criterionMap;
    //For Bonus2
    private Stack<CVFSState> undoStack;
    private Stack<CVFSState> redoStack;

    /**
     * Initializes a new CVFS instance with no disk and default configurations.
     */
    public CVFS(){
        disk = OPTIONAL;
        setUndoRedoStacks();
        setCriterionMap();
    }

    private void setCriterionMap(){
        criterionMap = new HashMap<>();
        Criterion isDocument = new IsDocumentCriterion();
        criterionMap.put("IsDocument", isDocument);
    }

    /**
     * Creates a new virtual disk with the specified maximum size.
     *
     * @param maxSize The maximum size of the disk in bytes.
     */
    public void createDisk(int maxSize){
        if (this.disk != null) {
            System.out.println("Warning!!! If current disk isn't saved, you will lost all the data!\nAre you sure you want to continue creating new disk? Yes/No");
            Scanner scanner = new Scanner(System.in);
            while(true){
                String ans = scanner.nextLine().trim();
                if(ans.equals("Yes")){
                    break;
                }
                if(ans.equals("No")){
                    System.out.println("Operation was cancelled.");
                    return;
                }
            }
            System.out.println("Closing the current disk...");
            this.disk = OPTIONAL;
            System.gc();
        }
        this.disk = new VirtualDisk(maxSize); // Create a new disk
        setCriterionMap();
        setUndoRedoStacks();
        System.out.println("Created a new virtual disk with size " + maxSize + ".");
    }

    /**
     * Creates a new document in the current directory.
     *
     * @param name    The name of the document.
     * @param type    The type of the document (e.g., txt, java).
     * @param content The content of the document.
     * @throws StateChangeCommandFailed If there is not enough disk space.
     */
    public void createDocument(String name, String type, String content){
        ensureDiskExists();
        beforeExecution();
        Document doc= new Document(name, type, content);
        if(doc.getSize()>disk.getRemainedSize()){
            throw new StateChangeCommandFailed("There is no enough space.");
        }
        disk.getCurrentDirectory().addFile(doc);
        System.out.println("Created a new document: " + name + "." + type);
    }

    /**
     * Creates a new directory in the current directory.
     *
     * @param name The name of the directory.
     * @throws StateChangeCommandFailed If there is not enough disk space.
     */
    public void createDirectory(String name){
        ensureDiskExists();
        beforeExecution();
        Directory dir = new Directory(name);
        if(dir.getSize()>disk.getRemainedSize()){
            throw new StateChangeCommandFailed("There is no enough space.");
        }
        disk.getCurrentDirectory().addFile(dir);
        System.out.println("Created a new directory: " + name);
    }

    /**
     * Deletes a file or directory by name from the current directory.
     *
     * @param name The name of the file or directory to delete.
     * @throws StateChangeCommandFailed If it can't find a file or directory.
     */
    public void delete(String name){
        ensureDiskExists();
        beforeExecution();
        disk.getCurrentDirectory().removeFile(name);
        System.out.println("File "+name + " was deleted from working directory");
    }

    /**
     * Renames a file or directory in the current directory.
     *
     * @param oldName The current name of the file or directory.
     * @param newName The new name for the file or directory.
     * @throws StateChangeCommandFailed If it can't find a file or directory.
     */
    public void rename(String oldName, String newName){
        ensureDiskExists();
        beforeExecution();
        disk.getCurrentDirectory().renameFile(oldName, newName);
        System.out.println("Renamed file: " + oldName + " to " + newName);
    }

    /**
     * Changes the working directory.
     *
     * @param dirName The name of the directory to switch to.
     * @throws StateChangeCommandFailed If it can't find a directory.
     */
    public void changeDir(String dirName){
        ensureDiskExists();
        beforeExecution();
        disk.changeDirectory(dirName);
    }

    /**
     * Lists all files in the current directory.
     */
    public void list(){
        ensureDiskExists();
        disk.getCurrentDirectory().listFiles();
    }

    /**
     * Lists all files in the current directory matching a specific criterion.
     *
     * @param criName The name of the criterion to filter files.
     * @throws IllegalArgumentException If the criterion does not exist.
     */
    public void list(String criName){
        ensureDiskExists();
        if(!criterionMap.containsKey(criName)){
            throw new IllegalArgumentException("Criteria with this name doesn't exists.");
        }
        Criterion criterion = criterionMap.get(criName);
        disk.getCurrentDirectory().listFiles(criterion);
    }

    /**
     * Recursively lists all files in the current directory and subdirectories.
     */
    public void recursiveList(){
        ensureDiskExists();
        disk.getCurrentDirectory().recursiveListFiles();
    }

    /**
     * Recursively lists all files in the current directory and subdirectories
     * matching a specific criterion.
     *
     * @param criName The name of the criterion to filter files.
     * @throws IllegalArgumentException If the criterion does not exist.
     */
    public void recursiveList(String criName){
        ensureDiskExists();
        if(!criterionMap.containsKey(criName)){
            throw new IllegalArgumentException("Criteria with this name doesn't exists.");
        }
        Criterion criterion = criterionMap.get(criName);
        disk.getCurrentDirectory().recursiveListFiles(criterion);
    }

    /**
     * Creates a simple criterion with a specified attribute, operator, and value.
     *
     * @param criName  The name of the criterion.
     * @param attrName The attribute name (e.g., name, type, size).
     * @param op       The operator (e.g., contains, equals, >).
     * @param val      The value to compare against.
     * @throws StateChangeCommandFailed If the criterion already exists.
     */
    public void createSimpleCri(String criName,String attrName,String op,String val){
        beforeExecution();
        if(criterionMap.containsKey(criName)){
            throw new StateChangeCommandFailed("Criteria with this name already exists.");
        }
        Criterion criterion = new SimpleCriterion(criName, attrName, op, val);
        criterionMap.put(criName, criterion);
        System.out.println("Created simple criterion: " + criName);
    }

    /**
     * Creates a negation criterion that negates another existing criterion.
     *
     * @param criName    The name of the new negation criterion.
     * @param criterion1 The name of the existing criterion to negate.
     * @throws StateChangeCommandFailed If the referenced criterion does not exist.
     */
    public void createNegationCri(String criName, String criterion1){
        beforeExecution();
        if (!criterionMap.containsKey(criterion1)) {
            throw new StateChangeCommandFailed("Criterion '" + criterion1 + "' does not exist.");
        }
        Criterion criterion = new NegationCriterion(criName, criterionMap.get(criterion1));
        criterionMap.put(criName, criterion);
        System.out.println("Created negation criterion: " + criName);
    }

    /**
     * Creates a binary criterion that combines two existing criteria with a logical operator.
     * This method also added for undo/redo actions. BON2
     *
     * @param criName    The name of the new binary criterion.
     * @param criterion1 The name of the first criterion.
     * @param logOp      The logical operator ("&&" for AND, "||" for OR).
     * @param criterion2 The name of the second criterion.
     * @throws StateChangeCommandFailed If any of the referenced criteria do not exist.
     */
    public void createBinaryCri(String criName, String criterion1, String logOp, String criterion2){
        beforeExecution();
        if (!(criterionMap.containsKey(criterion1) && criterionMap.containsKey(criterion2))) {
            throw new StateChangeCommandFailed("Both criteria must exist.");
        }
        Criterion criterion = new BinaryCriterion(criName, criterionMap.get(criterion1), logOp, criterionMap.get(criterion2));
        criterionMap.put(criName, criterion);
        System.out.println("Created binary criterion: " + criName);
    }

    /**
     * Prints all criteria currently stored in the criterion map.
     */
    public void printAllCriterion(){
        for (Criterion cri : criterionMap.values()){
            System.out.println(cri);
        }
    }

    /**
     * Saves the current state of the virtual disk to a file.
     *
     * @param path             The file path to save the disk.
     * @param withAllCriteria Whether to save all criteria in the map as well.
     */
    public void saveDisk(String path, boolean withAllCriteria){
        ensureDiskExists();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(disk);
            oos.writeBoolean(withAllCriteria);
            if(withAllCriteria) oos.writeObject(criterionMap);
            System.out.println("Virtual disk saved to " + path);
        } catch (IOException e) {
            System.err.println("Error saving virtual disk: " + e.getMessage());
        }
    }

    /**
     * Loads a virtual disk from a file.
     *
     * @param path The file path to load the disk from.
     */
    public void loadDisk(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            disk = (VirtualDisk) ois.readObject();
            boolean withAllCriteria = ois.readBoolean();
            if(withAllCriteria) {
                criterionMap = (Map<String, Criterion>) ois.readObject();
            }
            else{
                setCriterionMap();
            }
            setUndoRedoStacks();
            System.out.println("Virtual disk loaded from " + path);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading virtual disk: " + e.getMessage());
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////
//    Bonus2 methods
    private void setUndoRedoStacks(){
        undoStack=new Stack<>();
        redoStack=new Stack<>();
    }

    /**
     * Performs the undo operation by reverting to the previous state of the CVFS.
     * Moves the current state to the redo stack.
     */
    public void undo(){
        if (undoStack.isEmpty()){
            System.out.println("There is nothing to undo.");
            return;
        }
        CVFSState curState = new CVFSState(disk, criterionMap);
        redoStack.push(curState);

        CVFSState prevState = undoStack.pop();
        disk= prevState.getDiskState();
        criterionMap= prevState.getCriterionMapState();
    }

    /**
     * Performs the redo operation by restoring the state from the redo stack.
     * Moves the current state to the undo stack.
     */
    public void redo(){
        if (redoStack.isEmpty()){
            System.out.println("There is nothing to redo.");
            return;
        }
        CVFSState curState = new CVFSState(disk, criterionMap);
        undoStack.push(curState);

        CVFSState nextState = redoStack.pop();
        disk= nextState.getDiskState();
        criterionMap= nextState.getCriterionMapState();
    }

    /**
     * Saves the current state of the CVFS to the undo stack
     * and clears the redo stack to maintain consistency.
     */
    public void beforeExecution(){
        saveState();
        redoStack.clear();
    }

    /**
     * Handles a failed state change command by removing the last saved state
     * from the undo stack.
     */
    public void handleStateChangeCommandFailed(){
        undoStack.pop();
    }

    /**
     * Saves the current state of the CVFS to the undo stack.
     * Ensures the undo stack does not exceed its maximum size of 5 states.
     */
    public void saveState(){
        CVFSState state = new CVFSState(disk,criterionMap);
        undoStack.push(state);
        //Setting maximum size for undo and redo no more than 5
        //Theoretically redoStack.maxSize can't be more that undoStack.maxSize
        if(undoStack.size()>5){
            undoStack.remove(0);
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
//    Other methods
    /**
     * Retrieves the current working directory as a string path.
     *
     * @return The current working directory path.
     */
    public String getWorkingDir(){
        if (disk==null) return "";
        StringBuilder res= new StringBuilder();
        Directory workingDir= disk.getCurrentDirectory();
        while (workingDir.getParent()!=null){
            res.insert(0, "/"+workingDir.getName());
            workingDir=workingDir.getParent();
        }
        res.insert(0,"$");
        return res.toString();
    }

    /**
     * Displays the remaining free space on the virtual disk in bytes.
     */
    public void showRemainedSpace(){
        ensureDiskExists();
        System.out.println("Free space: " + disk.getRemainedSize() + " bytes");
    }

    /**
     * Ensures a virtual disk is loaded before performing operations.
     *
     * @throws IllegalArgumentException If no disk is loaded.
     */
    private void ensureDiskExists() {
        if (disk == null) {
            throw new IllegalArgumentException("No disk exists. Create new or load one.");
        }
    }

    /**
     * Retrieves the current virtual disk.
     *
     * @return The current virtual disk.
     */
    public VirtualDisk getDisk() {
        return disk;
    }

    /**
     * Retrieves the map of all stored criteria.
     *
     * @return The criterion map.
     */
    public Map<String, Criterion> getCriterionMap() {
        return criterionMap;
    }
}
