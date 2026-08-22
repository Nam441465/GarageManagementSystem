import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import database.DatabaseInitializer;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        DatabaseInitializer.initialize();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("ui/HomeView.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Hệ thống quản lý gara");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        Platform.runLater(() -> stage.setMaximized(true));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
