package co.com.zenway.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "usuario")
@Data
public class UsuarioEntity {

    @Id
    @Column("id_usuario")
    private Long id;

    @Column("nombre")
    private String nombre;

    @Column("apellido")
    private String apellido;

    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column("email")
    private String email;

    @Column("documento_identidad")
    private String documentoIdentidad;

    @Column("telefono")
    private String telefono;

    @Column("salario_base")
    private BigDecimal salarioBase;

    @Column("id_rol")
    private Long rolId;


}
