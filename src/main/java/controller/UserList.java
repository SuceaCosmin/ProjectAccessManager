package controller;

import core.data.Configuration;
import core.data.User;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserList {

    private static final Logger logger = LogManager.getLogger(UserList.class);

    private Configuration configuration;
    @FXML
    private BorderPane mainPanel;
    @FXML
    private TableView<User> userTable;

    public UserList() {
    }

    @FXML
    public void initialize() {
        renderTableColumns();
    }

    @FXML
    private void AddUser() {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainPanel.getScene().getWindow());

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/CreateUser.fxml"));
            dialog.getDialogPane().setContent(loader.load());

            dialog.setTitle("Add User");

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
                } else if (result.get() == ButtonType.CANCEL) {
                    logger.debug("Creating new user operation canceled..");
                }
            }

        } catch (Exception e) {
            logger.warn("Failed to load AddUser view due to: " + e.getMessage());
            logger.debug(e);
        }
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        bindInformation();
    }

    private void bindInformation() {
        userTable.setItems(configuration.ObservableUserList());
    }

    private void renderTableColumns() {

        TableColumn<User, Integer> indexColumn = new TableColumn<>("No.");
        indexColumn.setCellValueFactory(param -> {
            int userIntex = configuration.ObservableUserList().indexOf(param.getValue());

            return new ObservableValue<Integer>() {
                @Override
                public void addListener(ChangeListener<? super Integer> listener) {

                }

                @Override
                public void removeListener(ChangeListener<? super Integer> listener) {

                }

                @Override
                public Integer getValue() {
                    return userIntex;
                }

                @Override
                public void addListener(InvalidationListener listener) {

                }

                @Override
                public void removeListener(InvalidationListener listener) {

                }
            };
        });

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        userTable.getColumns().addAll(indexColumn, nameColumn, ageColumn);
    }
}
