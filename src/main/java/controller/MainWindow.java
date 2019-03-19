package controller;

import core.data.User;
import datasource.xml.data.ProjectAccessManagementConfigurationType;
import datasource.xml.data.UserType;
import datasource.xml.handler.PacConfigurationHandler;
import datasource.xml.mapper.UserEntryMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class MainWindow {

    private static Logger logger = LogManager.getLogger(MainWindow.class);

    public BorderPane mainPanel;

    public TreeView treeView;
    public Button addUser;
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
    private void OpenConfiguration() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open configuration");

        FileChooser.ExtensionFilter exception = new FileChooser.ExtensionFilter("Xml Files", "*.xml");
        chooser.getExtensionFilters().add(exception);
        File result = chooser.showOpenDialog(mainPanel.getScene().getWindow());

        if (result == null) {
            logger.info("Open operation has been canceled!");
            return;
        }
        try {
            PacConfigurationHandler handler = new PacConfigurationHandler();
            ProjectAccessManagementConfigurationType config = handler.loadConfiguration(result);

            UserEntryMapper mapper = new UserEntryMapper();
            List<User> importerUsers = mapper.mapToCoreType(config.getUsers());
            setUsers(importerUsers);

        } catch (Exception e) {
            logger.warn(String.format("Failed to load configuration from file(%s) due to: %s", result.getPath(), e.getMessage()));
        }
    }


    @FXML
    private void SaveConfigurationToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        FileChooser.ExtensionFilter exception = new FileChooser.ExtensionFilter("Xml Files", "*.xml");
        fileChooser.getExtensionFilters().add(exception);
        File file = fileChooser.showOpenDialog(mainPanel.getScene().getWindow());

        if (file == null) {
            logger.info("Save operation has been canceled!");
            return;
        }
        try {
            UserEntryMapper mapper = new UserEntryMapper();
            List<UserType> users = mapper.mapToDataSourceType(userList);
            ProjectAccessManagementConfigurationType config = new ProjectAccessManagementConfigurationType();
            config.setUsers(users);

            PacConfigurationHandler handle = new PacConfigurationHandler();
            handle.saveConfiguration(config, file);

        } catch (Exception e) {
            logger.warn(String.format("Failed to Store configuration to system(%s) due to:%s", file.getPath(), e.getMessage()));
        }

    }


    /**
     * Method used to updated the list of users that visible inside the List view.
     *
     * @param importedUsers represents a list of users will be displayed on the List View.
     */
    private void setUsers(List<User> importedUsers) {

        if (importedUsers == null) {
            throw new IllegalArgumentException("importedUsers is null!");
        }
        userList.clear();
        userList.addAll(importedUsers);
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
