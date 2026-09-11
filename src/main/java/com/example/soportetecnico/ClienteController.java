package com.example.soportetecnico;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class ClienteController {
    private static final Pattern CORREO = Pattern.compile("^[\\w.!#$%&'*+/=?^`{|}~-]+@[\\w-]+(?:\\.[\\w-]+)+$");
    private static final Pattern TELEFONO = Pattern.compile("^[0-9+() -]{7,20}$");

    @FXML private TextField nombreField;
    @FXML private TextField correoField;
    @FXML private TextField telefonoField;
    @FXML private ComboBox<String> tipoClienteCombo;
    @FXML private TextField documentoField;
    @FXML private TextField directorioField;

    private Path documentoSeleccionado;
    private Path directorioSeleccionado;

    @FXML
    private void initialize() {
        tipoClienteCombo.getItems().setAll("Individual", "Empresa", "Institución");
    }

    @FXML
    private void seleccionarDocumento() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccione el documento de identificación");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Documentos e imágenes", "*.pdf", "*.png", "*.jpg", "*.jpeg"));
        File archivo = chooser.showOpenDialog(nombreField.getScene().getWindow());
        if (archivo != null) {
            documentoSeleccionado = archivo.toPath();
            documentoField.setText(archivo.getAbsolutePath());
        }
    }

    @FXML
    private void seleccionarDirectorio() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Seleccione el directorio del cliente");
        File carpeta = chooser.showDialog(nombreField.getScene().getWindow());
        if (carpeta != null) {
            directorioSeleccionado = carpeta.toPath();
            directorioField.setText(carpeta.getAbsolutePath());
        }
    }

    @FXML
    private void guardar() {
        String error = validar();
        if (error != null) {
            Alertas.error(error);
            return;
        }

        Cliente cliente = new Cliente(nombreField.getText().trim(), correoField.getText().trim(),
                telefonoField.getText().trim(), tipoClienteCombo.getValue(),
                documentoSeleccionado, directorioSeleccionado);
        RepositorioDatos.clientes().add(cliente);
        Alertas.informacion("Cliente registrado correctamente.");
        limpiarFormulario();
    }

    @FXML
    private void crearSolicitud() {
        String error = validar();
        if (error != null) {
            Alertas.error(error);
            return;
        }
        if (!RepositorioDatos.existeCorreo(correoField.getText().trim())) {
            RepositorioDatos.clientes().add(new Cliente(nombreField.getText().trim(), correoField.getText().trim(),
                    telefonoField.getText().trim(), tipoClienteCombo.getValue(),
                    documentoSeleccionado, directorioSeleccionado));
        }
        navegar(AppNavigator::mostrarSolicitud);
    }

    @FXML
    private void limpiar() {
        limpiarFormulario();
    }

    @FXML
    private void cerrar() {
        navegar(AppNavigator::mostrarMenu);
    }

    private String validar() {
        if (nombreField.getText().isBlank()) return "Ingrese el nombre del cliente.";
        if (nombreField.getText().trim().length() < 3) return "El nombre debe contener al menos 3 caracteres.";
        String correo = correoField.getText().trim();
        if (correo.isEmpty()) return "Ingrese el correo del cliente.";
        if (!CORREO.matcher(correo).matches()) return "Ingrese un correo electrónico válido.";
        if (RepositorioDatos.existeCorreo(correo)) return "Ya existe un cliente registrado con ese correo.";
        String telefono = telefonoField.getText().trim();
        if (telefono.isEmpty()) return "Ingrese el teléfono del cliente.";
        if (!TELEFONO.matcher(telefono).matches()) return "El teléfono debe contener entre 7 y 20 caracteres válidos.";
        if (tipoClienteCombo.getValue() == null) return "Seleccione el tipo de cliente.";
        if (documentoSeleccionado == null) return "Seleccione el documento de identificación.";
        if (directorioSeleccionado == null) return "Seleccione el directorio del cliente.";
        return null;
    }

    private void limpiarFormulario() {
        nombreField.clear();
        correoField.clear();
        telefonoField.clear();
        tipoClienteCombo.getSelectionModel().clearSelection();
        documentoField.clear();
        directorioField.clear();
        documentoSeleccionado = null;
        directorioSeleccionado = null;
        nombreField.requestFocus();
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
