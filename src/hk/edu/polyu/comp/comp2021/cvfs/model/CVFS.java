package hk.edu.polyu.comp.comp2021.cvfs.model;

public class CVFS {
    private VirtualDisk disk;

    public CVFS(){
        this.disk= null;
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
        disk.listFiles();
    }

    public void recursiveList(){
        ensureDiskExists();
        disk.recursiveListFiles();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
//    Other methods
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
