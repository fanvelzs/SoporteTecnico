package com.example.soportetecnico;

import javafx.fxml.FXML;

import java.io.IOException;

public class MenuController {
    @FXML
    private void abrirClientes() {
        navegar(AppNavigator::mostrarClientes);
    }

    @FXML
    private void abrirSolicitudes() {
        navegar(AppNavigator::mostrarSolicitud);
    }

    @FXML
    private void salir() {
        AppNavigator.cerrarAplicacion();
    }

    private void navegar(Navegacion navegacion) {
        try {
            navegacion.abrir();
        } catch (IOException e) {
            Alertas.errorNavegacion(e);
        }
    }

    @FunctionalInterface
    private interface Navegacion {
        void abrir() throws IOException;
    }
}
