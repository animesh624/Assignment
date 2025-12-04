package org.example.assignment2.data;

import lombok.NonNull;
import org.example.assignment2.model.SalesRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class responsible for reading sales data from a CSV file.
 * This class loads the file, skips the header line, and converts each
 * remaining line into a {@link SalesRecord}. Any malformed lines are
 * skipped with an error message printed to the console.
 */
public class SalesDataReader {

    /**
     * Reads and parses a CSV file into a List of SalesRecord objects.
     *
     * @param filePath path of the CSV file to read
     * @return list of successfully parsed SalesRecord objects
     * @throws IOException if file read operation fails
     */
    public static List<SalesRecord> readData(@NonNull final String filePath) throws IOException {
        try (var lines = Files.lines(Paths.get(filePath))) {
            return lines
                    .skip(1)
                    .map(line -> {
                        try {
                            return SalesRecord.fromCsvLine(line);
                        } catch (Exception e) {
                            System.err.println("Skipping bad record: " + line + ". Error: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(record -> record != null)
                    .collect(Collectors.toList());
        }
    }
}
