package com.cine.controllers;
import com.cine.models.Boleto;
import com.cine.services.BoletoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/boletos")
public class BoletoController {
    private final BoletoService service;
    public BoletoController(BoletoService service) { this.service = service; }
    @GetMapping("/funcion/{funcionId}") public List<Boleto> porFuncion(@PathVariable Long funcionId) { return service.porFuncion(funcionId); }
    @PostMapping public Boleto comprar(@RequestParam Long funcionId, @RequestParam(required = false) Long clienteId,
                                       @RequestParam String fila, @RequestParam int numero) {
        return service.comprar(funcionId, clienteId, fila, numero);
    }
}
