package core.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private ObservableList<User> userList;

    public Configuration() {
        userList = FXCollections.observableArrayList();
    }

    public void AddUser(User user) {
        if (userList == null) {
            userList = FXCollections.observableArrayList();
        }
        userList.add(user);

    }

    public void AddAllUsers(List<User> list) {
        if (userList == null) {
            userList = FXCollections.observableArrayList();
        }
        userList.addAll(list);

    }

    public ObservableList<User> ObservableUserList() {
        return userList;
    }

    public List<User> getUserList() {
        if (userList == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(userList);
    }

}
