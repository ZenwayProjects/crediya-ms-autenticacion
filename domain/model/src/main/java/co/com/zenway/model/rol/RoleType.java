package co.com.zenway.model.rol;

import java.util.Arrays;

public enum RoleType {
    ADMINISTRADOR((short) 1),
    CLIENTE((short) 2),
    ASESOR((short) 3);

    private final short id;

    RoleType(short id) {
        this.id = id;
    }

    public short getId() {
        return id;
    }

    public static RoleType fromId(short id) {
        return Arrays.stream(values())
                .filter(r -> r.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rol id desconocido: " + id));
    }

}


