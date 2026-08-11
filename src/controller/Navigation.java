package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Navigation {
    private Navigation() {
    }

    public static void changeScene(Node source, String fxmlPath, double width, double height) {
        try {
            Parent root = new FXMLLoader(Navigation.class.getResource(fxmlPath)).load();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.centerOnScreen();
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot open screen: " + fxmlPath, exception);
        }
    }
}
