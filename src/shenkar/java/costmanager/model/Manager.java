package shenkar.java.costmanager.model;

import shenkar.java.costmanager.CostManagerException;

import java.time.LocalDateTime;
import java.sql.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.LinkedList;
import java.util.List;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;


public class Manager implements IManager {

    public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String JDBC_URL = "jdbc:derby:MyLifeDB;create=true";

    @Override
    public void addExpense(Expense expense) {

        try {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(JDBC_URL);
            //connection.createStatement().execute("create table expense(ID varchar(20), category varchar(20), sum decimal(5,2),currency varchar(3), date varchar(20), description varchar(100))");
            connection.createStatement().execute("insert into EXPENSE (CATEGORY_ID, CURRENCY, AMOUNT, DATE, DESCRIPTION)" +
                    " VALUES (" + expense.getCategory().getId() +
                    ", '" + expense.getCurrency() +
                    "', " + expense.getAmount() +
                    ", '" + Timestamp.valueOf(expense.getDate()) +
                    "', '" + expense.getDescription() + "')");
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void addExpenseCategory(ExpenseCategory category) {
        try {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(JDBC_URL);
            //connection.createStatement().execute("create table expense(ID varchar(20), category varchar(20), sum decimal(5,2),currency varchar(3), date varchar(20), description varchar(100))");
            PreparedStatement pstmt = connection.prepareStatement("insert into CATEGORY (NAME)" +
                    " VALUES ('" + category.getName() + "')", Statement.RETURN_GENERATED_KEYS);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            category.setId(rs.getInt(1));
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<? extends Expense> getReport(LocalDateTime a, LocalDateTime b) {
        List<Expense> expenses = new LinkedList<>();
        try {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(JDBC_URL);
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
        } catch (SQLException | ClassNotFoundException | CostManagerException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    @Override
    public void createExpensessTable() {
        try {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(JDBC_URL);
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
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void createCategoriesTable() {
        try {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(JDBC_URL);
            connection.createStatement().execute("create table category(" +
                    "id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                    "name varchar(20), " +
                    "CONSTRAINT id PRIMARY KEY (id))" +
                    "");
            connection.close();
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32"))
                return;
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Manager() {
    }

}
