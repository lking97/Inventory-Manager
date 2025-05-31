package UnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import Model.InHouse;
import Model.OutSourced;

public class PartTest {

    InHouse inHousePart;
    OutSourced outSourcedPart;

    @BeforeEach
    void setUp() {
        // Setting up with example data for each test
        inHousePart = new InHouse(1, "CPU", 250.00, 15, 1, 100, 101);
        outSourcedPart = new OutSourced(2, "GPU", 300.00, 20, 1, 100, "EVGA");
    }

    @Test
    @DisplayName("Test InHouse Part's Constructor and Getters")
    void testInHousePartConstructorAndGetters() {
        assertAll("Constructor and Getters",
                () -> assertEquals(1, inHousePart.getId()),
                () -> assertEquals("CPU", inHousePart.getName()),
                () -> assertEquals(250.00, inHousePart.getPrice()),
                () -> assertEquals(15, inHousePart.getStock()),
                () -> assertEquals(1, inHousePart.getMin()),
                () -> assertEquals(100, inHousePart.getMax()),
                () -> assertEquals(101, inHousePart.getMachineID())
        );
    }

    @Test
    @DisplayName("Test OutSourced Part's Constructor and Getters")
    void testOutSourcedPartConstructorAndGetters() {
        assertAll("Constructor and Getters",
                () -> assertEquals(2, outSourcedPart.getId()),
                () -> assertEquals("GPU", outSourcedPart.getName()),
                () -> assertEquals(300.00, outSourcedPart.getPrice()),
                () -> assertEquals(20, outSourcedPart.getStock()),
                () -> assertEquals(1, outSourcedPart.getMin()),
                () -> assertEquals(100, outSourcedPart.getMax()),
                () -> assertEquals("EVGA", outSourcedPart.getCompanyName())
        );
    }

    @Test
    @DisplayName("Test Setters for InHouse Part")
    void testInHousePartSetters() {
        inHousePart.setId(3);
        inHousePart.setName("RAM");
        inHousePart.setPrice(75.00);
        inHousePart.setStock(30);
        inHousePart.setMin(1);
        inHousePart.setMax(100);
        inHousePart.setMachineID(102);

        assertAll("Setters",
                () -> assertEquals(3, inHousePart.getId()),
                () -> assertEquals("RAM", inHousePart.getName()),
                () -> assertEquals(75.00, inHousePart.getPrice()),
                () -> assertEquals(30, inHousePart.getStock()),
                () -> assertEquals(1, inHousePart.getMin()),
                () -> assertEquals(100, inHousePart.getMax()),
                () -> assertEquals(102, inHousePart.getMachineID())
        );
    }

    @Test
    @DisplayName("Test Setters for OutSourced Part")
    void testOutSourcedPartSetters() {
        outSourcedPart.setId(4);
        outSourcedPart.setName("Mobo");
        outSourcedPart.setPrice(125.00);
        outSourcedPart.setStock(10);
        outSourcedPart.setMin(1);
        outSourcedPart.setMax(100);
        outSourcedPart.setCompanyName("Gigabyte");

        assertAll("Setters",
                () -> assertEquals(4, outSourcedPart.getId()),
                () -> assertEquals("Mobo", outSourcedPart.getName()),
                () -> assertEquals(125.00, outSourcedPart.getPrice()),
                () -> assertEquals(10, outSourcedPart.getStock()),
                () -> assertEquals(1, outSourcedPart.getMin()),
                () -> assertEquals(100, outSourcedPart.getMax()),
                () -> assertEquals("Gigabyte", outSourcedPart.getCompanyName())
        );
    }
}
