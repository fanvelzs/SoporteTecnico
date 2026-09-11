package com.example.soportetecnico;

import java.nio.file.Path;

public record Cliente(String nombre, String correo, String telefono, String tipo,
                       Path documentoIdentificacion, Path directorio) {
    @Override
    public String toString() {
        return nombre;
    }
}
