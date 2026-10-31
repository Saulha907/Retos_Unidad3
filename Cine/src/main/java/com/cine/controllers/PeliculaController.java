package com.cine.controllers;
import com.cine.models.*;
import com.cine.services.PeliculaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/peliculas")
public class PeliculaController {
    private final PeliculaService service;
    public PeliculaController(PeliculaService service) { this.service = service; }
    @GetMapping public List<Pelicula> listar(@RequestParam(required = false) Clasificacion clasificacion) {
        return clasificacion == null ? service.listar() : service.porClasificacion(clasificacion);
    }
    @GetMapping("/{id}") public Pelicula obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping public Pelicula crear(@RequestBody Pelicula p) { return service.crear(p); }
}
