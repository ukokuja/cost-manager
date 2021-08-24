package shenkar.java.costmanager.model;

public class ExpenseCategory implements IExpenseCategory {
    private String name;

    public ExpenseCategory(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
