package modelo;

import java.io.Serializable;

public class Destinatario implements Serializable {

    private String nombre;
    private String direccionEmail;

    public Destinatario() {}

    public Destinatario(String nombre, String direccionEmail) {
        this.nombre = nombre;
        this.direccionEmail = direccionEmail;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccionEmail() { return direccionEmail; }
    public void setDireccionEmail(String direccionEmail) { this.direccionEmail = direccionEmail; }
}
