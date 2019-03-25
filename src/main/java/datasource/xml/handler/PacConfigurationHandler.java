package datasource.xml.handler;

import datasource.xml.data.ProjectAccessManagementConfigurationType;
import datasource.xml.exception.PacConfigurationLoadingFailedException;
import datasource.xml.exception.PacConfigurationSavingFailedException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class PacConfigurationHandler {

    private File configurationPath;

    public String getConfigurationPath() {
        return configurationPath.getPath();
    }

    public ProjectAccessManagementConfigurationType loadConfiguration(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(ProjectAccessManagementConfigurationType.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            ProjectAccessManagementConfigurationType config = (ProjectAccessManagementConfigurationType) unmarshaller.unmarshal(file);
            configurationPath = file;
            return config;
        } catch (Exception e) {
            throw new PacConfigurationLoadingFailedException(e.getMessage());
        }
    }

    /**
     * Method used to save a configuration in the same file from which it was initially importer.
     *
     * @param config
     */
    public void saveConfiguration(ProjectAccessManagementConfigurationType config) {

        if (configurationPath == null) {
            throw new IllegalArgumentException("Configuration path is null!");
        }
        saveConfigurationAs(config, configurationPath);
    }

    public void saveConfigurationAs(ProjectAccessManagementConfigurationType config, File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            JAXBContext context = JAXBContext.newInstance(ProjectAccessManagementConfigurationType.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(config, file);
        } catch (Exception e) {
            throw new PacConfigurationSavingFailedException(e.getMessage());
        }
    }

}
