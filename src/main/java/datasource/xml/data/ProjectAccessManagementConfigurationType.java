package datasource.xml.data;

import core.data.User;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlType(name = "ProjectAccessManagementConfiguration")
public class ProjectAccessManagementConfigurationType {

    @XmlElementWrapper(name = "Users")
    private List<User> users;

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
