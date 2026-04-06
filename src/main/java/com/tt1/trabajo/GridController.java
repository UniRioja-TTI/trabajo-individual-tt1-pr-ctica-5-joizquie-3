package com.tt1.trabajo;

import org.slf4j.Logger;

import interfaces.InterfazContactoSim;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Punto;

@Controller
public class GridController {
	private final InterfazContactoSim ics;
	private final Logger logger;
	
	public GridController(InterfazContactoSim ics, Logger logger) {
		this.ics = ics;
		this.logger = logger;
	}
	
	@GetMapping("/grid")
    public String solicitud(@RequestParam int tok, Model model) {
		DatosSimulation ds = ics.descargarDatos(tok);
        model.addAttribute("count", ds.getAnchoTablero());
        model.addAttribute("maxTime", ds.getMaxSegundos());
		model.addAttribute("tok", tok); // Añadido para el span id del HTML
        Map<String, String> colors = new HashMap<>();
		// Ajuste: t <= ds.getMaxSegundos() para no perder el último fotograma
		for(var t = 0; t <= ds.getMaxSegundos(); t++) {
			List<Punto> puntosEnT = ds.getPuntos().get(t);
			// Ajuste esencial: verificar que la lista no sea nula antes de iterar
			if (puntosEnT != null) {
				for(Punto p : puntosEnT) {
					colors.put(t + "-" + p.getY() + "-" + p.getX(), p.getColor());
				}
			}
        }
        model.addAttribute("colors", colors);
        return "grid";
    }
}
