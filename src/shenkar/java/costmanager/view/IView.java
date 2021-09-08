package shenkar.java.costmanager.view;

import shenkar.java.costmanager.model.Expense;
import shenkar.java.costmanager.viewModel.IViewModel;

import java.util.List;

/**
 * IView interface defines the method that needs to be implemented for a view.
 * This view interface is part of the MVVM architecture, every method will
 * be provided to the ViewModel in order to work with the View.
 */
public interface IView {

    public abstract void displayExpenseTable(List<Expense> cs);
    public abstract void displayCategoriesSelect(String[] catNames);
    public abstract void displayCurrenciesSelect(String[] currencies);

    public abstract void showMessage(String message);
    public abstract void setViewModel(IViewModel vm);

}