import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;

public class SyntheticConfigReader {
    public static SyntheticFieldConfig readConfig(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(new File(filePath), SyntheticFieldConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read config: " + e.getMessage(), e);
        }
    }
}
