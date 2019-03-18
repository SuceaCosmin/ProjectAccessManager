package core.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

    private int id;
    private StringProperty name;
    private IntegerProperty age;

    public User() {
        name = new SimpleStringProperty();
        name.setValue("");

        age = new SimpleIntegerProperty();
        age.setValue(0);

    }

    public User(String name, int age) {
        this.name = new SimpleStringProperty();
        this.name.setValue(name);

        this.age = new SimpleIntegerProperty();
        this.age.setValue(age);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getAge() {
        return age.getValue();
    }

    public void setAge(int age) {
        this.age.setValue(age);
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
