package shenkar.java.costmanager.model;


import shenkar.java.costmanager.CostManagerException;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class main {

    public static void main(String args[])throws SQLException{
        Manager test=new Manager();
        ExpenseCategory cat = new ExpenseCategory("Food");
        Currency NIS = Currency.NIS;
        LocalDateTime datetime_a = LocalDateTime.now().minusDays(2);
        LocalDateTime datetime_b = LocalDateTime.now();
        Expense ex1 = null;
        try {
            ex1 = new Expense(cat,10,NIS,"Hamburger" , datetime_b);
        } catch (CostManagerException e) {
            e.printStackTrace();
        }

        for (Expense e : test.getReport(datetime_a, datetime_b)) {
            System.out.println(e);
        }

//        test.printQuery("select * from expense");
    }
}
