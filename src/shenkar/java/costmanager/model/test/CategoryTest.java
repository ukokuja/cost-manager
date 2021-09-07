package shenkar.java.costmanager.model.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shenkar.java.costmanager.model.ExpenseCategory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for all the Category class functionality
 */
class CategoryTest {

    ExpenseCategory category;

    @BeforeEach
    void setUp() {
        category = new ExpenseCategory("Shopping");
    }

    @AfterEach
    void tearDown() {
        category = null;
    }

    @Test
    void getName() {
        String expected = "Shopping";
        String actual = category.getName();
        assertEquals(expected, actual);
    }

    @Test
    void getId() {
        int expected = -1;
        int actual = category.getId();
        assertEquals(expected, actual, 0);
    }

    @Test
    void testToString() {
        String expected = "ExpenseCategory{" +
                "id=" + -1 + "," +
                " name='Shopping'" +
                '}';
        String actual = category.toString();
        assertEquals(expected, actual);
    }
}