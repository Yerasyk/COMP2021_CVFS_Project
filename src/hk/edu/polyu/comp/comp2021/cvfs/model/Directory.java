package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.util.ArrayList;
import java.util.List;

public class Directory extends File {
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

    public void addFile(File file) throws IllegalArgumentException {
        for (File existingFile : files) {
            if (existingFile.getName().equals(file.getName())) {
                throw new IllegalArgumentException("A file with the name '" + file.getName() + "' already exists.");
            }
        }
        files.add(file);
        if (file instanceof Directory) {
            ((Directory) file).setParent(this); // Set parent for subdirectory
        }
    }

    public void removeFile(String fileName) throws IllegalArgumentException {
        File fileToRemove = null;
        for (File file : files) {
            if (file.getName().equals(fileName)) {
                fileToRemove = file;
                break;
            }
        }
        if (fileToRemove != null) {
            files.remove(fileToRemove);
        } else {
            throw new IllegalArgumentException("A file with the name '" + fileName + "' doesn't exist.");
        }
    }

    public void renameFile(String oldFileName, String newFileName) throws IllegalArgumentException {
        for (File file : files) {
            if (file.getName().equals(oldFileName)) {
                file.setName(newFileName);
                return;
            }
        }
        throw new IllegalArgumentException("A file with the name '" + oldFileName + "' does not exist.");
    }

    @Override
    public int getSize() {
        int totalSize = 40; // Base size of a directory
        for (File file : files) {
            totalSize += file.getSize(); // Add size of each contained file/directory
        }
        return totalSize;
    }
}
