package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiServiceConsumer {
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "http://localhost:8080";
    // Cadena constante según el enunciado para esta iteración
    private static final String USUARIO_CONSTANTE = "test";

    public ResultsResponseDTO descargarResultados(int ticket) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        // Construcción de la URL con parámetros de query según el JSON
        String url = BASE_URL + "/Resultados?nombreUsuario=" + USUARIO_CONSTANTE + "&tok=" + ticket;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), ResultsResponseDTO.class);
    }

    public int enviarSolicitud(modelo.DatosSolicitud sol) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        // Convertimos el objeto DatosSolicitud a JSON automáticamente con Jackson
        String jsonBody = mapper.writeValueAsString(sol);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/solicitud"))
                .header("Content-Type", "application/json") // Importante para que el servidor entienda el JSON
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // El servidor devuelve el token como un número en texto (ej: "8772")
        return Integer.parseInt(response.body().trim());
    }
}
