package shenkar.java.costmanager.model;

import shenkar.java.costmanager.CostManagerException;

import java.time.LocalDateTime;

public class Expense implements IExpense {

    private IExpenseCategory category;
    private double sum;
    private Currency currency;
    private String description;
    private LocalDateTime date;

    public Expense(IExpenseCategory category, int sum, Currency currency, String description) {
        setCategory(category);
        setSum(sum);
        setCurrency(currency);
        setDescription(description);
    }

    public IExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(IExpenseCategory category) {
        this.category = category;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) throws CostManagerException {
        if (sum < 0) {
            throw new CostManagerException("Cost should be a positive double");
        }
        this.sum = sum;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
