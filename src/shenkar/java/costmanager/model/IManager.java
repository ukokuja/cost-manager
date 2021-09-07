package shenkar.java.costmanager.model;

import shenkar.java.costmanager.CostManagerException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Manager interface
 */
public interface IManager {
    public abstract void addExpense(Expense expense) throws CostManagerException;
    public abstract void addExpenseCategory(ExpenseCategory category) throws CostManagerException;
    public abstract List<? extends Expense> getReport(LocalDateTime a, LocalDateTime b) throws CostManagerException;
    public abstract void createExpensessTable() throws CostManagerException;
    public abstract void createCategoriesTable() throws CostManagerException;
    List<ExpenseCategory> getAllCategories() throws CostManagerException;
}
