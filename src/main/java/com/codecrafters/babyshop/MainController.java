package com.codecrafters.babyshop;
import java.sql.SQLIntegrityConstraintViolationException;
import com.codecrafters.babyshop.dao.EmployeeDAO;
import com.codecrafters.babyshop.dao.ItemDAO;
import com.codecrafters.babyshop.dao.TransactionDAO;
import com.codecrafters.babyshop.model.Employee;
import com.codecrafters.babyshop.model.Item;
import com.codecrafters.babyshop.model.Txn;
import com.codecrafters.babyshop.service.InventoryService;
import com.codecrafters.babyshop.exception.InsufficientStockException;
import com.codecrafters.babyshop.util.CsvExporter;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Path;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class MainController {

    private final ItemDAO itemDAO = new ItemDAO();
    private final TransactionDAO txnDAO = new TransactionDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final InventoryService inventoryService = new InventoryService();

    private final ObservableList<Item> items = FXCollections.observableArrayList();
    private final ObservableList<Txn> txns = FXCollections.observableArrayList();
    private final ObservableList<Employee> employees = FXCollections.observableArrayList();

    // ---------- Dashboard ----------
    @FXML private Label totalItemsLabel, lowStockLabel, balanceLabel, pendingSalaryLabel;
    @FXML private ListView<String> lowStockList;

    // ---------- Inventory ----------
    @FXML private TableView<Item> inventoryTable;
    @FXML private TableColumn<Item, String> invCodeCol, invNameCol, invSizeCol;
    @FXML private TableColumn<Item, String> invQtyCol, invPriceCol;
    @FXML private TextField codeField, nameField, sizeField, qtyField, priceField;

    // ---------- Transactions ----------
    @FXML private TableView<Txn> transactionTable;
    @FXML private TableColumn<Txn, String> trDateCol, trDescCol, trTypeCol, trAmountCol;
    @FXML private TextField trDescField, trAmountField;
    @FXML private ComboBox<String> trTypeBox;

    // ---------- Salaries ----------
    @FXML private TableView<Employee> salaryTable;
    @FXML private TableColumn<Employee, String> salNameCol, salRoleCol, salAmountCol, salStatusCol;
    @FXML private TextField salNameField, salRoleField, salAmountField;

    @FXML
    public void initialize() {
        // Inventory table
        invCodeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));
        invNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        invSizeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSize()));
        invQtyCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getQty())));
        invPriceCol.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().getPrice())));
        inventoryTable.setItems(items);

        // Transaction table
        trTypeBox.setItems(FXCollections.observableArrayList("Income", "Expense"));
        trTypeBox.getSelectionModel().selectFirst();
        trDateCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDate()));
        trDescCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDesc()));
        trTypeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType()));
        trAmountCol.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().getAmount())));
        transactionTable.setItems(txns);

        // Salary table
        salNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        salRoleCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmployeeRole()));
        salAmountCol.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().getSalary())));
        salStatusCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isPaid() ? "Paid" : "Pending"));
        salaryTable.setItems(employees);

        loadAllData();
    }

    /** Loads everything from the database into the tables. */
    private void loadAllData() {
        try {
            items.setAll(itemDAO.findAll());
            txns.setAll(txnDAO.findAll());
            employees.setAll(employeeDAO.findAll());
            refreshDashboard();
        } catch (SQLException e) {
            dbError(e);
        }
    }

    // ================= Inventory actions =================
    @FXML
    private void onAddItem() {
        if (codeField.getText().isBlank() || nameField.getText().isBlank()) {
            alert("Please enter item code and name."); return;
        }
        try {
            Item item = new Item(codeField.getText().trim(), nameField.getText().trim(), sizeField.getText().trim(),
                    Integer.parseInt(qtyField.getText().trim()), Double.parseDouble(priceField.getText().trim()));
            itemDAO.insert(item);
            items.add(item);
            codeField.clear(); nameField.clear(); sizeField.clear(); qtyField.clear(); priceField.clear();
            refreshDashboard();
        } catch (NumberFormatException e) {
            alert("Quantity and price must be valid numbers.");
        } catch (SQLIntegrityConstraintViolationException e) {
            alert("An item with that code already exists.");
        } catch (IllegalArgumentException e) {
            alert(e.getMessage());
        } catch (SQLException e) {
            dbError(e);
        }
    }

    @FXML private void onAddStock() { changeStock(1); }
    @FXML private void onReduceStock() { changeStock(-1); }

    private void changeStock(int sign) {
        Item selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) { alert("Select an item first."); return; }
        try {
            int amount = Integer.parseInt(qtyField.getText().trim());
            int newQty = inventoryService.calculateNewQuantity(selected, amount, sign > 0);
            itemDAO.updateQty(selected.getCode(), newQty);
            selected.setQty(newQty);
            qtyField.clear();
            inventoryTable.refresh();
            refreshDashboard();
        } catch (NumberFormatException e) {
            alert("Enter a valid quantity.");
        } catch (InsufficientStockException | IllegalArgumentException e) {
            alert(e.getMessage());
        } catch (SQLException e) {
            dbError(e);
        }
    }

    @FXML
    private void onDeleteItem() {
        Item selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) { alert("Select an item first."); return; }
        try {
            itemDAO.delete(selected.getCode());
            items.remove(selected);
            refreshDashboard();
        } catch (SQLException e) {
            dbError(e);
        }
    }

    // ================= Transaction actions =================
    @FXML
    private void onAddTransaction() {
        if (trDescField.getText().isBlank()) { alert("Enter a description."); return; }
        try {
            Txn t = txnDAO.insert(trDescField.getText().trim(), trTypeBox.getValue(),
                    Double.parseDouble(trAmountField.getText().trim()));
            txns.add(0, t);
            trDescField.clear(); trAmountField.clear();
            refreshDashboard();
        } catch (NumberFormatException e) {
            alert("Amount must be a valid number.");
        } catch (IllegalArgumentException e) {
            alert(e.getMessage());
        } catch (SQLException e) {
            dbError(e);
        }
    }

    @FXML
    private void onExportTransactions() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export transaction report");
        chooser.setInitialFileName("babyshop-transactions.csv");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        File file = chooser.showSaveDialog(transactionTable.getScene().getWindow());
        if (file == null) return;
        try {
            new CsvExporter().exportTransactions(txns, Path.of(file.toURI()));
            new Alert(Alert.AlertType.INFORMATION, "Transaction report exported successfully.").showAndWait();
        } catch (java.io.IOException e) {
            new Alert(Alert.AlertType.ERROR, "Could not export report: " + e.getMessage()).showAndWait();
        }
    }

    // ================= Salary actions =================
    @FXML
    private void onAddEmployee() {
        if (salNameField.getText().isBlank()) { alert("Enter employee name."); return; }
        try {
            Employee emp = employeeDAO.insert(salNameField.getText().trim(), salRoleField.getText().trim(),
                    Double.parseDouble(salAmountField.getText().trim()));
            employees.add(emp);
            salNameField.clear(); salRoleField.clear(); salAmountField.clear();
            refreshDashboard();
        } catch (NumberFormatException e) {
            alert("Salary must be a valid number.");
        } catch (IllegalArgumentException e) {
            alert(e.getMessage());
        } catch (SQLException e) {
            dbError(e);
        }
    }

    @FXML
    private void onPaySalary() {
        Employee e = salaryTable.getSelectionModel().getSelectedItem();
        if (e == null) { alert("Select an employee first."); return; }
        if (e.isPaid()) { alert(e.getName() + " is already paid."); return; }
        try {
            Txn expense = employeeDAO.paySalary(e);   // marks paid + records expense atomically
            e.setPaid(true);
            txns.add(0, expense);
            salaryTable.refresh();
            refreshDashboard();
        } catch (SQLException ex) {
            dbError(ex);
        }
    }

    // ================= Dashboard =================
    private void refreshDashboard() {
        totalItemsLabel.setText(String.valueOf(items.stream().mapToInt(i -> i.getQty()).sum()));

        lowStockList.getItems().clear();
        items.stream().filter(i -> i.getQty() < 10)
                .forEach(i -> lowStockList.getItems().add(i.getName() + " (" + i.getSize() + ") - only " + i.getQty() + " left"));
        lowStockLabel.setText(String.valueOf(lowStockList.getItems().size()));

        double balance = 0;
        for (Txn t : txns) balance += t.getType().equals("Income") ? t.getAmount() : -t.getAmount();
        balanceLabel.setText(String.format("%.2f", balance));

        double pending = employees.stream().filter(e -> !e.isPaid()).mapToDouble(e -> e.getSalary()).sum();
        pendingSalaryLabel.setText(String.format("%.2f", pending));
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }

    private void dbError(SQLException e) {
        new Alert(Alert.AlertType.ERROR, "Database error:\n" + e.getMessage()).showAndWait();
    }
}
