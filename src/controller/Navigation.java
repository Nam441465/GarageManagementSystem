package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Navigation {
    private Navigation() {
    }

    public static void changeScene(
            Node source,
            String fxmlPath,
            double width,
            double height) {

        System.out.println("=== CHANGE SCENE ===");
        System.out.println("FXML path = " + fxmlPath);

        try {
            var resource = Navigation.class.getResource(fxmlPath);

            System.out.println("Resource = " + resource);

            if (resource == null) {
                throw new IllegalStateException(
                        "Không tìm thấy FXML: " + fxmlPath);
            }

            Parent root = FXMLLoader.load(resource);

            Stage stage = (Stage) source.getScene().getWindow();

            stage.setScene(new Scene(root, width, height));
            stage.centerOnScreen();

        } catch (Exception exception) {
            exception.printStackTrace();

            throw new IllegalStateException(
                    "Không thể mở màn hình: " + fxmlPath,
                    exception);
        }
    }
}
