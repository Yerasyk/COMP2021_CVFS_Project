package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class CVFS {
    private VirtualDisk disk;
    private final Map<String, Criterion> criterionMap;

    public CVFS(){
        this.disk = null;
        criterionMap = new HashMap<>();
        Criterion isDocument = new IsDocumentCriterion();
        criterionMap.put("IsDocumentCriterion", isDocument);
    }

    public void createDisk(int maxSize){
        if (this.disk != null) {
            System.out.println("Closing the current disk...");
            this.disk = null;
            System.gc();
        }
        this.disk = new VirtualDisk(maxSize); // Create a new disk
        System.out.println("Created a new virtual disk with size " + maxSize + ".");
    }

    public void createDocument(String name, String type, String content) throws IllegalArgumentException {
        ensureDiskExists();
        Document doc= new Document(name, type, content);
        if(doc.getSize()>disk.getRemainedSize()){
            throw new IllegalArgumentException("There is no enough space.");
        }
        disk.getCurrentDirectory().addFile(doc);
        System.out.println("Created a new document: " + name + "." + type);
    }

    public void createDirectory(String name){
        ensureDiskExists();
        Directory dir = new Directory(name);
        if(dir.getSize()>disk.getRemainedSize()){
            throw new IllegalArgumentException("There is no enough space.");
        }
        disk.getCurrentDirectory().addFile(dir);
        System.out.println("Created a new directory: " + name);
    }

    public void delete(String name){
        ensureDiskExists();
        disk.getCurrentDirectory().removeFile(name);
        System.out.println("File "+name + " was deleted from working directory");
    }

    public void rename(String oldName, String newName){
        ensureDiskExists();
        disk.getCurrentDirectory().renameFile(oldName, newName);
        System.out.println("Renamed file: " + oldName + " to " + newName);
    }

    public void changeDir(String dirName){
        ensureDiskExists();
        disk.changeDirectory(dirName);
    }

    public void list(){
        ensureDiskExists();
        disk.getCurrentDirectory().listFiles();
    }
    //Search is the overloaded method of list
    public void list(String criName){
        ensureDiskExists();
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
        Criterion criterion = criterionMap.get(criName);
        disk.getCurrentDirectory().recursiveListFiles(criterion);
    }

    public void createSimpleCri(String criName,String attrName,String op,String val){
        if(criterionMap.containsKey(criName)){
            throw new IllegalArgumentException("Criteria with this name already exists.");
        }
        Criterion criterion = new SimpleCriterion(criName, attrName, op, val);
        criterionMap.put(criName, criterion);
        System.out.println("Created simple criterion: " + criName);
    }

    public void createNegationCri(String criName, String criterion1){
        if (!criterionMap.containsKey(criterion1)) {
            throw new IllegalArgumentException("Criterion '" + criterion1 + "' does not exist.");
        }
        Criterion criterion = new NegationCriterion(criName, criterionMap.get(criterion1));
        criterionMap.put(criName, criterion);
        System.out.println("Created negation criterion: " + criName);
    }

    public void createBinaryCri(String criName, String criterion1, String logOp, String criterion2){
        if (!(criterionMap.containsKey(criterion1) && criterionMap.containsKey(criterion2))) {
            throw new IllegalArgumentException("Both criteria must exist.");
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

    public void saveDisk(String path){
        ensureDiskExists();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(disk);
            System.out.println("Virtual disk saved to " + path);
        } catch (IOException e) {
            System.err.println("Error saving virtual disk: " + e.getMessage());
        }
    }

    public void loadDisk(String path) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            disk = (VirtualDisk) in.readObject();
            System.out.println("Virtual disk loaded from " + path);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading virtual disk: " + e.getMessage());
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
}
