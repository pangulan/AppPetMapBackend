package co.edu.ucentral.petmap.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Comentario extends PanacheEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "publicacion_id")
    public Publicacion publicacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "autor_id")
    public Usuario autor;

    @Column(length = 500)
    public String texto;

    public LocalDateTime fecha = LocalDateTime.now();
}