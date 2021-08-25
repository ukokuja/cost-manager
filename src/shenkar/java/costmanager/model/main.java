package shenkar.java.costmanager.model;


import shenkar.java.costmanager.CostManagerException;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class main {

    public static void main(String args[])throws SQLException{
        Manager test=new Manager();
        ExpenseCategory cat = new ExpenseCategory("Food");
        Currency NIS = Currency.NIS;
        Currency USD = Currency.USD;
        LocalDateTime datetime1 = LocalDateTime.now();
        Expense ex1 = null;
        try {
            ex1 = new Expense(cat,10,NIS,"Hamburger" , datetime1);
        } catch (CostManagerException e) {
            e.printStackTrace();
        }
        test.createCategoriesTable();
        test.createExpensessTable();
        test.addExpenseCategory(cat);
        test.addExpense(ex1);

//        test.printQuery("select * from expense");
    }
}
