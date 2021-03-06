package shenkar.java.costmanager.viewModel;


import shenkar.java.costmanager.CostManagerException;
import shenkar.java.costmanager.model.Currency;
import shenkar.java.costmanager.model.Expense;
import shenkar.java.costmanager.model.ExpenseCategory;
import shenkar.java.costmanager.model.IManager;
import shenkar.java.costmanager.view.IView;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is used to get data from the IModel and then update the IView with data we got.
 * In order to keep the View working without interupting it, we use a ExcutorService and for each
 * method we will submit a new thread into the pool.
 */
public class ViewModel implements IViewModel {
    private IManager model;
    private IView view;
    private ExecutorService pool;

    /**
     * Sets the ExecutorService to work with 10 threads.
     */
    public ViewModel() {
        setPool(Executors.newFixedThreadPool(10));
    }

    @Override
    public void setPool(ExecutorService pool) { this.pool = pool; }

    @Override
    public void setView(IView view) {
        this.view = view;
    }

    @Override
    public void setModel(IManager model) {
        this.model = model;
    }

    /**
     * When called, ask the IModel to add a new category to our db.
     * In case of either success or failure the user will be displayed with
     * a relevant message.
     * @param category - the category name to add to our db.
     */
    @Override
    public void addCategory(ExpenseCategory category) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //Add the category to db by calling the model
                    model.addExpenseCategory(category);
                } catch (CostManagerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * When called, ask the IModel to add a new cost item to our db.
     * In case of either success or failure the user will be displayed with
     * a relevant message.
     * @param item - the CostItem to add to our db.
     */
    @Override
    public void addCostItem(Expense item) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //Add the cost item to db by calling the model
                    model.addExpense(item);
                } catch (CostManagerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * When called, ask the IModel to return all the costs between two dates in our db.
     * In case of either success or failure the user will be displayed with
     * a relevant message.
     * @param dateFrom - get only costs from this date.
     * @param dateTo - get only costs to this date.
     */


    @Override
    public void getCostsForTable(LocalDateTime dateFrom, LocalDateTime dateTo) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                //Get list of cost items between to dates from the db by calling the model
                List<Expense> tableInfo = null;
                try {
                    tableInfo = (List<Expense>) model.getReport(dateFrom, dateTo);
                } catch (CostManagerException e) {
                    e.printStackTrace();
                }
                //If the operation was done successfully show the table
                view.displayExpenseTable(tableInfo);
            }
        });
    }



    /**
     * When called, ask the IModel to return all the categories in our db.
     * In case of either success or failure the user will be displayed with
     * a relevant message.
     */
    @Override
    public void getCategories() {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                //Get list of all existing categories in our db by calling the model
                ExpenseCategory[] categoryList = null;
                try {
                    categoryList = model.getAllCategories().toArray(new ExpenseCategory[0]);
                    view.displayCategoriesSelect(categoryList);
                } catch (CostManagerException e) {
                    e.printStackTrace();
                }

            }
        });
    }






    /**
     * When called, return all the Currencies name values in our Currency Enum.
     */
    @Override
    public void getCurrencies() {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                //Use the Currency Enum to get all the names values and send them to the view
                view.displayCurrenciesSelect(Arrays.toString(Currency.values()).replaceAll("^.|.$", "").split(", "));
            }
        });
    }
}