package co.edu.ucentral.petmap.dto.publicacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PublicacionEncontradaDTO {
    @NotBlank
    public String especie;

    @NotBlank
    public String descripcion;

    @NotBlank(message = "La foto es obligatoria para publicaciones de mascota encontrada")
    public String foto;

    @NotNull
    public Double latitud;

    @NotNull
    public Double longitud;

    @NotNull
    public LocalDate fecha;

    @NotNull
    public Long autorId;
}