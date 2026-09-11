package com.example.soportetecnico;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class AppNavigator {
    private static Stage stage;

    private AppNavigator() {
    }

    public static void inicializar(Stage primaryStage) {
        stage = primaryStage;
        stage.setMinWidth(760);
        stage.setMinHeight(560);
    }

    public static void mostrarMenu() throws IOException {
        cambiarVista("menu-principal.fxml", "Soporte técnico - Menú principal", 820, 580);
    }

    public static void mostrarClientes() throws IOException {
        cambiarVista("registro-clientes.fxml", "Registro de clientes", 900, 680);
    }

    public static void mostrarSolicitud() throws IOException {
        cambiarVista("solicitud-servicio.fxml", "Solicitud de servicio", 940, 780);
    }

    public static void cerrarAplicacion() {
        if (stage != null) {
            stage.close();
        }
    }

    private static void cambiarVista(String recurso, String titulo, double ancho, double alto) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(recurso));
        Parent root = loader.load();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root, ancho, alto));
        stage.centerOnScreen();
    }
}
