package datasource.xml.mapper;

import core.data.User;
import datasource.xml.data.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class UserEntryMapper {

    private static final Logger logger = LogManager.getLogger(UserEntryMapper.class);

    public List<User> mapToCoreType(List<UserType> users) {

        List<User> userList = new ArrayList<>();
        users.forEach(user -> {
            try {
                User u = new User(user.getName(), user.getAge());
                userList.add(u);

            } catch (Exception e) {
                logger.warn("Failed to create internal representation of the user " + user.getName() + " due to: " + e.getMessage());
            }
        });
        return userList;
    }

    public List<UserType> mapToDataSourceType(List<User> users) {
        List<UserType> userList = new ArrayList<>();
        users.forEach(user -> {
            try {
                UserType u = new UserType(user.getName(), user.getAge());
                userList.add(u);
            } catch (Exception e) {
                logger.warn("Failed to create data source representation of the user " + user.getName() + " due to: " + e.getMessage());
            }
        });
        return userList;
    }
}
