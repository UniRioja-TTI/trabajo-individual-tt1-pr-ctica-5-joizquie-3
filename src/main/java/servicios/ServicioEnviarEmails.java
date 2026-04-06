package servicios;

import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ServicioEnviarEmails implements InterfazEnviarEmails {
    private final Logger logger;

    public ServicioEnviarEmails(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean enviarEmail(Destinatario dest, String email) {
        logger.info("LOG DE EMAIL - Destinatario: {} | Contenido: {}", dest.getNombre(), email);
        return true;
    }
}
