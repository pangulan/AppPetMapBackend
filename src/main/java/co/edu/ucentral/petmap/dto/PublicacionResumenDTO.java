package co.edu.ucentral.petmap.dto;

import co.edu.ucentral.petmap.entity.Publicacion;

public class PublicacionResumenDTO {
    public Long id;
    public String tipo;
    public String especie;
    public String descripcion;
    public String foto;
    public Double latitud;
    public Double longitud;
    public String fecha;
    public String estado;

    public static PublicacionResumenDTO desde(Publicacion p) {
        PublicacionResumenDTO dto = new PublicacionResumenDTO();
        dto.id = p.id;
        dto.tipo = p.tipo != null ? p.tipo.name() : null;
        dto.especie = p.especie;
        dto.descripcion = p.descripcion;
        dto.foto = p.foto;
        dto.latitud = p.latitud;
        dto.longitud = p.longitud;
        dto.fecha = p.fecha != null ? p.fecha.toString() : null;
        dto.estado = p.estado != null ? p.estado.name() : null;
        return dto;
    }
}