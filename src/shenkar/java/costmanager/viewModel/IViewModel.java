package shenkar.java.costmanager.viewModel;


import shenkar.java.costmanager.model.Expense;
import shenkar.java.costmanager.model.ExpenseCategory;
import shenkar.java.costmanager.model.IManager;
import shenkar.java.costmanager.view.IView;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;


/**
 * IViewModel interface defines the method that needs to be implemented for a ViewModel.
 * This viewmodel interface is part of the MVVM architecture, every method will
 * be provided to the View in order to work with the Model.
 */
public interface IViewModel {

    public void setPool(ExecutorService pool);
    public void setView(IView view);
    public void setModel(IManager model);

    public void addCategory(ExpenseCategory category);
    public void addCostItem(Expense item);
    public void getCostsForTable(LocalDateTime dateFrom, LocalDateTime dateTo);

    public void getCategories();
    public void getCurrencies();

}