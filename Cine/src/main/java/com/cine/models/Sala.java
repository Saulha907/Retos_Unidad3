package com.cine.models;
import jakarta.persistence.*;
@Entity @Table(name = "salas", uniqueConstraints = @UniqueConstraint(columnNames = "numero"))
public class Sala {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private Integer numero;
    @Column(nullable = false) private Integer capacidad;
    public Sala() {}
    public Sala(Integer numero, Integer capacidad) { this.numero = numero; this.capacidad = capacidad; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
}
