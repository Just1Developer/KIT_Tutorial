package edu.kit.kastel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utility class to read file contents.
 * @author uwwfh
 */
public class FileReader {

    /**
     * Reads a file and returns the contents, line by line, in a String array.
     * Empty lines are empty String entries in the array.
     * <br/>
     * The filepath can be relative, but the File constructor must find it
     * using new File(filepath);
     * <br/>
     * Returns an empty array if the file is not found. Prints an error to the
     * Default Error Stream {@code System.err}.
     *
     * @param filepath The filepath. May be relative.
     * @return An array of the lines. Not null.
     */
    public static String[] readFile(String filepath) {

        // Find the file
        File file = new File(filepath);
        if (!file.exists()) {
            System.err.printf("Unable to find File %s%n", filepath);
            return new String[0];
        }

        // Open a scanner
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            System.err.printf("Failed to create FileInputStream for File %s%n", filepath);
            return new String[0];
        }

        // Read file and save Strings in list
        List<String> lines = new ArrayList<>();
        for (String s; scanner.hasNext() && (s = scanner.nextLine()) != null;) {
            lines.add(s);
        }

        // Convert to Array. Returns array because we haven't covered lists yet.
        return lines.toArray(new String[0]);
    }

}
