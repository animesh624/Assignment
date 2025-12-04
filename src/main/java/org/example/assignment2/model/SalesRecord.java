package org.example.assignment2.model;

import lombok.NonNull;

import java.util.Objects;

/**
 * Represents a single sales record from the CSV file.
 * This class also provides a helper method
 * to easily convert a CSV line into a SalesRecord object.
 */
public class SalesRecord {
    private final String transactionId;
    private final String productCategory;
    private final String region;
    private final int quantity;
    private final double price;
    private final double totalSales; // Calculated field

    /**
     * Creates a new SalesRecord with all required fields.
     */
    public SalesRecord(@NonNull final String transactionId,
                       @NonNull final String productCategory,
                       @NonNull final String region,
                       int quantity,
                       double price) {

        this.transactionId = Objects.requireNonNull(transactionId);
        this.productCategory = Objects.requireNonNull(productCategory);
        this.region = Objects.requireNonNull(region);
        this.quantity = quantity;
        this.price = price;
        this.totalSales = quantity * price;
    }

    public String getProductCategory() { return productCategory; }
    public String getRegion() { return region; }
    public int getQuantity() { return quantity; }
    public double getTotalSales() { return totalSales; }

    /**
     * Creates a SalesRecord object from a single CSV line.
     * @param line one line of CSV data
     * @return a populated SalesRecord object
     * @throws IllegalArgumentException if the line does not contain exactly 5 fields
     */
    public static SalesRecord fromCsvLine(String line) {
        String[] parts = line.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid CSV line format: " + line);
        }
        return new SalesRecord(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                Integer.parseInt(parts[3].trim()),
                Double.parseDouble(parts[4].trim())
        );
    }

    @Override
    public String toString() {
        return String.format("%s | %-15s | %-5s | Qty: %-3d | Total: $%.2f",
                transactionId, productCategory, region, quantity, totalSales);
    }
}
