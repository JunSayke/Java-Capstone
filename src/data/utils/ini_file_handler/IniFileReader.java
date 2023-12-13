package src.data.utils.ini_file_handler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IniFileReader extends IniFileHandler {
    public IniFileReader(String filePath) {
        super(filePath);
    }

    @Override
    public void processFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentSection = null;
            Map<String, String> currentProperties = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                // Ignore comments and empty lines
                if (!line.trim().startsWith(";") && !line.trim().isEmpty()) {
                    if (line.trim().startsWith("[")) {
                        // New section
                        currentSection = line.trim().substring(1, line.trim().indexOf("]"));
                        currentProperties = new HashMap<>();
                        sections.put(currentSection, currentProperties);
                    } else {
                        // Property within the current section
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();
                            if (currentSection != null) {
                                currentProperties.put(key, value);
                            } else {
                                // No section specified, consider it a global property
                                sections.computeIfAbsent("", k -> new HashMap<>()).put(key, value);
                            }
                        }
                    }
                }
            }
        }
    }
}
