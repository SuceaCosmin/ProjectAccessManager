package usersettings.data;

import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "UserSettings")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserSettings {

    @XmlElement(name = "LastBrowsedFolder")
    private String lastBrowsedFolder;

    @XmlElementWrapper(name = "RecentFiles")
    @XmlElement(name = "Path")
    private List<String> recentOpenedFileList;

    public String getLastBrowsedFolder() {
        return lastBrowsedFolder;
    }

    public void setLastBrowsedFolder(String lastBrowsedFolder) {
        this.lastBrowsedFolder = lastBrowsedFolder;
    }

    public boolean isLastBrowsedFolderAvailable() {
        return lastBrowsedFolder != null && new File(lastBrowsedFolder).exists();
    }

    /**
     * Method used to add a new File to the recent opened files list.
     *
     * @param filePath represents the path of the file in the system.
     */
    public void AddRecentFile(String filePath) {
        if (recentOpenedFileList == null) {
            recentOpenedFileList = new ArrayList<>();
        }

        if (!recentOpenedFileList.contains(filePath)) {
            recentOpenedFileList.add(filePath);
        }

    }

    /**
     * Method used to access the list of Recently opened files.
     *
     * @return a list of the files
     */
    public List<String> RecentlyOpenedFiles() {
        if (recentOpenedFileList == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(recentOpenedFileList);
    }

}
