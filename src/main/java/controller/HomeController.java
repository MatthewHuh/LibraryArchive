package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    public void handleLogout(ActionEvent actionEvent) {
        try {
            Parent home = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Stage st   = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            st.setScene(new Scene(home));
            st.sizeToScene();
            st.centerOnScreen();
        } catch (IOException e) {
            // 1) Log the error
            e.printStackTrace();

            // 2) Inform the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not load Login Page");
            alert.setContentText("Please try again or contact support.");
            alert.showAndWait();
        }
    }
}
