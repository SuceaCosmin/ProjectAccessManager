package controller.builder;

import core.data.Configuration;
import core.data.User;
import javafx.scene.control.TreeItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TreeBuilder {

    public static final String PROJECTS = "Projects";
    public static final String USERS = "User";


    private static final Logger logger = LogManager.getLogger(TreeBuilder.class);

    public TreeItem buildTreeStructure(Configuration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException();
        }

        TreeItem root = new TreeItem<String>("Root");
        TreeItem projects = new TreeItem<String>(PROJECTS);
        root.getChildren().add(projects);
        TreeItem users = buildUsersNode(configuration.getUserList());
        root.getChildren().add(users);

        return root;
    }

    private TreeItem buildUsersNode(List<User> userList) {

        TreeItem item = new TreeItem<>(USERS);

        for (User user : userList) {
            try {
                TreeItem userNode = new TreeItem<String>();
                userNode.setValue(user.getName());
                item.getChildren().add(userNode);
            } catch (Exception e) {
                logger.warn(String.format("Failed to build Tree Node for user  %s due to: %s", user.getName(), e.getMessage()));
            }
        }
        return item;
    }

}
