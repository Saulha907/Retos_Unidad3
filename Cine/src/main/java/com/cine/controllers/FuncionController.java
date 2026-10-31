package com.cine.controllers;
import com.cine.models.Funcion;
import com.cine.services.FuncionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/funciones")
public class FuncionController {
    private final FuncionService service;
    public FuncionController(FuncionService service) { this.service = service; }
    @GetMapping public List<Funcion> listar() { return service.listar(); }
    @GetMapping("/{id}") public Funcion obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping public Funcion programar(@RequestParam Long peliculaId, @RequestParam Long salaId, @RequestBody Funcion funcion) {
        return service.programar(peliculaId, salaId, funcion);
    }
}
