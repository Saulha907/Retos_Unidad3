package com.cine.repositories;
import com.cine.models.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FuncionRepository extends JpaRepository<Funcion, Long> {
    List<Funcion> findByPeliculaId(Long peliculaId);
    List<Funcion> findBySalaId(Long salaId);
}
