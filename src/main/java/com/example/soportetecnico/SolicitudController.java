package com.example.soportetecnico;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class SolicitudController {
    @FXML private ComboBox<Cliente> clienteCombo;
    @FXML private TextField correoField;
    @FXML private TextField tipoClienteField;
    @FXML private TextField asuntoField;
    @FXML private ComboBox<String> tipoServicioCombo;
    @FXML private RadioButton bajaRadio;
    @FXML private RadioButton mediaRadio;
    @FXML private RadioButton altaRadio;
    private final ToggleGroup prioridadGroup = new ToggleGroup();
    @FXML private TextArea descripcionArea;
    @FXML private TextField archivoField;
    @FXML private TextField evidenciasField;

    private Path archivoSeleccionado;
    private Path carpetaEvidencias;

    @FXML
    private void initialize() {
        bajaRadio.setToggleGroup(prioridadGroup);
        mediaRadio.setToggleGroup(prioridadGroup);
        altaRadio.setToggleGroup(prioridadGroup);
        clienteCombo.setItems(RepositorioDatos.clientes());
        tipoServicioCombo.getItems().setAll(
                "Soporte de hardware", "Soporte de software", "Redes y conectividad",
                "Seguridad informática", "Mantenimiento preventivo");
        clienteCombo.valueProperty().addListener((observable, anterior, cliente) -> mostrarDatosCliente(cliente));
        if (!clienteCombo.getItems().isEmpty()) {
            clienteCombo.getSelectionModel().selectLast();
        }
    }

    private void mostrarDatosCliente(Cliente cliente) {
        correoField.setText(cliente == null ? "" : cliente.correo());
        tipoClienteField.setText(cliente == null ? "" : cliente.tipo());
    }

    @FXML
    private void seleccionarArchivo() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccione un archivo adjunto");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));
        File archivo = chooser.showOpenDialog(asuntoField.getScene().getWindow());
        if (archivo != null) {
            archivoSeleccionado = archivo.toPath();
            archivoField.setText(archivo.getAbsolutePath());
        }
    }

    @FXML
    private void seleccionarEvidencias() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Seleccione la carpeta de evidencias");
        File carpeta = chooser.showDialog(asuntoField.getScene().getWindow());
        if (carpeta != null) {
            carpetaEvidencias = carpeta.toPath();
            evidenciasField.setText(carpeta.getAbsolutePath());
        }
    }

    @FXML
    private void guardar() {
        registrarSolicitud(false);
    }

    @FXML
    private void crearSolicitud() {
        registrarSolicitud(true);
    }

    private void registrarSolicitud(boolean limpiarDespues) {
        String error = validar();
        if (error != null) {
            Alertas.error(error);
            return;
        }

        RadioButton prioridad = (RadioButton) prioridadGroup.getSelectedToggle();
        SolicitudServicio solicitud = new SolicitudServicio(clienteCombo.getValue(), asuntoField.getText().trim(),
                tipoServicioCombo.getValue(), prioridad.getText(), descripcionArea.getText().trim(),
                archivoSeleccionado, carpetaEvidencias);
        RepositorioDatos.solicitudes().add(solicitud);
        Alertas.informacion("Solicitud de servicio registrada correctamente.");
        if (limpiarDespues) {
            limpiarFormulario();
        }
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
        if (clienteCombo.getValue() == null) return "Seleccione un cliente. Si no hay clientes, regístrelo primero.";
        if (correoField.getText().isBlank()) return "El cliente seleccionado no tiene correo.";
        if (tipoClienteField.getText().isBlank()) return "El cliente seleccionado no tiene tipo de cliente.";
        if (asuntoField.getText().isBlank()) return "Ingrese el asunto de la solicitud.";
        if (asuntoField.getText().trim().length() < 5) return "El asunto debe contener al menos 5 caracteres.";
        if (tipoServicioCombo.getValue() == null) return "Seleccione el tipo de servicio.";
        if (prioridadGroup.getSelectedToggle() == null) return "Seleccione la prioridad.";
        if (descripcionArea.getText().isBlank()) return "Describa el problema.";
        if (descripcionArea.getText().trim().length() < 10) return "La descripción debe contener al menos 10 caracteres.";
        if (archivoSeleccionado == null) return "Seleccione un archivo adjunto.";
        if (carpetaEvidencias == null) return "Seleccione la carpeta de evidencias.";
        return null;
    }

    private void limpiarFormulario() {
        clienteCombo.getSelectionModel().clearSelection();
        correoField.clear();
        tipoClienteField.clear();
        asuntoField.clear();
        tipoServicioCombo.getSelectionModel().clearSelection();
        prioridadGroup.selectToggle(null);
        descripcionArea.clear();
        archivoField.clear();
        evidenciasField.clear();
        archivoSeleccionado = null;
        carpetaEvidencias = null;
        clienteCombo.requestFocus();
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
