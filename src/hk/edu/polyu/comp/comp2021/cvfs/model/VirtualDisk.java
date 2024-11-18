package hk.edu.polyu.comp.comp2021.cvfs.model;

public class VirtualDisk {
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

    public int getMaxSize() {
        return this.maxSize;
    }

    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    public void changeDirectory(String dirName) throws IllegalArgumentException {
        if (dirName.equals("..")) {
            if (currentDirectory != root) {
                currentDirectory = currentDirectory.getParent();
            } else {
                throw new IllegalArgumentException("Already at root directory.");
            }
        } else {
            for (File file : currentDirectory.getFiles()) {
                if (file instanceof Directory && file.getName().equals(dirName)) {
                    currentDirectory = (Directory) file;
                    return;
                }
            }
            throw new IllegalArgumentException("Directory with the name '" + dirName + "' does not exist.");
        }
    }

    public void listFiles() {
        int totalFiles = 0;
        int totalSize = 0;

        for (File file : currentDirectory.getFiles()) {
            if (file instanceof Document) {
                System.out.println( file.getName() + "." + ((Document)file).getType() + ", Size: " + file.getSize());
            } else if (file instanceof Directory) {
                System.out.println( file.getName() + ", Size: " + file.getSize());
            }
            totalFiles++;
            totalSize += file.getSize();
        }

        System.out.println("\nTotal Files: " + totalFiles);
        System.out.println("Total Size: " + totalSize);
    }

    public void recursiveListFiles() {
        int[] totals = {0, 0}; // Array to store total file count and size
        recursiveList(currentDirectory, 0, totals);

        System.out.println("\nTotal Files: " + totals[0]);
        System.out.println("Total Size: " + totals[1]);
    }

    private void recursiveList(Directory directory, int level, int[] totals) {
        for (File file : directory.getFiles()) {
            StringBuilder indent= new StringBuilder();
            for(int i=0;i<level;i++){
                indent.append("  ");
            }

            if (file instanceof Document) {
                System.out.println(indent + file.getName() + "." + ((Document)file).getType() + ", Size: " + file.getSize());
            } else if (file instanceof Directory) {
                System.out.println(indent + file.getName() + ", Size: " + file.getSize());
                recursiveList((Directory) file, level + 1, totals); // Recurse into subdirectory
            }

            totals[0]++;
            totals[1] += file.getSize();
        }
    }
}
