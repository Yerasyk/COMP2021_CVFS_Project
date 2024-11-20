package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.Scanner;
import java.util.Stack;

public class CVFS {
    private VirtualDisk disk;
    private Map<String, Criterion> criterionMap;
    //For Bonus2
    private Stack<CVFSState> undoStack;
    private Stack<CVFSState> redoStack;

    public CVFS(){
        this.disk = null;
        setUndoRedoStacks();
        setCriterionMap();
    }

    private void setCriterionMap(){
        criterionMap = new HashMap<>();
        Criterion isDocument = new IsDocumentCriterion();
        criterionMap.put("IsDocument", isDocument);
    }

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
            this.disk = null;
            System.gc();
        }
        this.disk = new VirtualDisk(maxSize); // Create a new disk
        setCriterionMap();
        setUndoRedoStacks();
        System.out.println("Created a new virtual disk with size " + maxSize + ".");
    }

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

    public void delete(String name){
        ensureDiskExists();
        beforeExecution();
        disk.getCurrentDirectory().removeFile(name);
        System.out.println("File "+name + " was deleted from working directory");
    }

    public void rename(String oldName, String newName){
        ensureDiskExists();
        beforeExecution();
        disk.getCurrentDirectory().renameFile(oldName, newName);
        System.out.println("Renamed file: " + oldName + " to " + newName);
    }

    public void changeDir(String dirName){
        ensureDiskExists();
        beforeExecution();
        disk.changeDirectory(dirName);
    }

    public void list(){
        ensureDiskExists();
        disk.getCurrentDirectory().listFiles();
    }
    //Search is the overloaded method of list
    public void list(String criName){
        ensureDiskExists();
        if(!criterionMap.containsKey(criName)){
            throw new IllegalArgumentException("Criteria with this name doesn't exists.");
        }
        Criterion criterion = criterionMap.get(criName);
        disk.getCurrentDirectory().listFiles(criterion);
    }

    public void recursiveList(){
        ensureDiskExists();
        disk.getCurrentDirectory().recursiveListFiles();
    }
    //Recursive search is the overloaded method of recursiveList
    public void recursiveList(String criName){
        ensureDiskExists();
        if(!criterionMap.containsKey(criName)){
            throw new IllegalArgumentException("Criteria with this name doesn't exists.");
        }
        Criterion criterion = criterionMap.get(criName);
        disk.getCurrentDirectory().recursiveListFiles(criterion);
    }

    public void createSimpleCri(String criName,String attrName,String op,String val){
        beforeExecution();
        if(criterionMap.containsKey(criName)){
            throw new StateChangeCommandFailed("Criteria with this name already exists.");
        }
        Criterion criterion = new SimpleCriterion(criName, attrName, op, val);
        criterionMap.put(criName, criterion);
        System.out.println("Created simple criterion: " + criName);
    }

    public void createNegationCri(String criName, String criterion1){
        beforeExecution();
        if (!criterionMap.containsKey(criterion1)) {
            throw new StateChangeCommandFailed("Criterion '" + criterion1 + "' does not exist.");
        }
        Criterion criterion = new NegationCriterion(criName, criterionMap.get(criterion1));
        criterionMap.put(criName, criterion);
        System.out.println("Created negation criterion: " + criName);
    }

    //Also added to undo/redo
    public void createBinaryCri(String criName, String criterion1, String logOp, String criterion2){
        beforeExecution();
        if (!(criterionMap.containsKey(criterion1) && criterionMap.containsKey(criterion2))) {
            throw new StateChangeCommandFailed("Both criteria must exist.");
        }
        Criterion criterion = new BinaryCriterion(criName, criterionMap.get(criterion1), logOp, criterionMap.get(criterion2));
        criterionMap.put(criName, criterion);
        System.out.println("Created binary criterion: " + criName);
    }

    public void printAllCriterion(){
        for (Criterion cri : criterionMap.values()){
            System.out.println(cri);
        }
    }

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
    public void beforeExecution(){
        saveState();
        redoStack.clear();
    }
    public void handleStateChangeCommandFailed(){
        undoStack.pop();
    }
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

    public void showRemainedSpace(){
        ensureDiskExists();
        System.out.println("Free space: " + disk.getRemainedSize() + " bytes");
    }

    private void ensureDiskExists() {
        if (disk == null) {
            throw new IllegalArgumentException("No disk exists. Create new or load one.");
        }
    }

    public VirtualDisk getDisk() {
        return disk;
    }

    public Map<String, Criterion> getCriterionMap() {
        return criterionMap;
    }
}
