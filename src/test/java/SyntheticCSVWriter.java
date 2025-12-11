import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SyntheticCSVWriter {
    public static void writeCsv(String filePath, List<String> headers, List<Map<String, String>> records) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append(String.join(",", headers));
            writer.append("\n");
            // Write records
            for (Map<String, String> record : records) {
                for (int i = 0; i < headers.size(); i++) {
                    String value = record.getOrDefault(headers.get(i), "");
                    writer.append(value);
                    if (i < headers.size() - 1) writer.append(",");
                }
                writer.append("\n");
            }
        }
    }
}
