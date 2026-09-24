package co.edu.ucentral.petmap.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Publicacion extends PanacheEntity {

    @Enumerated(EnumType.STRING)
    public TipoPublicacion tipo;

    @NotBlank
    public String especie;

    @NotBlank
    public String descripcion;

    public String foto;

    @NotNull
    public Double latitud;

    @NotNull
    public Double longitud;

    @NotNull
    public LocalDate fecha;

    @Enumerated(EnumType.STRING)
    public EstadoPublicacion estado = EstadoPublicacion.PENDIENTE;

    public LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "autor_id")
    public Usuario autor;
}