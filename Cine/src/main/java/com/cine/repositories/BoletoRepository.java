package com.cine.repositories;
import com.cine.models.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BoletoRepository extends JpaRepository<Boleto, Long> {
    List<Boleto> findByFuncionId(Long funcionId);
    boolean existsByFuncionIdAndFilaIgnoreCaseAndNumero(Long funcionId, String fila, int numero);
}
