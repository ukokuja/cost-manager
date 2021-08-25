package shenkar.java.costmanager.model;

import shenkar.java.costmanager.CostManagerException;

import java.time.LocalDateTime;

public class Expense implements IExpense {

    private int id;
    private ExpenseCategory category;
    private double amount;
    private Currency currency;
    private String description;
    private LocalDateTime date;

    public Expense(ExpenseCategory category, double sum, Currency currency, String description, LocalDateTime date) throws CostManagerException {
        setCategory(category);
        setAmount(sum);
        setCurrency(currency);
        setDescription(description);
        setLocalDate(date);
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) throws CostManagerException {
        if (amount < 0) {
            throw new CostManagerException("Cost should be a positive double");
        }
        this.amount = amount;
    }

    public void setId(int Id) {
        this.id = Id;
    }

    public void setLocalDate(LocalDateTime Date) {
        this.date = Date;
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

    public int getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
