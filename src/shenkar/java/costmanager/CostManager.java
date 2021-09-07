package shenkar.java.costmanager;

import shenkar.java.costmanager.model.IManager;
import shenkar.java.costmanager.model.Manager;
import shenkar.java.costmanager.view.IView;
import shenkar.java.costmanager.view.View;
import shenkar.java.costmanager.viewModel.IViewModel;
import shenkar.java.costmanager.viewModel.ViewModel;

public class CostManager {
    public static void main(String args[]) {
        //creating the application components
        IManager model = new Manager();
        IView view = new View();
        IViewModel vm = new ViewModel();

        //connecting the components with each other
        view.setViewModel(vm);
        vm.setModel(model);
        vm.setView(view);

    }
}


