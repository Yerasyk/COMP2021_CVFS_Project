package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a directory in the virtual file system.
 * A directory can contain files and subdirectories, and it tracks its parent directory for navigation.
 */
public class Directory extends File implements Serializable {
    private final List<File> files;
    private Directory parent; // Track parent directory for navigation
    private static final Directory PARENT_OF_ROOT=null;

    /**
     * Constructs a new directory with the specified name.
     * The directory starts with no files and no parent (if it's the root).
     *
     * @param name The name of the directory.
     */
    public Directory(String name) {
        super(name);
        this.files = new ArrayList<>();
        this.parent = PARENT_OF_ROOT; // Root directory has no parent
    }

    /**
     * Retrieves the list of files and subdirectories within this directory.
     *
     * @return The list of files and subdirectories.
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * Sets the parent directory of this directory.
     *
     * @param parent The parent directory.
     */
    public void setParent(Directory parent) {
        this.parent = parent;
    }

    /**
     * Retrieves the parent directory of this directory.
     *
     * @return The parent directory, or {@code null} if this is the root directory.
     */
    public Directory getParent() {
        return parent;
    }

    /**
     * Adds a file or subdirectory to this directory.
     *
     * @param file The file or subdirectory to add.
     * @throws StateChangeCommandFailed If a file with the same name already exists in this directory.
     */
    public void addFile(File file) {
        File foundFile=findFile(file);
        if (foundFile!=null) {
            throw new StateChangeCommandFailed("A file with the name '" + file.getName() + "' already exists.");
        }
        files.add(file);
        if (file instanceof Directory) {
            ((Directory) file).setParent(this); // Set parent for subdirectory
        }
    }

    /**
     * Removes a file or subdirectory by its name from this directory.
     *
     * @param fileName The name of the file or subdirectory to remove.
     * @throws StateChangeCommandFailed If no file with the specified name exists in this directory.
     */
    public void removeFile(String fileName){
        File foundFile = findFile(fileName);
        if(foundFile==null){
            throw new StateChangeCommandFailed("A file with the name '" + fileName + "' doesn't exist.");
        }
        files.remove(foundFile);
    }

    /**
     * Renames a file or subdirectory in this directory.
     *
     * @param oldFileName The current name of the file or subdirectory.
     * @param newFileName The new name for the file or subdirectory.
     * @throws StateChangeCommandFailed If no file with the specified name exists in this directory.
     */
    public void renameFile(String oldFileName, String newFileName){
        File foundFile=findFile(oldFileName);
        if(foundFile==null) {
            throw new StateChangeCommandFailed("A file with the name '" + oldFileName + "' does not exist.");
        }
        foundFile.setName(newFileName);
    }

    /**
     * Searches for a file or subdirectory in this directory.
     *
     * @param file The file or subdirectory to search for.
     * @return The file or subdirectory if found, or {@code null} if not found.
     */
    public File findFile(File file){
        for (File existingFile : files) {
            if (existingFile.getName().equals(file.getName())) {return existingFile;}
        }
        return null;
    }
    /**
     * Searches for a file or subdirectory by name in this directory.
     *
     * @param fileName The name of the file or subdirectory to search for.
     * @return The file or subdirectory if found, or {@code null} if not found.
     */
    public File findFile(String fileName){
        for (File existingFile : files) {
            if (existingFile.getName().equals(fileName)) {return existingFile;}
        }
        return null;
    }

    /**
     * Lists all files and subdirectories in this directory.
     * Also calculates and displays the total count and size of files in the directory.
     */
    public void listFiles() {
        int[] totals = {0, 0}; // Array to store total file count and size
        listFiles(this, 0, false, null, totals);
        System.out.println("\nTotal files: " + totals[0] + ",   Total Size: " + totals[1]);
    }

    /**
     * Lists all files and subdirectories in this directory that match a specified criterion.
     *
     * @param criterion The criterion to filter files and subdirectories.
     */
    public void listFiles(Criterion criterion) {
        listFiles(this, 0, false, criterion, null);
    }

    /**
     * Recursively lists all files and subdirectories in this directory and its subdirectories.
     * Also calculates and displays the total count and size of files in the directory tree.
     */
    public void recursiveListFiles() {
        int[] totals = {0, 0};
        listFiles(this, 0, true, null, totals);
        System.out.println("\nTotal files: " + totals[0] + ",   Total Size: " + totals[1]);
    }

    /**
     * Recursively lists all files and subdirectories in this directory and its subdirectories
     * that match a specified criterion.
     *
     * @param criterion The criterion to filter files and subdirectories.
     */
    public void recursiveListFiles(Criterion criterion) {
        listFiles(this, 0, true, criterion, null);
    }

    // Unified method for listing and searching (flat and recursive)
    /**
     * Unified method for listing files and subdirectories, supporting both flat and recursive views.
     * Optionally filters files by a specified criterion and calculates total file count and size.
     *
     * @param directory The directory to list files from.
     * @param level     The current depth in the directory hierarchy.
     * @param recursive Whether to include subdirectories recursively.
     * @param criterion The criterion to filter files and subdirectories, or {@code null} to include all.
     * @param totals    An array to store total file count and size, or {@code null} if not needed.
     */
    private void listFiles(Directory directory, int level, boolean recursive, Criterion criterion, int[] totals) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < level; i++) {
            temp.append("  ");
        }
        String indent = temp.toString();

        for (File file : directory.getFiles()) {
            boolean matches = criterion == null || criterion.matches(file); // Check criterion

            if (matches) {
                System.out.println(indent + file);
                if(totals!=null){
                    totals[0]++;
                    if(level==0) {
                        totals[1] += file.getSize();
                    }
                }
            }

            if (recursive && file instanceof Directory) {
                listFiles((Directory) file, level + 1, true, criterion, totals);
            }
        }
    }

    /**
     * Calculates and retrieves the total size of the directory,
     * including the sizes of all files and subdirectories it contains.
     *
     * @return The total size of the directory in bytes.
     */
    @Override
    public int getSize() {
        int totalSize = DEFAULT_SIZE; // Base size of a directory
        for (File file : files) {
            totalSize += file.getSize();
        }
        return totalSize;
    }

    /**
     * Returns a string representation of the directory, including its name and size.
     *
     * @return A string representing the directory.
     */
    @Override
    public String toString() {
        return getName() + ", Size: " + getSize();
    }
}
