import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import database.DatabaseInitializer;


public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {

        DatabaseInitializer.initialize();


        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource("ui/LoginView.fxml")
                );


        Scene scene = new Scene(loader.load(),400,300);


        stage.setTitle("Đăng nhập");

        stage.setScene(scene);

        stage.show();

    }


    public static void main(String[] args){

        launch(args);

    }

}
