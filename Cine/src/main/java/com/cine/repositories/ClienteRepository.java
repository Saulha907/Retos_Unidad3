package com.cine.repositories;
import com.cine.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}
