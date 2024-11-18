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
            System.out.print(cvfs.getWorkingDir()+"> ");
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
                        if (docParams.length != 3) {
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
                        if (renameParams.length != 2) {
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

                    //REQ9
                    case "newSimpleCri":
                        String[] criParams = params.split("\\s+", 4);
                        if (criParams.length != 4) {
                            throw new IllegalArgumentException("Usage: newSimpleCri <criName> <attrName> <op> <val>");
                        }
                        cvfs.createSimpleCri(criParams[0],criParams[1],criParams[2],criParams[3]);
                        break;
                    //REQ10 is Built-in inside CVFS
                    //REQ11
                    case "newNegation":
                        String[] negateParams = params.split("\\s+", 2);
                        if (negateParams.length != 2) {
                            throw new IllegalArgumentException("Usage: newNegation <criName1> <criName2>");
                        }
                        cvfs.createNegationCri(negateParams[0],negateParams[1]);
                        break;
                    case "newBinaryCri":
                        String[] binaryParams = params.split("\\s+", 4);
                        if (binaryParams.length != 4) {
                            throw new IllegalArgumentException("Usage: newBinary <criName1> <criName2> <logOp> <criName3>");
                        }
                        cvfs.createBinaryCri(binaryParams[0],binaryParams[1],binaryParams[2],binaryParams[3]);
                        break;
                    //REQ12
                    case "printAllCriteria":
                        cvfs.printAllCriterion();
                        break;
                    //REQ13
                    case "search":
                        if (params.isEmpty()) {
                            throw new IllegalArgumentException("Usage: search <criName>");
                        }
                        cvfs.list(params);
                        break;
                    //REQ14
                    case "rSearch":
                        if (params.isEmpty()) {
                            throw new IllegalArgumentException("Usage: rSearch <criName>");
                        }
                        cvfs.recursiveList(params);
                        break;
                    //REQ15
                    case"save":
                        if (params.isEmpty()) {
                            throw new IllegalArgumentException("Usage: save <path>");
                        }
                        cvfs.saveDisk(params);
                        break;
                    //REQ16
                    case"load":
                        if (params.isEmpty()) {
                            throw new IllegalArgumentException("Usage: load <path>");
                        }
                        cvfs.loadDisk(params);
                        break;
                    //REQ17
                    case "quit":
                        System.out.println("Exiting CVFS. Goodbye!");
                        return;
//////////////////////////////////////////////////////////////////////////
                    case "rSpace":
                        cvfs.showRemainedSpace();
                        break;

                    case "help":
                        printHelp();
                        break;


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
        System.out.println("\nAvailable Commands:");
        System.out.println("  newDisk <size>            - Create a new virtual disk with the specified size.");
        System.out.println("  newDoc <name> <type> <content> - Create a new document in the current directory.");
        System.out.println("  newDir <name>             - Create a new directory in the current directory.");
        System.out.println("  delete <name>             - Delete a file or directory by name.");
        System.out.println("  rename <oldName> <newName> - Rename an existing file or directory.");
        System.out.println("  changeDir <dirName>       - Change the working directory to the specified directory.");
        System.out.println("  list                      - List all files and directories in the current directory.");
        System.out.println("  rList                     - Recursively list all files and directories.");
        System.out.println("  rSpace                    - Show the remaining space in the virtual disk.");
        System.out.println("  newSimpleCri <criName> <attrName> <op> <val> - Create a simple search criterion.");
        System.out.println("  newNegation <criName1> <criName2> - Create a negation criterion.");
        System.out.println("  newBinaryCri <criName1> <criName2> <logicOp> - Create a binary (AND/OR) criterion.");
        System.out.println("  printAllCriteria          - Print all defined criteria.");
        System.out.println("  search <criName>          - Search for files in the current directory based on a criterion.");
        System.out.println("  rSearch <criName>         - Recursively search for files based on a criterion.");
        System.out.println("  save <path>               - Save the virtual disk to a file.");
        System.out.println("  load <path>               - Load the virtual disk from a file.");
        System.out.println("  quit                      - Exit the application.");
        System.out.println("  help                      - Display this help message.\n");
    }
}
