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

    /**
     * Funcion que permite traer la informacion del usuario de la pantalla pasada a esta
     * @param id Id del usuario
     * @param userId id alfanumerico del usuario
     * @param userPin pin del usuario
     */
    public void setUserData(int id, String userId, String userPin) {
        System.out.println(id);
        System.out.println(userId);
        System.out.println(userPin);
        this.uniqueId = id;
        this.userId = userId;
        this.userPin = userPin;
    }

    /**
     * Funcion que permite moverse a main
     * @param event
     * @throws IOException
     */
    public void switchToMain(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminMainFunctions/ConfigParqueo.fxml"));
        Parent root = loader.load();
        ConfigParqueo controller = loader.getController();
        controller.setUserData(uniqueId, userId, userPin);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Funcion que permite moverse a la ventana que inspectores
     * @param event
     * @throws IOException
     */
    public void switchToAddInspectores(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminMainFunctions/AgregarInspectores.fxml"));
        Parent root = loader.load();
        AddInspectores controller = loader.getController();
        controller.setUserData(uniqueId, userId, userPin);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Permite cambiar a la ventana de cambiar la contra del inspector
     * @param event
     * @throws IOException
     */
    public void switchToCambiarContraInspector(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminMainFunctions/CambiarContraInspector.fxml"));
        Parent root = loader.load();
        CambiarContraInspector controller = loader.getController();
        controller.setUserData(uniqueId, userId, userPin);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Cambio a reportes
     * @param event
     * @throws IOException
     */
    public void switchReportes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminMainFunctions/AdminReport.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}
