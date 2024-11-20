package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;
/**
 * Represents a virtual disk in the file system.
 * The virtual disk manages file and directory operations, including navigation and storage limitations.
 */
public class VirtualDisk implements Serializable {
    private final int maxSize;
    private final Directory root;
    private Directory currentDirectory;

    /**
     * Constructs a new virtual disk with the specified maximum size.
     * Initializes the root directory and sets it as the current directory.
     *
     * @param size The maximum size of the disk in bytes.
     */
    public VirtualDisk(int size) {
        this.maxSize = size;
        this.root = new Directory("root");
        this.currentDirectory = root;
    }

    /**
     * Retrieves the remaining free space on the virtual disk.
     *
     * @return The remaining size of the disk in bytes.
     */
    public int getRemainedSize() {
        return maxSize - root.getSize();
    }

    /**
     * Retrieves the current working directory of the virtual disk.
     *
     * @return The current directory.
     */
    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * Changes the current working directory to the specified directory.
     * If the directory name is "..", it navigates to the parent directory unless already at the root.
     *
     * @param dirName The name of the directory to navigate to.
     * @throws StateChangeCommandFailed If navigating to a non-existent directory or already at the root.
     */
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
