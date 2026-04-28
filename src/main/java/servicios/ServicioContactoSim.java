package servicios;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ServicioContactoSim implements InterfazContactoSim {
    private List<DatosSolicitud> solicitudesTemporales = new ArrayList<>();
    private Random random = new Random();

    @Override
    public int solicitarSimulation(modelo.DatosSolicitud sol) {
        try {
            // En lugar de inventar un token, se lo pedimos al servidor externo
            api.ApiServiceConsumer consumer = new api.ApiServiceConsumer();
            int tokenReal = consumer.enviarSolicitud(sol);

            System.out.println("Token real recibido del servidor: " + tokenReal);
            return tokenReal;
        } catch (Exception e) {
            System.err.println("Error al contactar con el servidor de simulación: " + e.getMessage());
            return -1; // Devolvemos -1 para que el SolicitudController sepa que hubo un error
        }
    }

    @Override
    public DatosSimulation descargarDatos(int ticket) {
        try {
            api.ApiServiceConsumer consumer = new api.ApiServiceConsumer();
            api.ResultsResponseDTO response = consumer.descargarResultados(ticket);

            if (response.done && response.data != null) {
                return procesarRespuestaTexto(response.data);
            }
        } catch (Exception e) {
            System.err.println("Error al descargar datos: " + e.getMessage());
        }
        return new DatosSimulation();
    }

    @Override
    public List<Entidad> getEntities() {
        Entidad e1 = new Entidad();
        e1.setId(1);
        e1.setName("Planta Fotovoltaica A1");
        e1.setDescripcion("Ubicada en el sector norte del complejo");

        Entidad e2 = new Entidad();
        e2.setId(2);
        e2.setName("Aerogenerador E-44");
        e2.setDescripcion("Turbina de alta eficiencia");

        return List.of(e1, e2);
    }

    @Override
    public boolean isValidEntityId(int id) {
        return id > 0;
    }

    // Metodo para transformar el String del servicio en el objeto DatosSimulation
    private DatosSimulation procesarRespuestaTexto(String rawData) {
        DatosSimulation sim = new DatosSimulation();
        java.util.Map<Integer, List<modelo.Punto>> mapaPuntos = new java.util.HashMap<>();

        String[] lineas = rawData.split("\\r?\\n");
        if (lineas.length == 0) return sim;

        // La primera línea es el ancho del tablero
        sim.setAnchoTablero(Integer.parseInt(lineas[0].trim()));

        int maxT = 0;
        // Procesar cada línea de punto: tiempo, y, x, color
        for (int i = 1; i < lineas.length; i++) {
            String[] partes = lineas[i].split(",");
            if (partes.length < 4) continue;

            int t = Integer.parseInt(partes[0].trim());
            int y = Integer.parseInt(partes[1].trim());
            int x = Integer.parseInt(partes[2].trim());
            String color = partes[3].trim();

            modelo.Punto p = new modelo.Punto();
            p.setX(x);
            p.setY(y);
            p.setColor(color);

            // Organizar puntos por tiempo en el Mapa
            mapaPuntos.computeIfAbsent(t, k -> new java.util.ArrayList<>()).add(p);
            if (t > maxT) maxT = t;
        }

        sim.setPuntos(mapaPuntos);
        sim.setMaxSegundos(maxT);
        return sim;
    }
}
