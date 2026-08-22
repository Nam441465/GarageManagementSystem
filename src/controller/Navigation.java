package controller;

import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
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

            stage.setScene(new Scene(root));
            maximize(stage);

        } catch (Exception exception) {
            exception.printStackTrace();

            throw new IllegalStateException(
                    "Không thể mở màn hình: " + fxmlPath,
                    exception);
        }
    }

    public static void maximize(Stage stage) {
        stage.show();
        Platform.runLater(() -> {
            stage.setMaximized(true);

            Platform.runLater(() -> {
                if (!stage.isMaximized()) {
                    var bounds = Screen.getPrimary().getVisualBounds();
                    stage.setX(bounds.getMinX());
                    stage.setY(bounds.getMinY());
                    stage.setWidth(bounds.getWidth());
                    stage.setHeight(bounds.getHeight());
                }
            });
        });
    }
}