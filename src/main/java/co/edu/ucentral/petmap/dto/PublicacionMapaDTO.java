package co.edu.ucentral.petmap.dto;

import co.edu.ucentral.petmap.entity.Publicacion;

public class PublicacionMapaDTO {
    public Long id;
    public String tipo;
    public String especie;
    public String descripcion;
    public Double latitud;
    public Double longitud;
    public String fecha;
    public boolean tieneFoto;

    public static PublicacionMapaDTO desde(Publicacion p) {
        PublicacionMapaDTO dto = new PublicacionMapaDTO();
        dto.id = p.id;
        dto.tipo = p.tipo != null ? p.tipo.name() : null;
        dto.especie = p.especie;
        dto.descripcion = p.descripcion;
        dto.latitud = p.latitud;
        dto.longitud = p.longitud;
        dto.fecha = p.fecha != null ? p.fecha.toString() : null;
        dto.tieneFoto = p.foto != null && !p.foto.isBlank();
        return dto;
    }
}