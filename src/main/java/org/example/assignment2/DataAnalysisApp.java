package org.example.assignment2;

import org.example.assignment2.analyzer.SalesAnalyzer;
import org.example.assignment2.data.SalesDataReader;
import org.example.assignment2.model.SalesRecord;

import java.io.IOException;
import java.util.List;

/**
 * The DataAnalysisApp class acts as the main entry point for the application.
 * This class demonstrates end-to-end flow: data loading → analysis → reporting.
 */
public class DataAnalysisApp {

    /**
     * Static sample CSV content used to create a temporary file for demonstration.
     */
    private static final String CSV_CONTENT =
            "TransactionID,ProductCategory,Region,Quantity,Price\n" +
                    "T1001,Electronics,North,2,1200.00\n" +
                    "T1002,Apparel,South,10,25.50\n" +
                    "T1003,Electronics,East,1,2500.00\n" +
                    "T1004,Software,North,5,99.99\n" +
                    "T1005,Apparel,South,12,30.00\n" +
                    "T1006,Electronics,North,5,500.00\n" +
                    "T1007,Software,East,3,150.00\n" +
                    "T1008,Apparel,West,20,15.00\n" +
                    "T1009,Electronics,East,1,100.00\n" +
                    "T1010,Apparel,North,8,45.00";

    /**
     * The path of the dummy CSV file created at runtime.
     */
    private static final String FILE_PATH = "sales_data.csv";

    /**
     * Main method that triggers the full data analysis workflow.
     */
    public static void main(String[] args) {
        // Step 1: Create the dummy CSV file so the program can read it.
        createDummyCsvFile(FILE_PATH, CSV_CONTENT);

        try {
            // Step 2: Read sales data from CSV
            List<SalesRecord> records = SalesDataReader.readData(FILE_PATH);
            System.out.println("Data Loaded (" + records.size() + " Records)");

            // Step 3: Initialize analyzer with loaded data
            SalesAnalyzer analyzer = new SalesAnalyzer(records);

            // Analysis 1: Total sales grouped by category
            System.out.println("\n1. Total Sales by Category");
            analyzer.analyzeTotalSalesByCategory().forEach((cat, sales) ->
                    System.out.printf("  %-12s: $%.2f\n", cat, sales)
            );

            // Analysis 2: Average sales grouped by region
            System.out.println("\n2. Average Sales per Region");
            analyzer.analyzeAverageSalesPerRegion().forEach((reg, avg) ->
                    System.out.printf("  %-5s: $%.2f\n", reg, avg)
            );

            // Analysis 3: Identify the category with highest quantity sold
            System.out.println("\n3. Top Selling Category (by Quantity)");
            analyzer.findTopSellingCategoryByQuantity().ifPresentOrElse(
                    entry -> System.out.printf("  Category: %s | Total Quantity: %.0f\n", entry.getKey(), entry.getValue()),
                    () -> System.out.println("  No data found.")
            );

            // Analysis 4: Filter transactions that exceed a given sale amount
            double threshold = 500.00;
            System.out.println("\n4. High Value Transactions (Total Sales > $" + String.format("%.2f", threshold) + ")");
            List<SalesRecord> highValue = analyzer.filterHighValueTransactions(threshold);
            highValue.forEach(record -> System.out.println("  " + record));

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Creates a temporary CSV file using the given content.
     * @param path    the file path where the CSV file should be created
     * @param content the full text content to write into the file
     */
    private static void createDummyCsvFile(final String path, final String content) {
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(path), content.getBytes());
        } catch (IOException e) {
            System.err.println("Could not create dummy file: " + e.getMessage());
        }
    }
}
