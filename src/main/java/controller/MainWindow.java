package controller;

import controller.builder.TreeBuilder;
import core.data.Configuration;
import core.data.User;
import datasource.xml.data.ProjectAccessManagementConfigurationType;
import datasource.xml.data.UserType;
import datasource.xml.handler.PacConfigurationHandler;
import datasource.xml.mapper.UserEntryMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import usersettings.UserSettingsHandler;
import usersettings.data.UserSettings;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class MainWindow {

    private static Logger logger = LogManager.getLogger(MainWindow.class);

    public BorderPane mainPanel;

    public TreeView treeView;

    public Menu recentFiles;
    public Pane contentPanel;

    private Configuration configuration;
    private UserSettings userSettings;


    @FXML
    private void initialize() {

        loadUserSettings();

        if (userSettings != null) {
            configureRecentFiles();

        }
        configuration = new Configuration();

        TreeBuilder builder = new TreeBuilder();


        treeView.setRoot(builder.buildTreeStructure(configuration));
        treeView.setShowRoot(true);

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem item = (TreeItem) observable.getValue();

            if (item != null) {

                String value = item.getValue().toString();
                switchDisplayedView(value);

            }

        });


    }

    private void switchDisplayedView(String value) {
        if (value == null) {
            return;
        }

        try {
            if (value.equals(TreeBuilder.USERS)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("view/UserList.fxml"));

                contentPanel.getChildren().add(loader.load());
            } else {
                clearContentView();
            }
        } catch (Exception e) {
            logger.warn(String.format("Failed to render %s view due to: %s", value, e.getMessage()));
        }

    }

    private void clearContentView() {
        contentPanel.getChildren().clear();
    }


    @FXML
    private void closeAction() {
        Platform.exit();
    }

    /**
     * Method use to configured the last opened configuration under "Recent Files" menu tab.
     */
    private void configureRecentFiles() {
        try {
            for (String path : userSettings.RecentlyOpenedFiles()) {
                MenuItem item = new MenuItem();
                item.setText(path);
                item.setOnAction(event -> {
                    OpenConfiguration(new File(path));
                });

                recentFiles.getItems().add(item);
            }
            recentFiles.setDisable(recentFiles.getItems().isEmpty());
        } catch (Exception e) {
            logger.warn("Failed to load recent files!");
        }
    }

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
                    configuration.AddUser(user);
                    loadTreeViewData();
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
        if (userSettings.isLastBrowsedFolderAvailable()) {
            chooser.setInitialDirectory(new File(userSettings.getLastBrowsedFolder()));
        }

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
            configuration = new Configuration();
            configuration.AddAllUsers(importerUsers);
            userSettings.setLastBrowsedFolder(result.getParent());
            userSettings.AddRecentFile(result.getPath());

            loadTreeViewData();
        } catch (Exception e) {
            logger.warn(String.format("Failed to load configuration from file(%s) due to: %s", result.getPath(), e.getMessage()));
        }
    }

    private void OpenConfiguration(File file) {
        try {
            PacConfigurationHandler handler = new PacConfigurationHandler();
            ProjectAccessManagementConfigurationType config = handler.loadConfiguration(file);

            UserEntryMapper mapper = new UserEntryMapper();
            List<User> importerUsers = mapper.mapToCoreType(config.getUsers());
            configuration = new Configuration();
            configuration.AddAllUsers(importerUsers);
            loadTreeViewData();
        } catch (Exception e) {
            logger.warn(String.format("Failed to load configuration from file(%s) due to: %s", file.getPath(), e.getMessage()));
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
            List<UserType> users = mapper.mapToDataSourceType(configuration.getUserList());
            ProjectAccessManagementConfigurationType config = new ProjectAccessManagementConfigurationType();
            config.setUsers(users);

            PacConfigurationHandler handle = new PacConfigurationHandler();
            handle.saveConfiguration(config, file);

        } catch (Exception e) {
            logger.warn(String.format("Failed to Store configuration to system(%s) due to:%s", file.getPath(), e.getMessage()));
        }

    }


    public void saveUserSettings() {

        if (userSettings != null) {
            try {
                UserSettingsHandler handler = new UserSettingsHandler();
                handler.SavePreferences(userSettings);
            } catch (Exception e) {
                logger.warn("Failed to save user userSettings!");
            }
        }
    }

    private void loadUserSettings() {
        try {
            UserSettingsHandler handler = new UserSettingsHandler();
            if (handler.SettingsFileIsAvailable()) {
                userSettings = handler.LoadPreferences();
            } else {
                userSettings = new UserSettings();
            }
        } catch (Exception e) {
            logger.warn("Failed to loader user userSettings!");
        }
    }

    private void loadTreeViewData() {


        TreeBuilder builder = new TreeBuilder();
        TreeItem rootNode = builder.buildTreeStructure(configuration);

        treeView.setRoot(rootNode);
        treeView.setShowRoot(true);
        rootNode.setExpanded(true);
    }

}
