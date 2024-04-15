import modelo.Conversion;

import java.util.ArrayList;
import java.util.List;

public class ConversionHistory {
    private static final List<Conversion> conversionList = new ArrayList<>();

    public static void addConversion(Conversion conversion) {
        conversionList.add(conversion);
    }

    public void displayConversions() {
        System.out.println("Conversion history:");
        for (int i = 0; i < conversionList.size(); i++) {
            Conversion conversion = conversionList.get(i);
            System.out.println((i+1) + ". " + conversion.getFormattedDateTime() + " - "
                    + conversion.getFromCurrency() + " to " + conversion.getToCurrency() + ": "
                    + conversion.getValueToConvert() + " at a rate of " + conversion.getConversionRate() + " = "
                    + conversion.getConvertedValue());
        }
    }
}
