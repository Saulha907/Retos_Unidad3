package com.cine.models;
import jakarta.persistence.*;
@Entity @Table(name = "clientes")
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String nombre;
    @Column(unique = true) private String email;
    private boolean registrado;
    private Integer puntos = 0;
    public Cliente() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isRegistrado() { return registrado; }
    public void setRegistrado(boolean registrado) { this.registrado = registrado; }
    public Integer getPuntos() { return puntos; }
    public void setPuntos(Integer puntos) { this.puntos = puntos; }
}
