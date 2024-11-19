package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;

public class VirtualDisk implements Serializable {
    private final int maxSize;
    private final Directory root;
    private Directory currentDirectory;

    public VirtualDisk(int size) {
        this.maxSize = size;
        this.root = new Directory("root");
        this.currentDirectory = root;
    }

    public int getRemainedSize() {
        return maxSize - root.getSize();
    }

    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    public void changeDirectory(String dirName){
        if (dirName.equals("..")) {
            if (currentDirectory != root) {
                currentDirectory = currentDirectory.getParent();
            } else {
                throw new StateChangeCommandFailed("Already at root directory.");
            }
        } else {
            for (File file : currentDirectory.getFiles()) {
                if (file instanceof Directory && file.getName().equals(dirName)) {
                    currentDirectory = (Directory) file;
                    return;
                }
            }
            throw new StateChangeCommandFailed("Directory with the name '" + dirName + "' does not exist.");
        }
    }
}
