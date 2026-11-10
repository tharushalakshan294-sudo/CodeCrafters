package com.codecrafters.babyshop;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

/**
 * UI-only controller. Data is kept in memory for now;
 * replace the lists with DAO / database calls later.
 */
public class MainController {

    // ---------- simple model classes ----------
    public static class Item {
        String code, name, size; int qty; double price;
        Item(String code, String name, String size, int qty, double price) {
            this.code = code; this.name = name; this.size = size; this.qty = qty; this.price = price;
        }
    }
    public static class Txn {
        String date, desc, type; double amount;
        Txn(String date, String desc, String type, double amount) {
            this.date = date; this.desc = desc; this.type = type; this.amount = amount;
        }
    }
    public static class Employee {
        String name, role; double salary; boolean paid;
        Employee(String name, String role, double salary) { this.name = name; this.role = role; this.salary = salary; }
    }

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
        invCodeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().code));
        invNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().name));
        invSizeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().size));
        invQtyCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().qty)));
        invPriceCol.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().price)));
        inventoryTable.setItems(items);

        // Transaction table
        trTypeBox.setItems(FXCollections.observableArrayList("Income", "Expense"));
        trTypeBox.getSelectionModel().selectFirst();
        trDateCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().date));
        trDescCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().desc));
        trTypeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().type));
        trAmountCol.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().amount)));
        transactionTable.setItems(txns);

        // Salary table
        salNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().name));
        salRoleCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().role));
        salAmountCol.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().salary)));
        salStatusCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().paid ? "Paid" : "Pending"));
        salaryTable.setItems(employees);

        // Sample data (delete when DB is connected)
        items.addAll(new Item("B001", "Cotton Onesie", "0-3M", 25, 1250),
                     new Item("B002", "Baby Frock", "6-12M", 6, 2400),
                     new Item("B003", "Knitted Cap", "NB", 4, 650));
        txns.add(new Txn(LocalDate.now().toString(), "Opening balance", "Income", 50000));
        employees.addAll(new Employee("Nimal Perera", "Cashier", 45000),
                         new Employee("Kamani Silva", "Sales Assistant", 40000));

        refreshDashboard();
    }

    // ================= Inventory actions =================
    @FXML
    private void onAddItem() {
        try {
            if (codeField.getText().isBlank() || nameField.getText().isBlank()) {
                alert("Please enter item code and name."); return;
            }
            items.add(new Item(codeField.getText().trim(), nameField.getText().trim(), sizeField.getText().trim(),
                    Integer.parseInt(qtyField.getText().trim()), Double.parseDouble(priceField.getText().trim())));
            codeField.clear(); nameField.clear(); sizeField.clear(); qtyField.clear(); priceField.clear();
            refreshDashboard();
        } catch (NumberFormatException e) {
            alert("Quantity and price must be valid numbers.");
        }
    }

    @FXML private void onAddStock() { changeStock(1); }
    @FXML private void onReduceStock() { changeStock(-1); }

    private void changeStock(int sign) {
        Item selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) { alert("Select an item first."); return; }
        try {
            int amount = Integer.parseInt(qtyField.getText().trim());
            if (selected.qty + sign * amount < 0) { alert("Not enough stock."); return; }
            selected.qty += sign * amount;
            qtyField.clear();
            inventoryTable.refresh();
            refreshDashboard();
        } catch (NumberFormatException e) {
            alert("Enter a valid quantity.");
        }
    }

    @FXML
    private void onDeleteItem() {
        Item selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) { alert("Select an item first."); return; }
        items.remove(selected);
        refreshDashboard();
    }

    // ================= Transaction actions =================
    @FXML
    private void onAddTransaction() {
        try {
            if (trDescField.getText().isBlank()) { alert("Enter a description."); return; }
            txns.add(new Txn(LocalDate.now().toString(), trDescField.getText().trim(),
                    trTypeBox.getValue(), Double.parseDouble(trAmountField.getText().trim())));
            trDescField.clear(); trAmountField.clear();
            refreshDashboard();
        } catch (NumberFormatException e) {
            alert("Amount must be a valid number.");
        }
    }

    // ================= Salary actions =================
    @FXML
    private void onAddEmployee() {
        try {
            if (salNameField.getText().isBlank()) { alert("Enter employee name."); return; }
            employees.add(new Employee(salNameField.getText().trim(), salRoleField.getText().trim(),
                    Double.parseDouble(salAmountField.getText().trim())));
            salNameField.clear(); salRoleField.clear(); salAmountField.clear();
            refreshDashboard();
        } catch (NumberFormatException e) {
            alert("Salary must be a valid number.");
        }
    }

    @FXML
    private void onPaySalary() {
        Employee e = salaryTable.getSelectionModel().getSelectedItem();
        if (e == null) { alert("Select an employee first."); return; }
        if (e.paid) { alert(e.name + " is already paid."); return; }
        e.paid = true;
        // paying a salary is also an expense transaction
        txns.add(new Txn(LocalDate.now().toString(), "Salary - " + e.name, "Expense", e.salary));
        salaryTable.refresh();
        refreshDashboard();
    }

    // ================= Dashboard =================
    private void refreshDashboard() {
        int total = items.stream().mapToInt(i -> i.qty).sum();
        totalItemsLabel.setText(String.valueOf(total));

        lowStockList.getItems().clear();
        items.stream().filter(i -> i.qty < 10)
                .forEach(i -> lowStockList.getItems().add(i.name + " (" + i.size + ") - only " + i.qty + " left"));
        lowStockLabel.setText(String.valueOf(lowStockList.getItems().size()));

        double balance = 0;
        for (Txn t : txns) balance += t.type.equals("Income") ? t.amount : -t.amount;
        balanceLabel.setText(String.format("%.2f", balance));

        double pending = employees.stream().filter(e -> !e.paid).mapToDouble(e -> e.salary).sum();
        pendingSalaryLabel.setText(String.format("%.2f", pending));
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }
}
