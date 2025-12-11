import java.util.List;

import java.util.ArrayList;

public class SyntheticFieldConfig {
    public int recordCount = 1;
    public int masterQuantity = 0;
    public List<Field> fields;
    // Optional: for cash config
    public List<String> channels = new ArrayList<>();

    public static class Field {
        public String name;
        public String type;
        public boolean mandatory;
        public String format;
        public java.util.List<String> values = new java.util.ArrayList<>();
    }
}
