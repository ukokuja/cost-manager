package shenkar.java.costmanager.model;

import java.time.LocalDateTime;
import java.sql.*;
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
                    " VALUES (" + expense.getCategory().getId()  +
                    ", '" + expense.getCurrency() +
                    "', " + expense.getAmount() +
                    ", '" + expense.getDate() +
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
        return null;
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
                            "date varchar(63), " +
                            "description varchar(100)," +
                            "CONSTRAINT expense_id PRIMARY KEY (id)," +
                            "CONSTRAINT category_id FOREIGN KEY (ID) REFERENCES category(ID))");
            connection.close();
        } catch (SQLException e) {
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

    public void printQuery(String query) {
        try {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(JDBC_URL);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                System.out.format("%20s", resultSetMetaData.getColumnName(i) + " | ");
            }
            while (resultSet.next()) {
                System.out.println("");
                for (int i = 1; i <= columnCount; i++) {
                    System.out.format("%20s", resultSet.getString(i) + " | ");
                }
            }
            //CLOSE CONNECTION
            statement.close();
            connection.close();

        } catch (SQLException | ClassNotFoundException exeption) {
            System.out.println("Exception: " + exeption);
        }
    }
}
