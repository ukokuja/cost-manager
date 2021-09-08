package shenkar.java.costmanager.view;

import shenkar.java.costmanager.CostManagerException;
import shenkar.java.costmanager.model.Currency;
import shenkar.java.costmanager.model.Expense;
import shenkar.java.costmanager.model.ExpenseCategory;
import shenkar.java.costmanager.viewModel.IViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

public class View implements IView {
    private IViewModel vm;
    private ApplicationUI ui;
    private final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
            .toFormatter();

    /**
     * The View constructor sets the UI using the ApplicationUI inner class,
     * and then use it's start method to display the UI to the user.
     */
    public View() {
        SwingUtilities.invokeLater(() -> {
            setUi(new ApplicationUI());
            View.this.ui.start();
        });
    }

    public void setUi(ApplicationUI ui) {
        this.ui = ui;
    }

    public void setViewModel(IViewModel vm) {
        this.vm = vm;
    }

    /**
     * Calls for the updateTable() method in order to update the table
     * of the expense items.
     * @param cs - list of expense item to display in the table.
     */
    public void displayExpenseTable(List<Expense> cs) {

        String[][] table = new String[cs.size()][5];

        // Filling the table values with Cost Item's data.
        for (int i = 0; i < cs.size(); i++) {
            Expense expense = cs.get(i);
            table[i][0] = expense.getDate().toString();
            table[i][1] = expense.getCategory().toString();
            table[i][2] = Double.toString(expense.getAmount());
            table[i][3] = expense.getCurrency().name();
            table[i][4] = expense.getDescription();
        }
        View.this.ui.updateTable(table);
    }

    /**
     * Display the categories in the select drop list of "Add Cost Panel".
     * @param catNames
     */
    public void displayCategoriesSelect(ExpenseCategory[] catNames) {
        View.this.ui.updateCategoriesSelect(catNames);
    }

    /**
     * Display the currencies in the select drop list of "Add Cost Panel".
     * @param currencies
     */
    public void displayCurrenciesSelect(String[] currencies) {
        View.this.ui.updateCurrenciesSelect(currencies);
    }

    /**
     * Display the message in the home screen.
     * @param message
     */
    public void showMessage(String message) {
        this.ui.updateMessageBoard(message);
    }


    /**
     * This class implements all the user's GUI using Java Swing,
     * the class holds the panels as members, while each panel is
     * a screen in the application's frame.
     * This class also handling all the functionality of the View,
     * as in calling the IViewModel to get data, switch between screens and
     * displaying data from the IViewModel to the screens.
     */
    public class ApplicationUI {

        //General frame component
        private JFrame frame;
        //Current active panel
        private JPanel current;
        //All the application's panels
        private final MainPanel mainPanel;
        private final AddCostPanel addCostPanel;
        private final AddCategoryPanel addCategoryPanel;
        private final DatesChoosePanel dateChoosePanel;
        private final TablePanel tablePanel;
        private PieChartPanel chartPanel;

