import com.google.gson.Gson;
import com.google.gson.JsonObject;
import modelo.Conversion;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Gson gson = new Gson();
    private static final HttpClient client = HttpClient.newHttpClient();
    static ConversionHistory conversionHistory = new ConversionHistory();

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean continueConverting = true;

        while (continueConverting) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Convertir monedas");
            System.out.println("2. Historial de conversiones");
            System.out.println("3. Salir");
            System.out.print("Opción: ");

            try{
                int option = scanner.nextInt();

                switch (option) {
                    case 1:
                        // Mostrar la lista de monedas disponibles al inicio de la opción "Convertir monedas"
                        System.out.println("Monedas disponibles:");
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("https://v6.exchangerate-api.com/v6/168cde6f92b71b19581c146b/latest/USD"))
                                .build();

                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);

                        JsonObject conversionRates = jsonObject.get("conversion_rates").getAsJsonObject();
                        List<String> currencies = new ArrayList<>(conversionRates.keySet());

                        for (int i = 0; i < currencies.size(); i++) {
                            System.out.print(currencies.get(i) + " ");
                            if ((i + 1) % 30 == 0) {
                                System.out.println();
                            }
                        }
                        System.out.println();

                        try{
                            System.out.println("Ingrese la moneda de origen:");
                            String fromCurrency = scanner.next();
                            System.out.println("Ingrese la moneda de destino:");
                            String toCurrency = scanner.next();
                            convertCurrency(fromCurrency, toCurrency, scanner);
                        }catch (Exception e){
                            System.out.println("Ingrese monedas válidas");
                        }

                        break;
                    case 2:
                        conversionHistory.displayConversions();
                        System.out.println("Presione cualquier tecla para regresar al menu principal.");
                        System.in.read();
                        break;
                    case 3:
                        continueConverting = false;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            }catch (InputMismatchException ex) {
                System.out.println("Por favor, ingrese una opción válida.");
                scanner.nextLine();
            }

        }

        System.out.println("¡Hasta luego!");
    }

    private static void convertCurrency(String fromCurrency, String toCurrency, Scanner scanner) throws IOException, InterruptedException {
        // Save the selected destination currency
        if (!toCurrency.isEmpty()) {
            // Use the selected destination currency for the conversion
            System.out.println("Ingrese el monto a convertir:");
            try {
                double amount = scanner.nextDouble();

                // Realizar la solicitud HTTP
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://v6.exchangerate-api.com/v6/168cde6f92b71b19581c146b/pair/" + fromCurrency + "/" + toCurrency + "/" + amount))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);

                // Obtener el resultado de la conversión
                double conversionRate = jsonObject.get("conversion_rate").getAsDouble();
                double conversionResult = jsonObject.get("conversion_result").getAsDouble();

                // Imprimir el resultado de la conversión
                System.out.println("La tasa de conversión de " + fromCurrency + " a " + toCurrency + " es: " + conversionRate);
                System.out.println("El resultado de convertir "+ amount + " " + fromCurrency + " a " + toCurrency + " es: " + conversionResult);
                System.out.println("Presione cualquier tecla para regresar al menu principal.");

                // Agregar la conversión a la historia
                registerConversion(fromCurrency, toCurrency, conversionRate, amount, conversionResult);
            }catch (Exception e){
                System.out.println("Ingrese un monto válido");
            }

            // Wait for the user to press enter to continue
            System.in.read();
        }
    }

    private static void registerConversion(String fromCurrency, String toCurrency, double conversionRate, double valueToConvert, double convertedValue) {
        String date = formatedDate();
        Conversion conversion = new Conversion(fromCurrency,toCurrency,conversionRate,date,valueToConvert,convertedValue);
        ConversionHistory.addConversion(conversion);
    }

    private static String formatedDate(){
        // Crear una instancia de LocalDateTime con la fecha y hora actual
        LocalDateTime now = LocalDateTime.now();

        // Formatear la fecha y hora en un formato de cadena
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

}