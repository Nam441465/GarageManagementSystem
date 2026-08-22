import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import database.DatabaseInitializer;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        DatabaseInitializer.initialize();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("ui/HomeView.fxml"));

        Scene scene = new Scene(loader.load());

        stage.setTitle("Hệ thống quản lý gara");
        stage.setScene(scene);
        controller.Navigation.maximize(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}