package com.example.parqueoscallejeros.Admin.AdminMain;
// cambio
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private int uniqueId;
    private String userId;
    private String userPin;

    public void setUserData(int id, String userId, String userPin) {
        System.out.println(id);
        System.out.println(userId);
        System.out.println(userPin);
        this.uniqueId = id;
        this.userId = userId;
        this.userPin = userPin;
    }

    public void switchToMain(ActionEvent event) throws IOException { // REGISTRO
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminMainFunctions/ConfigParqueo.fxml"));
        Parent root = loader.load();
        ConfigParqueo controller = loader.getController();
        controller.setUserData(uniqueId, userId, userPin);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAddInspectores(ActionEvent event) throws IOException { // REGISTRO
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminMainFunctions/AgregarInspectores.fxml"));
        Parent root = loader.load();
        AddInspectores controller = loader.getController();
        controller.setUserData(uniqueId, userId, userPin);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCambiarContraInspector(ActionEvent event) throws IOException { // REGISTRO
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminMainFunctions/CambiarContraInspector.fxml"));
        Parent root = loader.load();
        CambiarContraInspector controller = loader.getController();
        controller.setUserData(uniqueId, userId, userPin);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
