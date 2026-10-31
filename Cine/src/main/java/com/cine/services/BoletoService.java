package com.cine.services;
import com.cine.models.*;
import com.cine.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;
@Service
public class BoletoService {
    private final BoletoRepository boletos;
    private final FuncionRepository funciones;
    private final ClienteRepository clientes;
    public BoletoService(BoletoRepository boletos, FuncionRepository funciones, ClienteRepository clientes) {
        this.boletos = boletos; this.funciones = funciones; this.clientes = clientes;
    }
    public List<Boleto> porFuncion(Long id) { return boletos.findByFuncionId(id); }
    @Transactional
    public Boleto comprar(Long funcionId, Long clienteId, String fila, int numero) {
        Funcion funcion = funciones.findById(funcionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Función no encontrada"));
        if (fila == null || fila.isBlank() || numero <= 0 || numero > funcion.getSala().getCapacidad())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Asiento inválido o fuera de capacidad");
        String filaNormalizada = fila.strip().toUpperCase();
        if (boletos.existsByFuncionIdAndFilaIgnoreCaseAndNumero(funcionId, filaNormalizada, numero))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Asiento ya vendido para esta función");
        Boleto b = new Boleto();
        b.setFolio("B-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        b.setFuncion(funcion); b.setFila(filaNormalizada); b.setNumero(numero);
        if (clienteId != null) {
            Cliente cliente = clientes.findById(clienteId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
            b.setCliente(cliente);
            if (cliente.isRegistrado()) {
                cliente.setPuntos(cliente.getPuntos() + (int) Math.floor(funcion.getPrecio() / 10));
                clientes.save(cliente);
            }
        }
        return boletos.save(b);
    }
}
