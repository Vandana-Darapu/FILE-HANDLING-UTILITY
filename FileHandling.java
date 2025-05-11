import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {

    /**
     * Reads the content of a text file.
     *
     * @param filePath The path to the file to be read.
     * @return The content of the file as a String, or null if an error occurs.
     */
    public String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) { //Use try-with-resources to auto-close.
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator()); //Preserve line breaks
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null; // Handle the error, consider throwing exception in a real application.
        }
        return content.toString();
    }

    /**
     * Writes text to a file.  If the file exists, it overwrites it.
     *
     * @param filePath The path to the file to write to.
     * @param text     The text to write to the file.
     * @return true if the write was successful, false otherwise.
     */
    public boolean writeFile(String filePath, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) { //Use try-with-resources
            writer.write(text);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Appends text to an existing file.  If the file does not exist, it creates it.
     * @param filePath The path to the file to append to.
     * @param text The text to append.
     * @return true if append was successful, false otherwise.
     */
    public boolean appendToFile(String filePath, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) { // The 'true' parameter enables append mode.
            writer.write(text);
            return true;
        } catch (IOException e) {
            System.err.println("Error appending to file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Modifies a specific line in a file.
     *
     * @param filePath    The path to the file to modify.
     * @param lineNumber  The line number to modify (1-based).
     * @param newText     The new text for the specified line.
     * @return true if the modification was successful, false otherwise.
     */
    public boolean modifyFileLine(String filePath, int lineNumber, String newText) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath)); // Read all lines.
            if (lineNumber > 0 && lineNumber <= lines.size()) {
                lines.set(lineNumber - 1, newText); // Replace the line (adjust for 0-based indexing).
                Files.write(Paths.get(filePath), lines); // Write the modified content back.
                return true;
            } else {
                System.err.println("Invalid line number: " + lineNumber);
                return false;
            }
        } catch (IOException e) {
            System.err.println("Error modifying file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a file.
     * @param filePath The path to the file to delete
     * @return true if the file was deleted successfully, false otherwise.
     */
    public boolean deleteFile(String filePath) {
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                return true;
            } else {
                System.err.println("Failed to delete file: " + filePath);
                return false;
            }
        } else {
            System.err.println("File does not exist: " + filePath);
            return false;
        }
    }

    /**
     * Creates a new directory.
     * @param dirPath The path to the directory to create.
     * @return true if the directory was created successfully, false otherwise.
     */
    public boolean createDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (dir.mkdirs()) { // Use mkdirs() to create parent directories if needed.
                return true;
            } else {
                System.err.println("Failed to create directory: " + dirPath);
                return false;
            }
        } else {
            System.err.println("Directory already exists: " + dirPath);
            return false;
        }
    }

     /**
     * Lists files and directories within a specified directory.
     *
     * @param dirPath The path to the directory to list.
     */
    public void listFilesAndDirectories(String dirPath) {
        File directory = new File(dirPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] filesAndDirs = directory.listFiles(); // Get all files and directories.

            if (filesAndDirs != null) {
                System.out.println("Contents of directory: " + dirPath);
                for (File fileOrDir : filesAndDirs) {
                    System.out.println((fileOrDir.isDirectory() ? "[D] " : "[F] ") + fileOrDir.getName());
                }
            } else {
                System.err.println("Error: Could not list files and directories or directory is empty.");
            }
        } else {
            System.err.println("Error: Directory does not exist or is not a directory: " + dirPath);
        }
    }

    public static void main(String[] args) {
        FileHandler handler = new FileHandler();
        String testFilePath = "test.txt";  //Using a constant for file path.
        String testDirectoryPath = "test_dir";

        // 1. Write to a file
        if (handler.writeFile(testFilePath, "Hello, this is a test file.\n")) {
            System.out.println("File write successful.");
        } else {
            System.out.println("File write failed.");
        }

        // 2. Read from a file
        String fileContent = handler.readFile(testFilePath);
        if (fileContent != null) {
            System.out.println("\nFile content:\n" + fileContent);
        }

        // 3. Append to a file
        if (handler.appendToFile(testFilePath, "This is appended text.\n")) {
            System.out.println("File append successful.");
        } else {
            System.out.println("File append failed.");
        }

        // 4. Read after append
        fileContent = handler.readFile(testFilePath);
        if (fileContent != null) {
            System.out.println("\nFile content after append:\n" + fileContent);
        }

        // 5. Modify a line
        if (handler.modifyFileLine(testFilePath, 1, "Modified first line.")) {
            System.out.println("File modify successful.");
        } else {
            System.out.println("File modify failed.");
        }
        // 6. Read after modify
        fileContent = handler.readFile(testFilePath);
         if (fileContent != null) {
            System.out.println("\nFile content after modify:\n" + fileContent);
        }

        // 7. Delete the file
        if (handler.deleteFile(testFilePath)) {
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("File deletion failed.");
        }

        // 8. Create a directory
        if(handler.createDirectory(testDirectoryPath)) {
            System.out.println("Directory created successfully");
        } else {
            System.out.println("Directory creation failed.");
        }

        // 9. List files and directories
        handler.listFilesAndDirectories(testDirectoryPath);

        // 10. Delete directory
        if(handler.deleteFile(testDirectoryPath)) {
            System.out.println("Directory deleted successfully.");
        } else {
            System.out.println("Directory deletion failed or did not exist.");
        }
    }
}

