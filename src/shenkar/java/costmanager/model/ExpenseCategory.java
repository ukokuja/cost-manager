package shenkar.java.costmanager.model;
import  shenkar.java.costmanager.model.*;

public class ExpenseCategory implements IExpenseCategory {

    private int id;
    private String name;

    public ExpenseCategory(String name) {
        setId(-1);
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ExpenseCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
