package controller;

import core.data.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateUser {


    public TextField nameField;
    public TextField ageField;
    private User user;

    public CreateUser() {
        user = new User();
    }

    @FXML
    public void initialize() {

        nameField.textProperty().bindBidirectional(user.nameProperty());

    }


    public User getUser() {
        user.setAge(Integer.parseInt(ageField.getText()));
        return user;
    }
}
