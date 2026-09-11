package com.example.soportetecnico;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AppNavigator.inicializar(stage);
        AppNavigator.mostrarMenu();
        stage.show();
    }
}
