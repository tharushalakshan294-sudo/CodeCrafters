package com.codecrafters.babyshop;

import com.codecrafters.babyshop.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/** Controller for login.fxml (Login / Register screen). */
public class LoginController {

    private final UserDAO userDAO = new UserDAO();

    @FXML private Pane loginPane, registerPane;
    @FXML private TextField loginUserField;
    @FXML private PasswordField loginPassField;
    @FXML private Label loginMsg;

    @FXML private TextField regUserField, regNameField, regEmailField, regPhoneField, regAddressField;
    @FXML private PasswordField regPassField, regPass2Field;
    @FXML private Label regMsg;

    // ---------- switching between the two forms ----------

    @FXML
    private void showRegister() {
        show(registerPane, loginPane);
        regMsg.setText("");
    }

    @FXML
    private void showLogin() {
        show(loginPane, registerPane);
        loginMsg.setText("");
    }

    private void show(Pane toShow, Pane toHide) {
        toHide.setVisible(false);
        toHide.setManaged(false);
        toShow.setVisible(true);
        toShow.setManaged(true);
    }

    // ---------- login ----------

    @FXML
    private void onLogin() {
        String user = loginUserField.getText().trim();
        String pass = loginPassField.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            loginMsg.setStyle("-fx-text-fill: -color-danger-fg;");
            loginMsg.setText("Please enter your username and password.");
            return;
        }
        try {
            if (userDAO.authenticate(user, pass)) {
                App.showMain((Stage) loginUserField.getScene().getWindow());
            } else {
                loginMsg.setStyle("-fx-text-fill: -color-danger-fg;");
                loginMsg.setText("Invalid username or password.");
                loginPassField.clear();
            }
        } catch (SQLException e) {
            loginMsg.setStyle("-fx-text-fill: -color-danger-fg;");
            loginMsg.setText("Database error: " + e.getMessage());
        }
    }

    // ---------- register ----------

    @FXML
    private void onRegister() {
        String user = regUserField.getText().trim();
        String pass = regPassField.getText();
        String pass2 = regPass2Field.getText();
        String name = regNameField.getText().trim();
        String email = regEmailField.getText().trim();
        String phone = regPhoneField.getText().trim();
        String address = regAddressField.getText().trim();

        String error = validate(user, pass, pass2, name, email, phone);
        if (error != null) {
            regMsg.setStyle("-fx-text-fill: -color-danger-fg;");
            regMsg.setText(error);
            return;
        }
        try {
            if (userDAO.usernameExists(user)) {
                regMsg.setStyle("-fx-text-fill: -color-danger-fg;");
                regMsg.setText("That username is already taken.");
                return;
            }
            userDAO.register(user, pass, name, email, phone, address);

            // back to the login form with a success message and the username filled in
            loginUserField.setText(user);
            loginPassField.clear();
            clearRegisterForm();
            showLogin();
            loginMsg.setStyle("-fx-text-fill: -color-success-fg;");
            loginMsg.setText("Account created. Please log in.");
        } catch (SQLIntegrityConstraintViolationException e) {
            regMsg.setStyle("-fx-text-fill: -color-danger-fg;");
            regMsg.setText("That username is already taken.");
        } catch (SQLException e) {
            regMsg.setStyle("-fx-text-fill: -color-danger-fg;");
            regMsg.setText("Database error: " + e.getMessage());
        }
    }

    private String validate(String user, String pass, String pass2, String name, String email, String phone) {
        if (user.isEmpty() || pass.isEmpty() || pass2.isEmpty() || name.isEmpty())
            return "Please fill in all fields marked with *.";
        if (user.length() > 50) return "Username must be 50 characters or fewer.";
        if (pass.length() < 6) return "Password must be at least 6 characters.";
        if (!pass.equals(pass2)) return "Passwords do not match.";
        if (!email.isEmpty() && !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            return "Please enter a valid email address.";
        if (phone.length() > 15) return "Phone number must be 15 characters or fewer.";
        return null;
    }

    private void clearRegisterForm() {
        regUserField.clear();
        regPassField.clear();
        regPass2Field.clear();
        regNameField.clear();
        regEmailField.clear();
        regPhoneField.clear();
        regAddressField.clear();
        regMsg.setText("");
    }
}
