package com.cine.services;
import com.cine.models.Cliente;
import com.cine.repositories.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service
public class ClienteService {
    private final ClienteRepository repo;
    public ClienteService(ClienteRepository repo) { this.repo = repo; }
    public List<Cliente> listar() { return repo.findAll(); }
    public Cliente registrar(Cliente c) {
        if (c.getNombre() == null || c.getNombre().isBlank() || c.getEmail() == null || c.getEmail().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre y correo son obligatorios");
        if (repo.findByEmail(c.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Correo registrado previamente");
        c.setId(null); c.setRegistrado(true); c.setPuntos(0);
        return repo.save(c);
    }
}
