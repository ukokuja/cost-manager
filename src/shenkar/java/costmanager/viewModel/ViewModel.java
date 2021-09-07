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
                String message = "";
                try {
                    //Add the category to db by calling the model
                    model.addExpenseCategory(category);
                    //If the operation was done successfully show this message
                    message = "Category: " + category.toString() + "  was added successfully";
                } catch (CostManagerException e) {
                    //If we couldn't add the category to the db, show this message
                    message = "Error adding " + category.toString() + " category";
                } finally {
                    view.showMessage(message);
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
                String message = "";
                try {
                    //Add the cost item to db by calling the model
                    model.addExpense(item);
                    //If the operation was done successfully show this message
                    message = "New cost: " + item.toString() + "  was added successfully";
                } catch (CostManagerException e) {
                    //If we couldn't add the cost item to the db, show this message
                    message = "Error with cost item:  " + item.toString();
                } finally {
                    view.showMessage(message);
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
                view.displayCostItemTable(tableInfo);
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
                List<ExpenseCategory> categoryList = null;
                try {
                    categoryList = model.getAllCategories();
                } catch (CostManagerException e) {
                    e.printStackTrace();
                }
                //Creating array in order to send it to the view
                String[] categoriesNames = new String[categoryList.size()];
                //Fill the array with all the category names in the list
                for (int i = 0; i < categoryList.size(); i++) {
                    categoriesNames[i] = categoryList.get(i).getName();
                }
                //If the operation was done send the categories names to the view
                view.displayCategoriesSelect(categoriesNames);
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