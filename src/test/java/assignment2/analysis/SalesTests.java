package assignment2.analysis;

import org.example.assignment2.analyzer.SalesAnalyzer;
import org.example.assignment2.model.SalesRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SalesTests {

    // --- Part 1: Testing the Parser (Model) ---

    @Test
    void testCsvParsingValidLine() {
        String csvLine = "T1001,Electronics,North,2,50.00";
        SalesRecord record = SalesRecord.fromCsvLine(csvLine);

        assertEquals("Electronics", record.getProductCategory());
        assertEquals("North", record.getRegion());
        assertEquals(2, record.getQuantity());
        assertEquals(100.00, record.getTotalSales(), 0.001, "Total sales should be Qty * Price");
    }

    @Test
    void testCsvParsingInvalidLine() {
        String invalidLine = "T1001,Electronics,North"; // Missing columns
        assertThrows(IllegalArgumentException.class, () -> {
            SalesRecord.fromCsvLine(invalidLine);
        });
    }

    // --- Part 2: Testing the Analyzer (Logic) ---

    private SalesAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        // Create a controlled dataset for testing
        // R1: Electronics, 100 total
        // R2: Electronics, 200 total
        // R3: Apparel, 50 total
        List<SalesRecord> mockData = Arrays.asList(
                new SalesRecord("1", "Electronics", "North", 1, 100.0),
                new SalesRecord("2", "Electronics", "South", 2, 100.0),
                new SalesRecord("3", "Apparel", "North", 1, 50.0)
        );
        analyzer = new SalesAnalyzer(mockData);
    }

    @Test
    void testAnalyzeTotalSalesByCategory() {
        Map<String, Double> result = analyzer.analyzeTotalSalesByCategory();

        // Electronics: 100 + 200 = 300
        assertEquals(300.0, result.get("Electronics"), 0.001);
        // Apparel: 50
        assertEquals(50.0, result.get("Apparel"), 0.001);
    }

    @Test
    void testAnalyzeAverageSalesPerRegion() {
        Map<String, Double> result = analyzer.analyzeAverageSalesPerRegion();

        // North: (100 + 50) / 2 = 75.0
        assertEquals(75.0, result.get("North"), 0.001);
        // South: 200 / 1 = 200.0
        assertEquals(200.0, result.get("South"), 0.001);
    }

    @Test
    void testFindTopSellingCategoryByQuantity() {
        Optional<Map.Entry<String, Double>> result = analyzer.findTopSellingCategoryByQuantity();

        assertTrue(result.isPresent());
        // Electronics Qty: 1 + 2 = 3
        // Apparel Qty: 1
        assertEquals("Electronics", result.get().getKey());
        assertEquals(3.0, result.get().getValue());
    }

    @Test
    void testFilterHighValueTransactions() {
        // Threshold 150. Only record "2" (Total 200) should match.
        List<SalesRecord> result = analyzer.filterHighValueTransactions(150.0);

        assertEquals(1, result.size());
        assertEquals("South", result.get(0).getRegion());
    }

    @Test
    void testFilterHighValueTransactionsNoMatch() {
        // Threshold 1000. Nothing should match.
        List<SalesRecord> result = analyzer.filterHighValueTransactions(1000.0);
        assertTrue(result.isEmpty());
    }
}