package com.cine.models;
import jakarta.persistence.*;
@Entity @Table(name = "peliculas")
public class Pelicula {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String titulo;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Clasificacion clasificacion;
    private int duracion;
    private String genero;
    public Pelicula() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Clasificacion getClasificacion() { return clasificacion; }
    public void setClasificacion(Clasificacion clasificacion) { this.clasificacion = clasificacion; }
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
}
