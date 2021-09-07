package shenkar.java.costmanager.model.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shenkar.java.costmanager.CostManagerException;
import shenkar.java.costmanager.model.Currency;
import shenkar.java.costmanager.model.Expense;
import shenkar.java.costmanager.model.ExpenseCategory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for all the Expense class functionality
 */
class ExpenseTest {

    private Expense costItem = null;
    private ExpenseCategory category = null;

    @BeforeEach
    void setUp() throws CostManagerException {
        category = new ExpenseCategory("shopping");
        costItem = new Expense(category, 550, Currency.NIS, "shoe shopping", LocalDateTime.of(2020, 11, 12, 10, 10, 10));
    }

    @AfterEach
    void tearDown() {
        category = null;
        costItem = null;
    }

    @Test
    void getId() {
        int expected = -1;
        int actual = costItem.getId();
        assertEquals(expected, actual);
    }

    @Test
    void getCategory() {
        ExpenseCategory expected = new ExpenseCategory("shopping");
        ExpenseCategory actual = costItem.getCategory();
        assertEquals(expected, actual);

    }

    @Test
    void getAmount() {
        double expected = 550;
        double actual = costItem.getAmount();
        assertEquals(expected, actual);
    }

    @Test
    void getCurrency() {
        assertEquals(Currency.NIS, costItem.getCurrency());
    }

    @Test
    void getDescription() {
        String expected = "shoe shopping";
        String actual = costItem.getDescription();
        assertEquals(expected, actual);
    }

    @Test
    void getDate() {

        LocalDateTime actual = costItem.getDate();

        assertEquals("2020-11-12T10:10:10", actual.toString());
    }

    @Test
    void testToString() {

        String expected = "Expense{" +
                "id=-1" +
                ", category=ExpenseCategory{" +
                "id=-1" +
                ", name='shopping'" +
                '}' +
                ", amount=550.0" +
                ", currency=NIS" +
                ", description='shoe shopping'" +
                ", date=2020-11-12T10:10:10" +
                '}';
        String actual = costItem.toString();
        assertEquals(expected, actual);
    }
}