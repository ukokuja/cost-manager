package shenkar.java.costmanager.model;

import java.time.LocalDateTime;
import java.util.List;

public class Manager implements IManager {

    @Override
    public void addExpense(IExpense expense) {

    }

    @Override
    public void addExpenseCategory(IExpenseCategory category) {

    }

    @Override
    public List<? extends IExpense> getReport(LocalDateTime a, LocalDateTime b) {
        return null;
    }
}
