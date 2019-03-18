package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Run extends Application {

    private static final Logger logger = LogManager.getLogger(Run.class.getName());

    public static void main(String[] args) {
        Application.launch(Run.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/MainWindow.fxml"));
        primaryStage.setTitle("Project Access Controller");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        logger.debug("Main window has started..");
    }
}