        public ApplicationUI() {

            //Starting the panels inner class data members
            mainPanel = new MainPanel();
            addCostPanel = new AddCostPanel();
            addCategoryPanel = new AddCategoryPanel();
            dateChoosePanel = new DatesChoosePanel();
            tablePanel = new TablePanel();
            chartPanel = new PieChartPanel();

            //General common components setup
            frame = new JFrame("CostManager");
            frame.setSize(900, 900);

            //Event listener for application closing
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    frame = null;
                    System.exit(0);
                }
            });
        }

        /**
         * The following methods are used by the View outer class in order to pass the data
         * to a specific panel.
         */
        public void updateMessageBoard(String message) {
            mainPanel.updateMessageBoard(message);
        }
        public void updateTable(String[][] table) {
            this.tablePanel.updateTableData(table);
        }
        public void updateCategoriesSelect(ExpenseCategory[] catNames) {
            this.addCostPanel.updateCategories(catNames);
        }
        public void updateCurrenciesSelect(String[] currencies) {
            this.addCostPanel.updateCurrencies(currencies);
        }


        public void start() {
            displayMainMenu();
        }
        /**
         * Sets the screen to be the main panel, this method called
         * in the start() method when the application starts running.
         */
        public void displayMainMenu() {
            replaceScreen(mainPanel);
        }

        /**
         * Removes the current panel, and then repainting in order to replace it
         * with the next desired panel.
         * @param next - the panel show next in the application.
         */
        public void replaceScreen(JPanel next) {
            if (this.current != null) {
                frame.remove(this.current);
            }
            frame.repaint();
            this.current = next;
            frame.add(this.current);
            frame.setVisible(true);
        }

        /**
         * This panel is used as the home screen of the application,
         * where the user can navigate through a menu with all the actions he can take in the application.
         * All the setup of this panel happens in the constructor, where the JComponents are initialized
         * and the event listeners are added.
         */
        public class MainPanel extends JPanel {

            //Buttons for navigating to other panels of the application
            private JButton btAddCostItem;
            private JButton btAddCategory;
            private JButton btDisplayBetweenDates;
            private JButton btDisplayTable;

            //Used to display messages to the user
            private JLabel jlMessage;
            private JTextArea taMessage;

            public MainPanel() {
                setBorder(new EmptyBorder(10, 10, 10, 10));
                setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.anchor = GridBagConstraints.NORTH;
                gbc.insets = new Insets(5, 5, 5, 5);

                add(new JLabel("<html><h1><strong><i>Welcome to cost manager</i></strong></h1><hr></html>"), gbc);

                gbc.anchor = GridBagConstraints.CENTER;
                gbc.fill = GridBagConstraints.BOTH;

                btAddCostItem = new JButton("Add Cost");
                btAddCategory = new JButton("Add Category");
                btDisplayBetweenDates = new JButton("Display expenses between dates");
                btDisplayTable = new JButton("Display Table");

                jlMessage = new JLabel("Message Board");
                taMessage = new JTextArea(7, 40);
                taMessage.setEditable(false);
                JScrollPane scroll = new JScrollPane(taMessage);

                JPanel buttons = new JPanel(new GridBagLayout());
                JPanel messageBoard = new JPanel(new GridBagLayout());

                buttons.add(btAddCostItem, gbc);
                buttons.add(btAddCategory, gbc);
                buttons.add(btDisplayBetweenDates, gbc);
                buttons.add(btDisplayTable, gbc);
                messageBoard.add(jlMessage, gbc);
                messageBoard.add(scroll, gbc);

                //Sends the user to the AddCostItem panel when pressed on Add Cost Item button
                btAddCostItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ApplicationUI.this.addCostPanel.cleanInputs();
                        View.this.vm.getCategories();
                        View.this.vm.getCurrencies();
                        ApplicationUI.this.replaceScreen(ApplicationUI.this.addCostPanel);
                    }
                });

                //Sends the user to the AddCategory panel when pressed on Add Category button
                btAddCategory.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ApplicationUI.this.addCategoryPanel.cleanInputs();
                        ApplicationUI.this.replaceScreen(ApplicationUI.this.addCategoryPanel);
                    }
                });

                //Sends the user to the select dates panel when pressed on Display Table button
                btDisplayTable.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ApplicationUI.this.dateChoosePanel.cleanInputs();
                        ApplicationUI.this.dateChoosePanel.updateButton("table");
                        ApplicationUI.this.replaceScreen(ApplicationUI.this.dateChoosePanel);
                    }
                });

                //Sends the user to the select dates panel when pressed on Display Pie Chart button
                btDisplayBetweenDates.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ApplicationUI.this.dateChoosePanel.cleanInputs();
                        ApplicationUI.this.dateChoosePanel.updateButton("Expeneses between dates");
                        ApplicationUI.this.replaceScreen(ApplicationUI.this.dateChoosePanel);
                    }
                });

                gbc.weighty = 1;
                this.add(buttons, gbc);
                this.add(messageBoard, gbc);
            }

            public void updateMessageBoard(String message) {
                this.taMessage.append(message + "\n");
            }
        }

        /**
         * This panel is used as the Add Cost screen of the application,
         * the user fills a form and when done the cost he will be added to the DB
         * by using an Event Listener.
         * All the setup of this panel happens in the constructor as well as setting the
         * event listeners.
         */
        public class AddCostPanel extends JPanel {

            private JComboBox cbChooseCategory;
            private JComboBox cbChooseCurrency;
            private TextField tfEnterAmount;
            private TextField tfEnterDescription;
            private JFormattedTextField tfDate;
            private JButton btSubmit;
            private JLabel jlEnterAmount;
            private JLabel jlEnterDescription;
            private JLabel jlDate;
            private JButton btBackToMainMenu;
            private JLabel jlHeader;
            private Font myFont;

            //Used for the select drop list in the form
            private ExpenseCategory[] categoriesOptions;
            private DefaultComboBoxModel<ExpenseCategory> defaultCBCategories;

            private String[] currencyOptions;
            private DefaultComboBoxModel<String> defaultCBCurrencies;


            public AddCostPanel() {
                setBorder(new EmptyBorder(10, 10, 10, 10));
                setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.insets = new Insets(5, 5, 5, 5);

                JPanel homePanel = new JPanel(new GridBagLayout());
                btBackToMainMenu = new JButton("Home");
                btSubmit = new JButton("Submit");
                gbc.weightx = 1;
                gbc.anchor = GridBagConstraints.WEST;
                homePanel.add(btBackToMainMenu, gbc);
                add(homePanel, gbc);

                //Sending the user back to the home screen (MainPanel)
                btBackToMainMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ApplicationUI.this.replaceScreen(ApplicationUI.this.mainPanel);
                    }
                });

                //Collecting data from user inputs and creating new Cost Item
                btSubmit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ExpenseCategory category = (ExpenseCategory) cbChooseCategory.getSelectedItem();
                        String currencyStr = cbChooseCurrency.getSelectedItem().toString();

                        Currency currencyEnum;
                        switch (currencyStr) {
                            case "ILS":
                                currencyEnum = Currency.NIS;
                                break;
                            case "USD":
                                currencyEnum = Currency.USD;
                                break;
                            default:
                                currencyEnum = Currency.USD;
                                break;
                        }
                        double amount = Double.parseDouble(tfEnterAmount.getText());
                        String description = tfEnterDescription.getText();
                        String date = tfDate.getText();

                        try {
                            //Trying to add data to DB
                            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                                    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
                                    .toFormatter();
                            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
                            Expense cs = new Expense(category, amount, currencyEnum, description, dateTime);
                            View.this.vm.addCostItem(cs);
                        } catch (CostManagerException err) {

                            View.this.ui.updateMessageBoard(err.getMessage());
                        }

                        //Rendering the Main Panel window.
                        ApplicationUI.this.replaceScreen(ApplicationUI.this.mainPanel);
                    }
                });

                gbc.weightx = 0;
                JPanel headerPanel = new JPanel(new GridBagLayout());
                jlHeader = new JLabel("<html><h1><strong><i>Add new cost item</i></strong></h1><hr></html>");
                gbc.anchor = GridBagConstraints.CENTER;
                headerPanel.add(jlHeader, gbc);
                add(headerPanel, gbc);

                gbc.anchor = GridBagConstraints.CENTER;
                gbc.fill = GridBagConstraints.BOTH;

                //Creating a JComboBox in order to initialize the select drop list
                categoriesOptions = new ExpenseCategory[0];
                View.this.vm.getCategories();
                defaultCBCategories = new DefaultComboBoxModel<>(categoriesOptions);
                cbChooseCategory = new JComboBox(defaultCBCategories);
                cbChooseCategory.setBackground(Color.white);
                cbChooseCategory.setRenderer(new MyComboBoxRenderer("Category"));
                cbChooseCategory.setSelectedIndex(-1); //By default it selects first item, we don't want any selection

                currencyOptions = new String[0];
                View.this.vm.getCurrencies();
                defaultCBCurrencies = new DefaultComboBoxModel<>(currencyOptions);
                cbChooseCurrency = new JComboBox(defaultCBCurrencies);
                cbChooseCurrency.setBackground(Color.white);
                cbChooseCurrency.setRenderer(new MyComboBoxRenderer("Currency"));
                cbChooseCurrency.setSelectedIndex(-1);

                //Creating the text fields of the form
                myFont = new Font("Default", Font.PLAIN, 12);
                jlEnterAmount = new JLabel("Enter Amount");
                tfEnterAmount = new TextField(5);
                tfEnterAmount.setFont(myFont);
                jlEnterDescription = new JLabel("Enter Description");
                tfEnterDescription = new TextField(20);
                tfEnterDescription.setFont(myFont);

                //Setting a format for the date field in the form
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                //DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        //.appendPattern("yyyy-MM-dd HH:mm:ss")
                        //.appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
                        //.toFormatter();
                jlDate = new JLabel("Enter date (yyyy-MM-dd HH:mm:ss):");
                tfDate = new JFormattedTextField(formatter);
                tfDate.setFont(myFont);

                JPanel form = new JPanel(new GridBagLayout());
                JPanel submit = new JPanel(new GridBagLayout());

                form.add(cbChooseCategory, gbc);
                form.add(cbChooseCurrency, gbc);
                form.add(jlEnterAmount, gbc);
                form.add(tfEnterAmount, gbc);
                form.add(jlEnterDescription, gbc);
                form.add(tfEnterDescription, gbc);
                form.add(jlDate, gbc);
                form.add(tfDate, gbc);
                submit.add(btSubmit, gbc);
                submit.setAlignmentX(RIGHT_ALIGNMENT);

                gbc.weighty = 1;
                add(form, gbc);
                add(submit, gbc);
            }

            /**
             * Clean all the previous inputs in this panel
             */
            public void cleanInputs() {
                tfEnterAmount.setText("");
                tfEnterDescription.setText("");
                cbChooseCategory.setSelectedIndex(-1);
                cbChooseCurrency.setSelectedIndex(-1);
                tfDate.setText(LocalDateTime.now().format(formatter));
            }

            /**
             * Updates the values of the select drop list of the categories
             * @param catNames
             */
            public void updateCategories(ExpenseCategory[] catNames) {
                this.categoriesOptions = catNames;

                this.defaultCBCategories.removeAllElements();
                for (ExpenseCategory category : this.categoriesOptions) {
                    this.defaultCBCategories.addElement(category);
                }
                this.cbChooseCategory.setSelectedIndex(-1);

//                ApplicationUI.this.replaceScreen(ApplicationUI.this.addCostPanel);
            }

            /**
             * Updates the values of the select drop list of the currencies
             * @param currencies
             */
            public void updateCurrencies(String[] currencies) {
                this.currencyOptions = currencies;

                this.defaultCBCurrencies.removeAllElements();
                for (String name : this.currencyOptions) {
                    this.defaultCBCurrencies.addElement(name);
                }
                this.cbChooseCurrency.setSelectedIndex(-1);

//                ApplicationUI.this.replaceScreen(ApplicationUI.this.addCostPanel);
            }

            /**
             * This class is used to set values into the select drop lists in the form,
             * it also used to set the first value the user will see and to get the value
             * the user selected.
             */
            class MyComboBoxRenderer extends JLabel implements ListCellRenderer {
                private String _title;

                public MyComboBoxRenderer(String title) {
                    _title = title;
                }

                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
                    if (index == -1 && value == null) setText(_title);
                    else setText(value.toString());
                    return this;
                }
            }
        }

        /**
         * This panel is used as the Add Category screen of the application,
         * the only thing in the form of this panel is a textual field for the
         * category name.
         */
        public class AddCategoryPanel extends JPanel {

            private TextField tfCategoryName;
            private JLabel jlEnterCategory;
            private JButton btSubmit;
            private JButton btBackToMainMenu;
            private JLabel jlHeader;
            private Font myFont;


            public AddCategoryPanel() {
                setBorder(new EmptyBorder(10, 10, 10, 10));
                setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.insets = new Insets(5, 5, 5, 5);

                JPanel homePanel = new JPanel(new GridBagLayout());
                btBackToMainMenu = new JButton("Home");
                gbc.weightx = 1;
                gbc.anchor = GridBagConstraints.WEST;
                homePanel.add(btBackToMainMenu, gbc);
                add(homePanel, gbc);

                //Sends the user back to the home screen (MainPanel)
                btBackToMainMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ApplicationUI.this.replaceScreen(ApplicationUI.this.mainPanel);
                    }
                });

                gbc.weightx = 0;
                JPanel headerPanel = new JPanel(new GridBagLayout());
                jlHeader = new JLabel("<html><h1><strong><i>Add new cost item</i></strong></h1><hr></html>");
                gbc.anchor = GridBagConstraints.CENTER;
                headerPanel.add(jlHeader, gbc);
                add(headerPanel, gbc);

                myFont = new Font("Default", Font.PLAIN, 12);
                tfCategoryName = new TextField("", 10);
                tfCategoryName.setFont(myFont);
                jlEnterCategory = new JLabel("<html><h3><strong><i>Enter new category:</i></strong></h3></html>");

                btSubmit = new JButton("Submit");
                JPanel form = new JPanel(new GridBagLayout());
                JPanel submit = new JPanel(new GridBagLayout());

                form.add(jlEnterCategory, gbc);
                form.add(tfCategoryName, gbc);
                submit.add(btSubmit, gbc);

                //Calls to the vm in order to add the new category
                btSubmit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String categoryName = tfCategoryName.getText();
                        ExpenseCategory category = new ExpenseCategory(categoryName);
                        View.this.vm.addCategory(category);
                        ApplicationUI.this.replaceScreen(ApplicationUI.this.mainPanel);
                    }
                });


                gbc.weighty = 1;
                add(form, gbc);
                add(submit, gbc);
            }

            public void cleanInputs() {
                tfCategoryName.setText("");
            }

        }

        /**
         * This panel is used to choose dates (from, to) for the table and pie chart panels,
         * it is a form of only two fields, depending on the submit button text it will call
         * the next panel (Table, Pie Chart).
         * All the setup of the panel including the event listeners are in the constructor.
         */
        class DatesChoosePanel extends JPanel {

            private JLabel jlHeader;
            private JFormattedTextField tfFromDate;
            private JLabel jlFromDate;
            private JFormattedTextField tfToDate;
            private JLabel jlToDate;
            private JButton btSubmit;
            private JButton btBackToMainMenu;

            //Also used in order to call the next panel
            private String buttonName;

            public DatesChoosePanel() {
                setBorder(new EmptyBorder(10, 10, 10, 10));
                setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.insets = new Insets(5, 5, 5, 5);

                JPanel homePanel = new JPanel(new GridBagLayout());
                btBackToMainMenu = new JButton("Home");
                btSubmit = new JButton("Submit");
                gbc.weightx = 1;
                gbc.anchor = GridBagConstraints.WEST;
                homePanel.add(btBackToMainMenu, gbc);
                add(homePanel, gbc);

                //Sends the user back to the home screen (MainPanel)
                btBackToMainMenu.addActionListener(new ActionListener() {
                    @Override

                    public void actionPerformed(ActionEvent e) {
                        ApplicationUI.this.replaceScreen(ApplicationUI.this.mainPanel);
                    }
                });

                //Submitting the dates and calls for the vm in order to get the data, then to the next panel

                btSubmit.addActionListener(e -> {
                    //reading dates from text box
                    String fromDate = tfFromDate.getText();
                    String toDate = tfToDate.getText();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    //DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                            //.appendPattern("yyyy-MM-dd HH:mm:ss")
                            //.appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
                            //.toFormatter();

                    if (buttonName.equals("table")) {
                        //sending dates to viewmodel to query the model and present the data in the app
                        LocalDateTime date1 = LocalDateTime.parse(fromDate, formatter);
                        LocalDateTime date2 = LocalDateTime.parse(toDate, formatter);
                        View.this.vm.getCostsForTable(LocalDateTime.parse(fromDate, formatter), LocalDateTime.parse(toDate, formatter));

                        //rendering the table panel
                        ApplicationUI.this.replaceScreen(ApplicationUI.this.tablePanel);

                    }
                });
                gbc.weightx = 0;
                JPanel headerPanel = new JPanel(new GridBagLayout());
                jlHeader = new JLabel("<html><h1><strong><i>Choose dates</i></strong></h1><hr></html>");
                gbc.anchor = GridBagConstraints.CENTER;
                headerPanel.add(jlHeader, gbc);
                add(headerPanel, gbc);

                JPanel form = new JPanel(new GridBagLayout());
                JPanel submit = new JPanel(new GridBagLayout());

                jlFromDate = new JLabel("<html><h3><strong><i>From date: yyyy-MM-dd HH:mm:ss</i></strong></h3></html>");
                jlToDate = new JLabel("<html><h3><strong><i>To date: yyyy-MM-dd HH:mm:ss</i></strong></h3></html>");

                Font myFont = new Font("Default", Font.PLAIN, 12);

                //Setting a format to the date fields
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                //DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        //.appendPattern("yyyy-MM-dd HH:mm:ss")
                        //.appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
                        //.toFormatter();
                tfFromDate = new JFormattedTextField(formatter);
                tfToDate = new JFormattedTextField(formatter);
                tfFromDate.setFont(myFont);
                tfToDate.setFont(myFont);

                gbc.fill = GridBagConstraints.HORIZONTAL;
                tfFromDate.setColumns(10);
                tfToDate.setColumns(10);
                form.add(jlFromDate, gbc);
                form.add(tfFromDate, gbc);
                form.add(jlToDate, gbc);
                form.add(tfToDate, gbc);
                submit.add(btSubmit, gbc);

                gbc.weighty = 1;
                add(form, gbc);
                add(submit, gbc);
            }

            //Update the submit button's text
            public void updateButton(String str) {
                this.buttonName = str;
                btSubmit.setText("Get " + str);
            }

            public void cleanInputs() {
                tfFromDate.setValue("2000-01-01 00:00:00");
                tfToDate.setValue(LocalDateTime.now().format(formatter));
            }

        }


        /**
         * This panel is used to display the table filled with cost item's data,
         * the table is held as a member using JTable.
         * The setup of the panel and the event listeners happens in the constructor,
         * while the filling of the table happens in the method updateTableData().
         */
        class TablePanel extends JPanel {
            private JPanel table;
            private GridBagConstraints gbc;
            private JScrollPane scrolledTable;
            private JButton btBackToMainMenu;
            private JLabel jlHeader;
            private JTable tableCosts;
            private String[] colNames = {"Date", "Category", "Amount", "Currency", "Description"};

            public TablePanel() {
                setBorder(new EmptyBorder(10, 10, 10, 10));
                setLayout(new GridBagLayout());
                gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.insets = new Insets(5, 5, 5, 5);

                JPanel homePanel = new JPanel(new GridBagLayout());
                btBackToMainMenu = new JButton("Home");
                gbc.weightx = 1;
                gbc.anchor = GridBagConstraints.WEST;
                homePanel.add(btBackToMainMenu, gbc);
                add(homePanel, gbc);

                //Sends the user back to the home screen (MainPanel)
                btBackToMainMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ApplicationUI.this.replaceScreen(ApplicationUI.this.mainPanel);
                    }
                });

                gbc.weightx = 0;
                JPanel headerPanel = new JPanel(new GridBagLayout());
                jlHeader = new JLabel("<html><h1><strong><i>Costs table</i></strong></h1><hr></html>");
                gbc.anchor = GridBagConstraints.CENTER;
                headerPanel.add(jlHeader, gbc);
                add(headerPanel, gbc);

                //The panel used to host the JTable
                table = new JPanel(new GridBagLayout());

                gbc.fill = GridBagConstraints.BOTH;
                gbc.weighty = 1;
                add(table, gbc);
                gbc.weighty = 0;
            }

            /**
             * Updates the data in the table with costs items,
             * then adding the table to the table's panel.
             * @param data - the data needed to be filled in the table.
             */
            public void updateTableData(String[][] data) {
                tableCosts = new JTable(data, this.colNames);

                //Making the table scrollable in cases of many data.
                tableCosts.setPreferredScrollableViewportSize(new Dimension(600, 300));
                tableCosts.setFillsViewportHeight(true);
                tableCosts.setEnabled(false);
                scrolledTable = new JScrollPane(tableCosts);
                table.removeAll();
                table.add(scrolledTable, gbc);

                ApplicationUI.this.replaceScreen(tablePanel);
            }
        }

        /**
         * This panel is used to display the pie chart filled with categories and sums data.
         * The setup of the panel and the event listeners happens in the constructor,
         * while the filling of the pie chart happens in the method updateChart().
         */
        class PieChartPanel extends JPanel {
            private GridBagConstraints gbc;
            private JButton btBackToMainMenu;
            private JLabel jlHeader;
            //The panel that holds the chart
            private JPanel chart;

            public PieChartPanel() {
                setBorder(new EmptyBorder(10, 10, 10, 10));
                setLayout(new GridBagLayout());
                gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.insets = new Insets(5, 5, 5, 5);

                JPanel homePanel = new JPanel(new GridBagLayout());
                btBackToMainMenu = new JButton("Home");
                gbc.weightx = 1;
                gbc.anchor = GridBagConstraints.WEST;
                homePanel.add(btBackToMainMenu, gbc);
                add(homePanel, gbc);

                //Sends the user back to the home screen (MainPanel)
                btBackToMainMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ApplicationUI.this.replaceScreen(ApplicationUI.this.mainPanel);
                    }
                });

                gbc.weightx = 0;
                JPanel headerPanel = new JPanel(new GridBagLayout());
                jlHeader = new JLabel("<html><h1><strong><i>Pie chart for category sum:</i></strong></h1><hr></html>");
                gbc.anchor = GridBagConstraints.CENTER;
                headerPanel.add(jlHeader, gbc);
                add(headerPanel, gbc);

                //Creating the panel that will hold our pie chart
                chart = new JPanel(new GridBagLayout());

                gbc.fill = GridBagConstraints.BOTH;
                gbc.weighty = 1;
                add(chart, gbc);
                gbc.weighty = 0;
            }
        }
    }



}
