package co.com.zenway.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "rol")
@Data
public class RolEntity {
    @Id
    @Column("id_rol")
    private Short id;

    @Column("nombre")
    private String nombre;

    @Column("descripcion")
    private String descripcion;
}
