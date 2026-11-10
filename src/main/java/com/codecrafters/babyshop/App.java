package com.codecrafters.babyshop;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

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
