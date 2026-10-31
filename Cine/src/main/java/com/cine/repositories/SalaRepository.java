package com.cine.repositories;
import com.cine.models.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface SalaRepository extends JpaRepository<Sala, Long> {
    Optional<Sala> findByNumero(Integer numero);
}
