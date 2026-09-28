package co.edu.ucentral.petmap.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class Usuario extends PanacheEntity {

    @NotBlank
    public String nombre;

    @NotBlank
    @Email
    @Column(unique = true)
    public String correo;

    @NotBlank
    public String contrasena;

    public String telefono;

    public String foto;

    @Enumerated(EnumType.STRING)
    public RolUsuario rol = RolUsuario.PUBLICADOR;

    public LocalDateTime fechaRegistro = LocalDateTime.now();
}