package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Directory extends File implements Serializable {
    private final List<File> files;
    private Directory parent; // Track parent directory for navigation

    public Directory(String name) {
        super(name);
        this.files = new ArrayList<>();
        this.parent = null; // Root directory has no parent
    }

    public List<File> getFiles() {
        return files;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public Directory getParent() {
        return parent;
    }

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

    public void removeFile(String fileName){
        File foundFile = findFile(fileName);
        if(foundFile==null){
            throw new StateChangeCommandFailed("A file with the name '" + fileName + "' doesn't exist.");
        }
        files.remove(foundFile);
    }

    public void renameFile(String oldFileName, String newFileName){
        File foundFile=findFile(oldFileName);
        if(foundFile==null) {
            throw new StateChangeCommandFailed("A file with the name '" + oldFileName + "' does not exist.");
        }
        foundFile.setName(newFileName);
    }
    public File findFile(File file){
        for (File existingFile : files) {
            if (existingFile.getName().equals(file.getName())) {return existingFile;}
        }
        return null;
    }
    public File findFile(String fileName){
        for (File existingFile : files) {
            if (existingFile.getName().equals(fileName)) {return existingFile;}
        }
        return null;
    }

    public void listFiles() {
        int[] totals = {0, 0}; // Array to store total file count and size
        listFiles(this, 0, false, null, totals);
        System.out.println("\nTotal files: " + totals[0] + ",   Total Size: " + totals[1]);
    }

    public void listFiles(Criterion criterion) {
        listFiles(this, 0, false, criterion, null);
    }

    public void recursiveListFiles() {
        int[] totals = {0, 0};
        listFiles(this, 0, true, null, totals);
        System.out.println("\nTotal files: " + totals[0] + ",   Total Size: " + totals[1]);
    }

    public void recursiveListFiles(Criterion criterion) {
        listFiles(this, 0, true, criterion, null);
    }

    // Unified method for listing and searching (flat and recursive)
    private void listFiles(Directory directory, int level, boolean recursive, Criterion criterion, int[] totals) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < level; i++) {
            temp.append("  ");
        }
        String indent = temp.toString();

        for (File file : directory.getFiles()) {
            boolean matches = criterion == null || criterion.matches(file); // Check criterion

            if (matches) {
                if (file instanceof Document) {
                    System.out.println(indent + file.getName() + "." + ((Document) file).getType() + ", Size: " + file.getSize());
                } else if (file instanceof Directory) {
                    System.out.println(indent + file.getName() + ", Size: " + file.getSize());
                }
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

    @Override
    public int getSize() {
        int totalSize = 40; // Base size of a directory
        for (File file : files) {
            totalSize += file.getSize();
        }
        return totalSize;
    }
}
