package co.edu.ucentral.petmap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PublicacionPerdidaDTO {
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

    @NotNull
    public Long autorId;
}
