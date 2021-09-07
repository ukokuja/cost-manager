/**
 * @authors: Jonatan Kobany (204397244) and Lucas Kujawski (345535330)
 */

package shenkar.java.costmanager.model;

import shenkar.java.costmanager.CostManagerException;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.LinkedList;
import java.util.List;


public class Manager implements IManager {

    public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String JDBC_URL = "jdbc:derby:MyLifeDB;create=true";

    /**
     * Inserts expense into the DB
     * @param expense: Expense instance to add
     */
    @Override
    public void addExpense(Expense expense) throws CostManagerException {
        Connection connection = getConnection();
        try {
            connection.createStatement().execute("insert into EXPENSE (CATEGORY_ID, CURRENCY, AMOUNT, DATE, DESCRIPTION)" +
                    " VALUES (" + expense.getCategory().getId() +
                    ", '" + expense.getCurrency() +
                    "', " + expense.getAmount() +
                    ", '" + Timestamp.valueOf(expense.getDate()) +
                    "', '" + expense.getDescription() + "')");
            connection.close();
        } catch (SQLException e) {
            throw new CostManagerException("Could not add expense", e);
        }


    }

    /**
     * Inserts category into the DB
     * @param category: Category instance to add
     */
    @Override
    public void addExpenseCategory(ExpenseCategory category) throws CostManagerException {
        Connection connection = getConnection();
        try {
            PreparedStatement pstmt = connection.prepareStatement("insert into CATEGORY (NAME)" +
                    " VALUES ('" + category.getName() + "')", Statement.RETURN_GENERATED_KEYS);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            category.setId(rs.getInt(1));
            connection.close();
        } catch (SQLException e) {
            throw new CostManagerException("Could not add category", e);
        }
    }

    private Connection getConnection() throws CostManagerException {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(JDBC_URL);
        } catch (ClassNotFoundException | SQLException e) {
            throw new CostManagerException("Could not connect to DB", e);
        }
    }

    /**
     * Returns a list of expenses between given dates
     * @param a: from date
     * @param b: to date
     * @return: list of expenses
     */
    @Override
    public List<? extends Expense> getReport(LocalDateTime a, LocalDateTime b) throws CostManagerException {
        List<Expense> expenses = new LinkedList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "select * from expense ee left join category cc on ee.category_id = cc.id" +
                            " where ee.DATE BETWEEN ? and ?", Statement.RETURN_GENERATED_KEYS);
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
                    .toFormatter();
            pstmt.setString(1, a.format(formatter));
            pstmt.setString(2, b.format(formatter));
            pstmt.executeQuery();

            ResultSet rs = pstmt.getResultSet();
            while (rs.next()) {
                ExpenseCategory ec = new ExpenseCategory(rs.getInt(7), rs.getString(8));

                LocalDateTime dateTime = LocalDateTime.parse(rs.getString(5), formatter);
                Expense expense = new Expense(rs.getInt(1), ec, rs.getDouble(3), Currency.valueOf(rs.getString(4)), rs.getString(6), dateTime);
                expenses.add(expense);
            }
            connection.close();
        } catch (SQLException e) {
            throw new CostManagerException("Could not get report from DB", e);
        }
        return expenses;
    }

    /**
     * Creates expenses table
     */
    @Override
    public void createExpensessTable() throws CostManagerException {
        Connection connection = getConnection();
        try {
            connection.createStatement().execute(
                    "create table expense(" +
                            "id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                            "category_id int NOT NULL, " +
                            "amount decimal(5,2)," +
                            "currency varchar(3), " +
                            "date TIMESTAMP, " +
                            "description varchar(100)," +
                            "CONSTRAINT expense_id PRIMARY KEY (id)," +
                            "CONSTRAINT category_id FOREIGN KEY (ID) REFERENCES category(ID))");
            connection.close();
        } catch (SQLException e) {
            // Table exist error
            if (e.getSQLState().equals("X0Y32"))
                return;
            throw new CostManagerException("Could not create expenses table to DB", e);
        }

    }


    /**
     * Creates Categories table
     */
    @Override
    public void createCategoriesTable() throws CostManagerException {
        Connection connection = getConnection();
        try {
            connection.createStatement().execute("create table category(" +
                    "id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                    "name varchar(20), " +
                    "CONSTRAINT id PRIMARY KEY (id))" +
                    "");
            connection.close();
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32"))
                return;
            throw new CostManagerException("Could not create categories table to DB", e);
        }
    }

    @Override
    public List<ExpenseCategory> getAllCategories() throws CostManagerException {
        List<ExpenseCategory> categories = new LinkedList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "select * from category", Statement.RETURN_GENERATED_KEYS);
            pstmt.executeQuery();

            ResultSet rs = pstmt.getResultSet();
            while (rs.next()) {
                ExpenseCategory ec = new ExpenseCategory(rs.getInt(1), rs.getString(2));
                categories.add(ec);
            }
            connection.close();
        } catch (SQLException e) {
            throw new CostManagerException("Could not get categories from DB", e);
        }
        return categories;
    }


}
