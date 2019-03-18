package controller;

import core.data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class MainWindow {

    private static Logger logger = LogManager.getLogger(MainWindow.class);

    public TreeView treeView;
    public Button addUser;
    public BorderPane mainPanel;
    public ListView<User> userListView;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    private void AddUser() {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainPanel.getScene().getWindow());

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/CreateUser.fxml"));
            dialog.getDialogPane().setContent(loader.load());

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            logger.debug("Creating new user..");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {

                    CreateUser controller = loader.getController();
                    User user = controller.getUser();
                    logger.debug(user);
                    userList.add(user);
                } else if (result.get() == ButtonType.CANCEL) {
                    logger.debug("Creating new user operation canceled..");
                }
            }

        } catch (Exception e) {
            logger.warn("Failed to load AddUser view due to: " + e.getMessage());
            logger.debug(e);
        }
    }

    @FXML
    private void initialize() {
        userListView.setItems(userList);
        userListView.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        TreeItem<String> dummyItem = new TreeItem<>();
    }
}
