package controller;

import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
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
            String title) {

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
            
            if (title != null && !title.isEmpty()) {
                stage.setTitle(title);
            }

            Scene scene = stage.getScene();
            if (scene == null) {
                stage.setScene(new Scene(root));
            } else {
                scene.setRoot(root);
            }

            stage.setMaximized(true);

        } catch (Exception exception) {
            exception.printStackTrace();

            throw new IllegalStateException(
                    "Không thể mở màn hình: " + fxmlPath,
                    exception);
        }
    }

    public static void changeScene(
            Node source,
            String fxmlPath,
            double width,
            double height) {
        changeScene(source, fxmlPath, null);
    }

    public static void changeScene(Node source, String fxmlPath) {
        changeScene(source, fxmlPath, null);
    }

    public static void maximize(Stage stage) {
        stage.setMaximized(true);
        stage.show();
        Platform.runLater(() -> {
            stage.setMaximized(true);
        });
    }
}
