package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    public void handleSignup(ActionEvent actionEvent) {
        try {
            Parent login = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Stage st = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            st.setScene(new Scene(login));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not return to Login");
            alert.setContentText("Please try again or contact support.");
            alert.showAndWait();
        }
    }

    public void handleGoToLogin(ActionEvent actionEvent) {
        try {
            Parent login = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Stage st = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            st.setScene(new Scene(login));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not open Login form");
            alert.setContentText("Please try again or contact support.");
            alert.showAndWait();
        }
    }
}
