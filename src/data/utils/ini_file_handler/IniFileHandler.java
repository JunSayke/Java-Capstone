package src.data.utils.ini_file_handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class IniFileHandler {
    protected final String filePath;
    protected final Map<String, Map<String, String>> sections;

    public IniFileHandler(String filePath) {
        this.filePath = filePath;
        this.sections = new HashMap<>();
    }

    public abstract void processFile() throws IOException;

    public void setProperty(String key, String value) {
        setProperty("", key, value);
    }

    public void setProperty(String section, String key, String value) {
        sections.computeIfAbsent(section, k -> new HashMap<>()).put(key, value);
    }

    public String getProperty(String key) {
        return getProperty("", key);
    }

    public String getProperty(String section, String key) {
        Map<String, String> sectionProperties = sections.get(section);
        return (sectionProperties != null) ? sectionProperties.get(key) : null;
    }

    public Map<String, Map<String, String>> getContent() {
        return sections;
    }

    public Map<String, String> getSection(String section) {
        return sections.get(section);
    }

}
