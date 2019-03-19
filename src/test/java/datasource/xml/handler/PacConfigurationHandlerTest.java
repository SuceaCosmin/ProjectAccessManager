package datasource.xml.handler;

import datasource.xml.data.ProjectAccessManagementConfigurationType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.File;

public class PacConfigurationHandlerTest {

    private static final Logger logger = LogManager.getLogger(PacConfigurationHandlerTest.class);

    private static final String SAVE_LOCATION = ".\\src\\test\\resources\\datasource\\xml\\Saved_Config.xml";

    @Test
    public void loadConfiguration() {
    }

    @Test
    public void saveConfiguration() {
        File file = new File(SAVE_LOCATION);
        PacConfigurationHandler handle = new PacConfigurationHandler();
        handle.saveConfiguration(new ProjectAccessManagementConfigurationType(), file);
        logger.info("Project Access Configuration has been save successfully as xml to " + SAVE_LOCATION);
    }
}