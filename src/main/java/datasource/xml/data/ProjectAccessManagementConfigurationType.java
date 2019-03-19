package datasource.xml.data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ProjectAccessConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectAccessManagementConfigurationType {

    @XmlElementWrapper(name = "Users")
    @XmlElement(name = "User")
    private List<UserType> users;


    public List<UserType> getUsers() {
        return new ArrayList<>(users);
    }

    public void setUsers(List<UserType> users) {
        this.users = users;
    }
}
