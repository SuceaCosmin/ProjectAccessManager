package main;

import controller.MainWindow;
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
    public void start(Stage window) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/MainWindow.fxml"));
        Parent root = loader.load();
        MainWindow controller = loader.getController();
        window.setTitle("Project Access Controller");
        window.setOnCloseRequest(e -> controller.saveUserSettings());
        window.setScene(new Scene(root, 600, 400));
        window.show();
        logger.debug("Main window has started..");
    }
}
