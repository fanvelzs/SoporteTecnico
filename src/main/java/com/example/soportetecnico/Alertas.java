package com.example.soportetecnico;

import javafx.scene.control.Alert;

public final class Alertas {
    private Alertas() {
    }

    public static void error(String mensaje) {
        mostrar(Alert.AlertType.ERROR, "Datos incompletos", mensaje);
    }

    public static void informacion(String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, "Operación exitosa", mensaje);
    }

    public static void errorNavegacion(Exception excepcion) {
        mostrar(Alert.AlertType.ERROR, "Error de navegación",
                "No fue posible abrir la pantalla solicitada.\n" + excepcion.getMessage());
    }

    private static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
