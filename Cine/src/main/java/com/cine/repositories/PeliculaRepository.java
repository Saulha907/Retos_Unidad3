package com.cine.repositories;
import com.cine.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    List<Pelicula> findByGenero(String genero);
    List<Pelicula> findByClasificacion(Clasificacion clasificacion);
}
