package shenkar.java.costmanager.model;

import java.time.LocalDateTime;
import java.util.List;

public interface IManager {
    public abstract void addExpense(Expense expense);
    public abstract void addExpenseCategory(ExpenseCategory category);
    public abstract List<? extends Expense> getReport(LocalDateTime a, LocalDateTime b);
    public abstract void createExpensessTable();
    public abstract void createCategoriesTable();
    public abstract void printQuery(String query);

}
