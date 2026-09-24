package co.edu.ucentral.petmap.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Usuario extends PanacheEntity {
    public String nombre;
    public String correo;
}