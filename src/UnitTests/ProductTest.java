package UnitTests;

import Model.OutSourced;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import Model.Product;
import Model.Part;
import Model.InHouse;

class ProductTest {
    Product product;
    Part part1;
    Part part2;

    @BeforeEach
    void setUp() {
        product = new Product(1, "CPU & GPU", 550.00, 3, 1, 20);
        part1 = new InHouse(1, "CPU", 250.00, 15, 1, 100, 101);
        part2 = new OutSourced(2, "GPU", 300.00, 20, 1, 100, "EVGA");
    }

    @Test
    @DisplayName("Constructor initializes properties correctly")
    void testProductConstructor() {
        assertAll("Product constructor",
                () -> assertEquals(1, product.getProductID()),
                () -> assertEquals("CPU & GPU", product.getName()),
                () -> assertEquals(550.00, product.getPrice()),
                () -> assertEquals(3, product.getInStock()),
                () -> assertEquals(1, product.getMin()),
                () -> assertEquals(20, product.getMax())
        );
    }

    @Test
    @DisplayName("Setters update properties correctly")
    void testSetters() {
        product.setProductID(2);
        product.setName("RAM & Mobo");
        product.setPrice(200);
        product.setInStock(2);
        product.setMin(1);
        product.setMax(15);

        assertAll("Setters",
                () -> assertEquals(2, product.getProductID()),
                () -> assertEquals("RAM & Mobo", product.getName()),
                () -> assertEquals(200, product.getPrice()),
                () -> assertEquals(2, product.getInStock()),
                () -> assertEquals(1, product.getMin()),
                () -> assertEquals(15, product.getMax())
        );
    }

    @Test
    @DisplayName("Adding and removing associated parts")
    void testAddRemoveAssociatedPart() {
        product.addAssociatedPart(part1);
        assertTrue(product.getAssociatedParts().contains(part1), "Part should be present after addition.");

        product.removeAssociatedPart(part1.getId());
        assertFalse(product.getAssociatedParts().contains(part1), "Part should no longer be present after removal.");
    }
}

