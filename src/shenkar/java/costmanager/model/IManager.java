package shenkar.java.costmanager.model;

import java.time.LocalDateTime;
import java.util.List;

public interface IManager {

    public void addExpense(IExpense expense);
    public void addExpenseCategory(IExpenseCategory category);
    public List<? extends IExpense> getReport(LocalDateTime a, LocalDateTime b);
}
