package hk.edu.polyu.comp.comp2021.cvfs;

import hk.edu.polyu.comp.comp2021.cvfs.model.CVFS;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        CVFS cvfs = new CVFS();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n#####################################################################");
        System.out.println("       Welcome to the Command-Line Virtual File System (CVFS).");
        System.out.println("Type 'help' to see the list of commands or 'exit' to quit the application.");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim();
            String[] parts = command.split("\\s+", 2);
            String cmd = parts[0];
            String params = parts.length > 1 ? parts[1] : "";

            try {
                switch (cmd) {
                    //REQ1
                    case "newDisk":
                        cvfs.createDisk(Integer.parseInt(params));
                        break;

                    //REQ2
                    case "newDoc":
                        String[] docParams = params.split("\\s+", 3);
                        if (docParams.length < 3) {
                            throw new IllegalArgumentException("Usage: newDoc <name> <type> <content>");
                        }
                        cvfs.createDocument(docParams[0], docParams[1], docParams[2]);
                        break;

                    //REQ3
                    case "newDir":
                        if (params.isEmpty()) {
                            throw new IllegalArgumentException("Usage: newDir <name>");
                        }
                        cvfs.createDirectory(params);
                        break;

                    //REQ4
                    case "delete":
                        if (params.isEmpty()) {
                            throw new IllegalArgumentException("Usage: delete <name>");
                        }
                        cvfs.delete(params);
                        break;

                    //REQ5
                    case "rename":
                        String[] renameParams = params.split("\\s+");
                        if (renameParams.length < 2) {
                            throw new IllegalArgumentException("Usage: rename <oldName> <newName>");
                        }
                        cvfs.rename(renameParams[0], renameParams[1]);
                        break;

                    //REQ6
                    case "changeDir":
                        if (params.isEmpty()) {
                            throw new IllegalArgumentException("Usage: changeDir <dirName>");
                        }
                        cvfs.changeDir(params);
                        break;

                    //REQ7
                    case "list":
                        cvfs.list();
                        break;

                    //REQ8
                    case "rList":
                        cvfs.recursiveList();
                        break;

//////////////////////////////////////////////////////////////////////////
                    case "rSpace":
                        cvfs.showRemainedSpace();
                        break;

                    case "help":
                        printHelp();
                        break;

                    case "exit":
                        System.out.println("Exiting CVFS. Goodbye!");
                        return;

                    default:
                        System.out.println("Unknown command. Type 'help' to see available commands.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format. Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void printHelp() {
        System.out.println("Available Commands:");
        System.out.println("  newDisk <size>       - Create a new virtual disk with the specified size.");
        System.out.println("  newDoc <name> <type> <content> - Create a new document in the current directory.");
        System.out.println("  newDir <name>        - Create a new directory in the current directory.");
        System.out.println("  delete <name>        - Delete a file or directory by name.");
        System.out.println("  rename <oldName> <newName> - Rename an existing file or directory.");
        System.out.println("  changeDir <dirName>  - Change the working directory to the specified directory.");
        System.out.println("  list                 - List all files and directories in the current directory.");
        System.out.println("  rList                - Recursively list all files and directories.");
        System.out.println("  rSpace               - Shows how much space left in the disk.");
        System.out.println("  help                 - Display this help message.");
        System.out.println("  exit                 - Exit the application.");
    }
}
