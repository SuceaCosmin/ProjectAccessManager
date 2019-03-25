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

public class MainWindow {

    private static Logger logger = LogManager.getLogger(MainWindow.class);

    public BorderPane mainPanel;

    public TreeView treeView;

    public Menu recentFiles;
    public Pane contentPanel;

    private Configuration configuration;
    private UserSettings userSettings;

    private PacConfigurationHandler handler;


    public MainWindow() {
        configuration = new Configuration();
    }

    @FXML
    private void initialize() {

        loadUserSettings();

        if (userSettings != null) {
            configureRecentFiles();

        }


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

    @FXML
    public void newConfiguration() {

        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Creating a new configuration will result in loosing all previous configured data!");
            alert.setContentText("Would you like to proceed?");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    configuration = new Configuration();
                    logger.warn("A new configuration has been created!(Previous data has been lost!)");
                }
            });
        } catch (Exception e) {
            logger.warn("Exception occurred while creating a new configuration: " + e.getMessage());
        }

    }

    @FXML
    private void openConfiguration() {
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

    private void openConfiguration(File file) {
        try {
            handler = new PacConfigurationHandler();
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
    private void SaveConfiguration() {
        try {
            UserEntryMapper mapper = new UserEntryMapper();
            List<UserType> users = mapper.mapToDataSourceType(configuration.getUserList());
            ProjectAccessManagementConfigurationType config = new ProjectAccessManagementConfigurationType();
            config.setUsers(users);
            handler.saveConfiguration(config);
        } catch (Exception e) {
            logger.warn(String.format("Failed to save configuration to %s due to : %s " + handler.getConfigurationPath(), e.getMessage()));
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
            handle.saveConfigurationAs(config, file);

        } catch (Exception e) {
            logger.warn(String.format("Failed to Store configuration to system(%s) due to:%s", file.getPath(), e.getMessage()));
        }

    }

    @FXML
    private void closeAction() {
        Platform.exit();
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
                UserList controller = loader.getController();
                controller.setConfiguration(configuration);
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

    /**
     * Method use to configured the last opened configuration under "Recent Files" menu tab.
     */
    private void configureRecentFiles() {
        try {
            for (String path : userSettings.RecentlyOpenedFiles()) {
                MenuItem item = new MenuItem();
                item.setText(path);
                item.setOnAction(event -> {
                    openConfiguration(new File(path));
                });

                recentFiles.getItems().add(item);
            }
            recentFiles.setDisable(recentFiles.getItems().isEmpty());
        } catch (Exception e) {
            logger.warn("Failed to load recent files!");
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
