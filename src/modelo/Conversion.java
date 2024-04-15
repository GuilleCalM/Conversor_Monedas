package modelo;

public class Conversion {
    private final String fromCurrency;
    private final String toCurrency;
    private final double conversionRate;
    private final String formattedDateTime;
    private final double valueToConvert;
    private final double convertedValue;

    public Conversion(String toCurrency, String fromCurrency, double conversionRate, String formattedDateTime, double valueToConvert, double convertedValue) {
        this.toCurrency = toCurrency;
        this.fromCurrency = fromCurrency;
        this.conversionRate = conversionRate;
        this.formattedDateTime = formattedDateTime;
        this.valueToConvert = valueToConvert;
        this.convertedValue = convertedValue;
    }

    public double getValueToConvert() {
        return valueToConvert;
    }

    public String getFormattedDateTime() {
        return formattedDateTime;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public double getConvertedValue() {
        return convertedValue;
    }
}
