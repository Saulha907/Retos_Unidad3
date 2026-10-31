package com.cine.models;
import jakarta.persistence.*;
@Entity @Table(name = "boletos", uniqueConstraints = {
    @UniqueConstraint(columnNames = "folio"),
    @UniqueConstraint(columnNames = {"funcion_id", "fila", "numero"})
})
public class Boleto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String folio;
    @Column(nullable = false) private String fila;
    private int numero;
    @ManyToOne(optional = false) @JoinColumn(name = "funcion_id") private Funcion funcion;
    @ManyToOne @JoinColumn(name = "cliente_id") private Cliente cliente;
    public Boleto() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }
    public String getFila() { return fila; }
    public void setFila(String fila) { this.fila = fila; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public Funcion getFuncion() { return funcion; }
    public void setFuncion(Funcion funcion) { this.funcion = funcion; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}
