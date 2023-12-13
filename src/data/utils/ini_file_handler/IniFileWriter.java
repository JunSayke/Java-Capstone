package src.data.utils.ini_file_handler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IniFileWriter extends IniFileHandler {
    public IniFileWriter(String filePath) {
        super(filePath);
    }

    @Override
    public void processFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Map<String, String>> sectionEntry : sections.entrySet()) {
                String section = sectionEntry.getKey();
                Map<String, String> sectionProperties = sectionEntry.getValue();

                if (!section.isEmpty()) {
                    writer.write("[" + section + "]");
                    writer.newLine();
                }

                for (Map.Entry<String, String> entry : sectionProperties.entrySet()) {
                    writer.write(entry.getKey() + "=" + entry.getValue());
                    writer.newLine();
                }
            }
        }
    }
}
