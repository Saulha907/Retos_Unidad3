package com.cine.controllers;
import com.cine.models.Sala;
import com.cine.repositories.SalaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@RestController @RequestMapping("/salas")
public class SalaController {
    private final SalaRepository repo;
    public SalaController(SalaRepository repo) { this.repo = repo; }
    @GetMapping public List<Sala> listar() { return repo.findAll(); }
    @PostMapping public Sala crear(@RequestBody Sala sala) {
        if (sala.getNumero() == null || sala.getNumero() < 1 || sala.getNumero() > 6 || sala.getCapacidad() == null || sala.getCapacidad() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cine tiene salas del 1 al 6 y capacidad positiva");
        if (repo.findByNumero(sala.getNumero()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El número de sala ya existe");
        sala.setId(null); return repo.save(sala);
    }
}
