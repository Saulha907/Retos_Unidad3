package com.cine.controllers;
import com.cine.models.Cliente;
import com.cine.services.ClienteService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService service;
    public ClienteController(ClienteService service) { this.service = service; }
    @GetMapping public List<Cliente> listar() { return service.listar(); }
    @PostMapping public Cliente registrar(@RequestBody Cliente cliente) { return service.registrar(cliente); }
}
