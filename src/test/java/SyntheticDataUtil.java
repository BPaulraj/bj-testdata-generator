import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class SyntheticDataUtil {
    private static final Faker faker = new Faker();

    /**
     * Generate stock records, sharing TransactionRef and Identifier with cash/account if provided.
     */
    public static List<Map<String, String>> generateStockRecords(String stockConfigPath, List<Map<String, String>> accountRecords, List<Map<String, String>> cashRecords) {
        SyntheticFieldConfig stockConfig = SyntheticConfigReader.readConfig(stockConfigPath);
        List<Map<String, String>> stockRecords = new java.util.ArrayList<>();
        int masterQuantity = stockConfig.masterQuantity;
        int recordCount = stockConfig.recordCount;
        int[] quantities = new int[recordCount];
        int remaining = masterQuantity;
        // Generate random integer quantities that sum to masterQuantity
        for (int i = 0; i < recordCount; i++) {
            if (i == recordCount - 1) {
                quantities[i] = remaining;
            } else {
                int max = remaining - (recordCount - i - 1); // ensure at least 1 for each remaining record
                int qty = faker.number().numberBetween(1, Math.max(2, max));
                quantities[i] = qty;
                remaining -= qty;
            }
        }
        for (int i = 0; i < recordCount; i++) {
            Map<String, String> stockData = new java.util.HashMap<>();
            for (SyntheticFieldConfig.Field field : stockConfig.fields) {
                String value;
                if (field.name.equalsIgnoreCase("Identifier") && accountRecords != null && !accountRecords.isEmpty()) {
                    value = accountRecords.get(i % accountRecords.size()).get("Identifier");
                } else if (field.name.equalsIgnoreCase("TransactionRef")) {
                    if (field.values != null && !field.values.isEmpty()) {
                        value = field.values.get(0); // Use the single configured value
                    } else if (cashRecords != null && !cashRecords.isEmpty()) {
                        value = cashRecords.get(i % cashRecords.size()).get("TransactionRef");
                    } else {
                        value = "EQ" + String.format("%06d", faker.number().numberBetween(100000, 999999));
                    }
                } else if (field.name.equalsIgnoreCase("quantity")) {
                    value = String.valueOf(quantities[i]);
                } else {
                    switch (field.format) {
                        case "plain":
                            value = faker.lorem().word();
                            break;
                        case "alphanumeric":
                            value = faker.bothify("??????###");
                            break;
                        case "integer":
                            value = String.valueOf(quantities[i]);
                            break;
                        default:
                            value = "";
                    }
                }
                stockData.put(field.name, value);
            }
            stockRecords.add(stockData);
        }
        return stockRecords;
    }

    /**
     * Generate multiple account records and corresponding cash records per account, based on recordCount in config.
     */
    public static Map<String, Object> generateAccountAndCashRecords(String accountConfigPath, String cashConfigPath) {
        Map<String, Object> result = new HashMap<>();
        SyntheticFieldConfig accountConfig = SyntheticConfigReader.readConfig(accountConfigPath);
        SyntheticFieldConfig cashConfig = SyntheticConfigReader.readConfig(cashConfigPath);

        java.util.List<Map<String, String>> accountRecords = new java.util.ArrayList<>();
        java.util.List<Map<String, String>> cashRecords = new java.util.ArrayList<>();

        for (int i = 0; i < accountConfig.recordCount; i++) {
            Map<String, String> accountData = new HashMap<>();
            for (SyntheticFieldConfig.Field field : accountConfig.fields) {
                String value;
                switch (field.format) {
                    case "plain":
                        if (field.name.equalsIgnoreCase("Firstname")) {
                            value = faker.name().firstName();
                        } else if (field.name.equalsIgnoreCase("Surname")) {
                            value = faker.name().lastName();
                        } else {
                            value = faker.lorem().word();
                        }
                        break;
                    case "date":
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
                        value = sdf.format(faker.date().birthday());
                        break;
                    case "country":
                        String[] europeanCountries = {
                            "Austria", "Belgium", "Croatia", "Czech Republic", "Denmark", "Estonia", "Finland", "France", "Germany", "Greece", "Hungary", "Iceland", "Ireland", "Italy", "Latvia", "Lithuania", "Luxembourg", "Netherlands", "Norway", "Poland", "Portugal", "Romania", "Slovakia", "Slovenia", "Spain", "Sweden", "Switzerland", "United Kingdom"
                        };
                        value = europeanCountries[faker.random().nextInt(europeanCountries.length)];
                        break;
                    case "postcode":
                        value = faker.address().zipCode();
                        break;
                    case "identifier":
                        value = faker.bothify("###########");
                        break;
                    case "holderType":
                        if (field.values != null && !field.values.isEmpty()) {
                            value = field.values.get(faker.random().nextInt(field.values.size()));
                        } else {
                            String[] types = {"H","S","D","C"};
                            value = types[faker.random().nextInt(types.length)];
                        }
                        break;
                    case "paymentType":
                        if (field.values != null && !field.values.isEmpty()) {
                            value = field.values.get(faker.random().nextInt(field.values.size()));
                        } else {
                            value = faker.lorem().word();
                        }
                        break;
                    default:
                        value = "";
                }
                accountData.put(field.name, value);
            }
            accountRecords.add(accountData);
            for (String channel : cashConfig.channels) {
                Map<String, String> cashData = new HashMap<>();
                for (SyntheticFieldConfig.Field field : cashConfig.fields) {
                    String value;
                    switch (field.format) {
                        case "plain":
                            value = faker.lorem().word();
                            break;
                        case "decimal6":
                            value = String.format("%.6f", faker.number().randomDouble(6, 1, 100000));
                            break;
                        case "alphanumeric":
                            value = faker.bothify("??????###");
                            break;
                        case "TransactionRef":
                            if (field.values != null && !field.values.isEmpty()) {
                                value = field.values.get(0); // Use the single configured value
                            } else {
                                value = "EQ" + String.format("%06d", faker.number().numberBetween(100000, 999999));
                            }
                            break;
                        case "paymentType":
                            if (field.values != null && !field.values.isEmpty()) {
                                value = field.values.get(faker.random().nextInt(field.values.size()));
                            } else {
                                value = faker.lorem().word();
                            }
                            break;
                        default:
                            value = "";
                    }
                    if (field.name.equalsIgnoreCase("Identifier")) {
                        value = accountData.get("Identifier");
                    } else if (field.name.equalsIgnoreCase("Paymentchannel")) {
                        value = channel;
                    }
                    cashData.put(field.name, value);
                }
                cashRecords.add(cashData);
            }
        }
        result.put("account", accountRecords);
        result.put("cash", cashRecords);
        return result;
    }

    public static Map<String, Map<String, String>> generateDataFromConfigs(String[] configPaths) {
        Map<String, Map<String, String>> allData = new HashMap<>();
        Map<String, String> sharedValues = new HashMap<>();
        for (String configPath : configPaths) {
            SyntheticFieldConfig config = SyntheticConfigReader.readConfig(configPath);
            Map<String, String> data = new HashMap<>();
            for (SyntheticFieldConfig.Field field : config.fields) {
                String value;
                if (sharedValues.containsKey(field.name)) {
                    value = sharedValues.get(field.name);
                } else {
                    switch (field.format) {
                        case "plain":
                            if (field.name.equalsIgnoreCase("fullName")) {
                                value = faker.name().fullName();
                            } else if (field.name.equalsIgnoreCase("Firstname")) {
                                value = faker.name().firstName();
                            } else if (field.name.equalsIgnoreCase("Surname")) {
                                value = faker.name().lastName();
                            } else if (field.name.equalsIgnoreCase("address") || field.name.equalsIgnoreCase("Address1") || field.name.equalsIgnoreCase("Address2")) {
                                value = faker.address().streetAddress();
                            } else {
                                value = faker.lorem().word();
                            }
                            break;
                        case "email":
                            value = faker.internet().emailAddress();
                            break;
                        case "phone":
                            value = faker.phoneNumber().cellPhone();
                            break;
                        case "date":
                            value = faker.date().birthday().toString();
                            break;
                        case "country":
                            value = faker.address().country();
                            break;
                        case "postcode":
                            value = faker.address().zipCode();
                            break;
                        default:
                            value = "";
                    }
                    sharedValues.put(field.name, value);
                }
                data.put(field.name, value);
            }
            allData.put(configPath, data);
        }
        return allData;
    }

    // Backward compatible single config method
    public static Map<String, String> generateDataFromConfig(String configPath) {
        return generateDataFromConfigs(new String[]{configPath}).get(configPath);
    }
}
