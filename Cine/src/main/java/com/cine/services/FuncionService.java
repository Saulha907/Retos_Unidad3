package com.cine.services;
import com.cine.models.*;
import com.cine.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class FuncionService {
    private final FuncionRepository repo;
    private final PeliculaRepository peliculas;
    private final SalaRepository salas;
    public FuncionService(FuncionRepository repo, PeliculaRepository peliculas, SalaRepository salas) {
        this.repo = repo; this.peliculas = peliculas; this.salas = salas;
    }
    public List<Funcion> listar() { return repo.findAll(); }
    public Funcion obtener(Long id) { return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Función no encontrada")); }
    public Funcion programar(Long peliculaId, Long salaId, Funcion f) {
        Pelicula pelicula = peliculas.findById(peliculaId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Película no encontrada"));
        Sala sala = salas.findById(salaId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala no encontrada"));
        if (f.getFechaHora() == null || f.getPrecio() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fecha/hora y precio positivo son obligatorios");
        LocalDateTime inicio = f.getFechaHora();
        LocalDateTime fin = inicio.plusMinutes(pelicula.getDuracion());
        for (Funcion otra : repo.findBySalaId(salaId)) {
            LocalDateTime otroInicio = otra.getFechaHora();
            LocalDateTime otroFin = otroInicio.plusMinutes(otra.getPelicula().getDuracion());
            if (inicio.isBefore(otroFin) && otroInicio.isBefore(fin))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Horario ocupado en la sala");
        }
        f.setId(null); f.setPelicula(pelicula); f.setSala(sala);
        return repo.save(f);
    }
}
