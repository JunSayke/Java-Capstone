package src.data.utils.ini_file_handler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IniFileReader {
    private final String filePath;
    private final Map<String, String> properties;

    public IniFileReader(String filePath) {
        this.filePath = filePath;
        this.properties = new HashMap<>();
    }

    public void read() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignore comments and empty lines
                if (!line.trim().startsWith(";") && !line.trim().isEmpty()) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        properties.put(key, value);
                    }
                }
            }
        }
    }

    public String getProperty(String key) {
        return properties.get(key);
    }
}
