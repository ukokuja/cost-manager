package shenkar.java.costmanager.model.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shenkar.java.costmanager.CostManagerException;
import shenkar.java.costmanager.model.Currency;
import shenkar.java.costmanager.model.Expense;
import shenkar.java.costmanager.model.ExpenseCategory;
import shenkar.java.costmanager.model.Manager;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the DB functionality
 */
class DBTest {

    Manager manager = new Manager();
    LocalDateTime datetime_a = LocalDateTime.of(2020, 11, 12, 10, 10, 10);
    LocalDateTime datetime_b = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        ExpenseCategory cat = new ExpenseCategory("Food");
        Currency NIS = Currency.NIS;
        Expense ex1 = null;
        try {
            ex1 = new Expense(cat, 10, NIS, "Hamburger", datetime_a);
            manager.createCategoriesTable();
            manager.createExpensessTable();
            manager.addExpenseCategory(cat);
            manager.addExpense(ex1);
        } catch (CostManagerException e) {
            e.printStackTrace();
        }

    }


    @Test
    void testReport() {
        List<Expense> report = null;
        try {
            report = (LinkedList<Expense>) manager.getReport(datetime_a, datetime_b);
        } catch (CostManagerException e) {
            e.printStackTrace();
        }
        Expense e = report.get(0);
        String expected = "Expense{" +
                "id=1" +
                ", category=ExpenseCategory{" +
                "id=1" +
                ", name='Food'" +
                '}' +
                ", amount=10.0" +
                ", currency=NIS" +
                ", description='Hamburger'" +
                ", date=2020-11-12T10:10:10" +
                '}';
        String actual = e.toString();
        assertEquals(expected, actual);
        assertEquals(report.toArray().length, 1);
    }
}