package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainWindow {

    private static Logger logger = LogManager.getLogger(MainWindow.class);

    public TreeView treeView;
    public Button addUser;


    private void AddUser() {

    }

    @FXML
    private void initialize() {
        TreeItem<String> dummyItem = new TreeItem<>();
        addUser.setOnMouseClicked(event -> logger.info("Adding a new customer"));
    }
}
