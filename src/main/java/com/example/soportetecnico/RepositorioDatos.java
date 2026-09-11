package com.example.soportetecnico;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class RepositorioDatos {
    private static final ObservableList<Cliente> CLIENTES = FXCollections.observableArrayList();
    private static final ObservableList<SolicitudServicio> SOLICITUDES = FXCollections.observableArrayList();

    private RepositorioDatos() {
    }

    public static ObservableList<Cliente> clientes() {
        return CLIENTES;
    }

    public static ObservableList<SolicitudServicio> solicitudes() {
        return SOLICITUDES;
    }

    public static boolean existeCorreo(String correo) {
        return CLIENTES.stream().anyMatch(cliente -> cliente.correo().equalsIgnoreCase(correo));
    }
}
