package com.example.parqueoscallejeros;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage primaryStage; // Guardar una referencia al Stage principal

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        showAdministradores(); // Iniciar con la primera escena
    }

    // Método para mostrar la primera escena
    public static void showAdministradores() throws IOException {
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("/com/example/parqueoscallejeros/Admin/AdminLogin/Scene1.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Scene 1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para mostrar la segunda escena
    public static void showUsuarios() throws IOException {
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("/com/example/parqueoscallejeros/User/UserLogin/Scene1.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Scene 2");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para mostrar la tercera escena
    public static void showInspectores() throws IOException {
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("/com/example/parqueoscallejeros/User/UserLogin/Scene3.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Scene 3");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
// cambio