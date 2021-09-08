package shenkar.java.costmanager.model;

import java.util.Objects;

/**
 * Expense category implementation
 */
public class ExpenseCategory implements IExpenseCategory {

    private int id;
    private String name;

    /**
     * Constructor when id is unknown
     * @param name: category name
     */
    public ExpenseCategory(String name) {
        setId(-1);
        setName(name);
    }

    /**
     * Constructor when id is known
     * @param id: category id
     * @param name: category name
     */
    public ExpenseCategory(int id, String name) {
        setId(id);
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
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseCategory that = (ExpenseCategory) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
