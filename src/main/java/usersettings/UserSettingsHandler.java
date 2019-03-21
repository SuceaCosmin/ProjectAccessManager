package usersettings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import usersettings.data.UserSettings;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class UserSettingsHandler {

    private static final Logger logger = LogManager.getLogger(UserSettingsHandler.class);

    private static final File FILE = new File("settings.xml");

    public void SavePreferences(UserSettings configuration) {

        try {
            JAXBContext context = JAXBContext.newInstance(UserSettings.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(configuration, FILE);

        } catch (Exception e) {
            logger.debug("Exception occurred while saving user preference file due to: " + e.getMessage());
            throw new IllegalArgumentException("Failed to store application settings to file!");
        }

    }

    public UserSettings LoadPreferences() {
        try {
            JAXBContext context = JAXBContext.newInstance(UserSettings.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (UserSettings) unmarshaller.unmarshal(FILE);

        } catch (Exception e) {
            logger.debug("Exception occurred while loading user preference file due to: " + e.getMessage());
            throw new IllegalArgumentException("Failed to load application settings from file!");
        }

    }

    public boolean SettingsFileIsAvailable() {
        return FILE.exists();
    }
}
