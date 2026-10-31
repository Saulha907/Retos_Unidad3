package com.cine.services;
import com.cine.models.*;
import com.cine.repositories.PeliculaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service
public class PeliculaService {
    private final PeliculaRepository repo;
    public PeliculaService(PeliculaRepository repo) { this.repo = repo; }
    public List<Pelicula> listar() { return repo.findAll(); }
    public Pelicula obtener(Long id) { return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Película no encontrada")); }
    public List<Pelicula> porClasificacion(Clasificacion clasificacion) { return repo.findByClasificacion(clasificacion); }
    public Pelicula crear(Pelicula p) {
        if (p.getTitulo() == null || p.getTitulo().isBlank() || p.getClasificacion() == null || p.getDuracion() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Título, clasificación y duración válida son obligatorios");
        p.setId(null);
        return repo.save(p);
    }
}
