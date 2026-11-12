package com.codecrafters.babyshop;

import atlantafx.base.theme.PrimerLight;
import com.codecrafters.babyshop.db.DBConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        try {
            DBConnection.init();   // creates tables if they don't exist
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Cannot connect to the database.\nCheck that MySQL is running and db.properties is correct.\n\n"
                            + e.getMessage()).showAndWait();
            Platform.exit();
            return;
        }

        showLogin(stage);
    }

    /** Login / Register screen (shown first). */
    public static void showLogin(Stage stage) {
        try {
            Parent root = FXMLLoader.load(App.class.getResource("login.fxml"));
            stage.setScene(new Scene(root, 900, 620));
            stage.setTitle("Baby Clothing Shop - Login");
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Main management screen (shown after a successful login). */
    public static void showMain(Stage stage) {
        try {
            Parent root = FXMLLoader.load(App.class.getResource("main.fxml"));
            stage.setResizable(true);
            stage.setScene(new Scene(root, 1150, 720));
            stage.setTitle("Baby Clothing Shop - Management System");
            stage.centerOnScreen();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Cannot open main screen:\n" + e.getMessage()).showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
