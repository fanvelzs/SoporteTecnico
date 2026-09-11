package com.example.soportetecnico;

import java.nio.file.Path;

public record SolicitudServicio(Cliente cliente, String asunto, String tipoServicio,
                                String prioridad, String descripcion, Path archivoAdjunto,
                                Path carpetaEvidencias) {
}
