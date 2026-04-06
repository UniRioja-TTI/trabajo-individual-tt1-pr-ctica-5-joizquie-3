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
                .POST(HttpRequest.BodyPublishers.noBody()) // El JSON marca POST
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), ResultsResponseDTO.class);
    }
}
