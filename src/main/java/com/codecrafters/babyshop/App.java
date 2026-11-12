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

        // TODO (last step): load login.fxml first, then switch to main.fxml after login
        Parent root = FXMLLoader.load(App.class.getResource("main.fxml"));
        stage.setScene(new Scene(root, 1150, 720));
        stage.setTitle("Baby Clothing Shop - Management System");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
