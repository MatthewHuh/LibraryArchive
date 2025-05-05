package controller;

import POJO.CommonObjs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.event.ActionEvent;

public class LoginController {

    public void handleLogin(ActionEvent actionEvent) {
        try {
            BorderPane home = FXMLLoader.load(getClass().getResource("/view/Home.fxml"));
            Stage  st   = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            st.setScene(new Scene(home));
            st.sizeToScene();
            st.centerOnScreen();

            CommonObjs commonObjs = CommonObjs.getInstance();
            commonObjs.setBorderPane(home);

        } catch (IOException e) {
            // 1) Log the error
            e.printStackTrace();

            // 2) Inform the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not load Home Page");
            alert.setContentText("Please try again or contact support.");
            alert.showAndWait();
        }
    }

    public void handleGoToSignup(ActionEvent actionEvent) {
        try {
            Parent signup = FXMLLoader.load(getClass().getResource("/view/SignUp.fxml"));
            Stage st = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            st.setScene(new Scene(signup));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not open Sign-up form");
            alert.setContentText("Please try again or contact support.");
            alert.showAndWait();
        }
    }
}
