package org.example.assignment2.analyzer;

import lombok.NonNull;
import org.example.assignment2.model.SalesRecord;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The SalesAnalyzer class performs different types of analysis
 * on a collection of {@link SalesRecord} objects.
 */
public class SalesAnalyzer {

    private final List<SalesRecord> records;

    /**
     * Creates a new analyzer to process the given sales records.
     * @param records list of sales records to analyze
     */
    public SalesAnalyzer(@NonNull final List<SalesRecord> records) {
        this.records = records;
    }

    /**
     * Computes total sales for each product category.
     * @return a map where the key is the category and the value is total sales amount
     */
    public Map<String, Double> analyzeTotalSalesByCategory() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getProductCategory,
                        Collectors.summingDouble(SalesRecord::getTotalSales)
                ));
    }

    /**
     * Calculates the average sales amount for each region.
     * @return a map of region to average sales value
     */
    public Map<String, Double> analyzeAverageSalesPerRegion() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getRegion,
                        Collectors.averagingDouble(SalesRecord::getTotalSales)
                ));
    }

    /**
     * Finds the product category with the highest total quantity sold.
     * @return an Optional containing the entry with the highest quantity,
     *         or empty if no records are present
     */
    public Optional<Map.Entry<String, Double>> findTopSellingCategoryByQuantity() {
        final Map<String, Double> quantityByCategory = records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getProductCategory,
                        Collectors.summingDouble(SalesRecord::getQuantity)
                ));

        return quantityByCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue());
    }

    /**
     * Returns all transactions whose total sales exceed a specified threshold.
     * The results are sorted by region and then by total sales (descending).
     */
    public List<SalesRecord> filterHighValueTransactions(double minSalesThreshold) {
        return records.stream()
                .filter(record -> record.getTotalSales() > minSalesThreshold)
                .sorted(Comparator.comparing(SalesRecord::getRegion)
                        .thenComparing(SalesRecord::getTotalSales).reversed())
                .collect(Collectors.toList());
    }
}
