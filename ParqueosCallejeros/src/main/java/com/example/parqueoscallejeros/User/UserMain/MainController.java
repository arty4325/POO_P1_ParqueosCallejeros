package com.example.parqueoscallejeros.User.UserMain;
// cambio

import com.example.parqueoscallejeros.dataBase.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private int uniqueId;
    private String userId;
    private String userPin;

    @FXML
    private TextField registroPlaca;

    @FXML
    private TextField registroMarca;

    @FXML
    private TextField registroModelo;

    public void setUserData(int id, String userId, String userPin) {
        System.out.println(id);
        System.out.println(userId);
        System.out.println(userPin);
        this.uniqueId = id;
        this.userId = userId;
        this.userPin = userPin;
    }

    public void switchToAgregarParqueos(ActionEvent event) throws IOException { // REGISTRO
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/User/UserMainFunctions/AgregarParqueos.fxml"));
        Parent root = loader.load();

        // Obtén el controlador del nuevo FXML
        MainController controller = loader.getController();

        // Pasa el valor (por ejemplo, el idUsuario)
        controller.setUserData(uniqueId, userId, userPin);

        // Cambia la escena
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToParquear(ActionEvent event) throws IOException { // REGISTRO
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/User/UserMainFunctions/Parquear1.fxml"));
        Parent root = loader.load();

        // Obtén el controlador del nuevo FXML
        ParquearController controller = loader.getController();

        // Pasa el valor (por ejemplo, el idUsuario)
        controller.setUserData(this.uniqueId, this.userId, this.userPin);

        // Cambia la escena
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDesaparquear(ActionEvent event) throws IOException { // REGISTRO
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/User/UserMainFunctions/Desaparquear1.fxml"));
        Parent root = loader.load();

        // Obtén el controlador del nuevo FXML
        Desaparcar controller = loader.getController();

        // Pasa el valor (por ejemplo, el idUsuario)
        controller.setUserData(this.uniqueId, this.userId, this.userPin);

        // Cambia la escena
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void registrarCarros(ActionEvent event) throws IOException {
        if(validarRegistro()){
            DatabaseManager databaseManager = new DatabaseManager();
            //System.out.println(this.uniqueId);
            databaseManager.insertarVehiculo(this.uniqueId, registroPlaca.getText(), registroMarca.getText(), registroModelo.getText());
        } else {
            validarRegistro();
        }
    }

    public boolean validarRegistro() {
        // Validar placa (debe tener entre 1 y 6 caracteres y solo números)
        String placa = registroPlaca.getText();
        if (placa == null || !placa.matches(".{1,6}")) {
            System.out.println("La placa debe tener entre 1 y 6 caracteres y solo contener números.");
            return false;
        }

        // Validar marca (opcional: hasta 15 caracteres)
        String marca = registroMarca.getText();
        if (marca != null && marca.length() > 15) {
            System.out.println("La marca no puede exceder los 15 caracteres.");
            return false;
        }

        // Validar modelo (opcional: hasta 15 caracteres)
        String modelo = registroModelo.getText();
        if (modelo != null && modelo.length() > 15) {
            System.out.println("El modelo no puede exceder los 15 caracteres.");
            return false;
        }

        // Si pasa todas las validaciones
        System.out.println("PASO");
        return true;
    }




}
