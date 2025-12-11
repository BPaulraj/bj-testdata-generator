import java.util.*;
import java.io.IOException;

@SuppressWarnings("unchecked")
public class GenerateSyntheticData {
    public static void main(String[] args) throws IOException {
        String accountConfig = "src/test/resources/account-fields.yaml";
        String cashConfig = "src/test/resources/cash-fields.yaml";
        String accountOut = "target/account-data.csv";
        String cashOut = "target/cash-data.csv";
        String stockConfig = "src/test/resources/stock-fields.yaml";
        String stockOut = "target/stock-data.csv";

        // Generate data
        Map<String, Object> data = SyntheticDataUtil.generateAccountAndCashRecords(accountConfig, cashConfig);
        List<Map<String, String>> accountRecords = (List<Map<String, String>>) data.get("account");
        List<Map<String, String>> cashRecords = (List<Map<String, String>>) data.get("cash");

        // Write account CSV
        if (!accountRecords.isEmpty()) {
            SyntheticFieldConfig accountFieldConfig = SyntheticConfigReader.readConfig(accountConfig);
            List<String> accountHeaders = new ArrayList<>();
            for (SyntheticFieldConfig.Field field : accountFieldConfig.fields) {
                accountHeaders.add(field.name);
            }
            SyntheticCSVWriter.writeCsv(accountOut, accountHeaders, accountRecords);
        }

        // Write cash CSV
        if (!cashRecords.isEmpty()) {
            SyntheticFieldConfig cashFieldConfig = SyntheticConfigReader.readConfig(cashConfig);
            List<String> cashHeaders = new ArrayList<>();
            for (SyntheticFieldConfig.Field field : cashFieldConfig.fields) {
                cashHeaders.add(field.name);
            }
            SyntheticCSVWriter.writeCsv(cashOut, cashHeaders, cashRecords);
        }

        // Generate and write stock records
        List<Map<String, String>> stockRecords = SyntheticDataUtil.generateStockRecords(stockConfig, accountRecords, cashRecords);
        if (!stockRecords.isEmpty()) {
            SyntheticFieldConfig stockFieldConfig = SyntheticConfigReader.readConfig(stockConfig);
            List<String> stockHeaders = new ArrayList<>();
            for (SyntheticFieldConfig.Field field : stockFieldConfig.fields) {
                stockHeaders.add(field.name);
            }
            SyntheticCSVWriter.writeCsv(stockOut, stockHeaders, stockRecords);
        }

        System.out.println("Synthetic data generated: " + accountOut + ", " + cashOut + ", " + stockOut);
    }
}
