package controller;

import POJO.Member;
import POJO.Singleton.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HomeController {
    @FXML
    public BorderPane mainPane;

    @FXML
    private Button adminButton;

    @FXML
    private Hyperlink accountLink;

    @FXML
    private Hyperlink borrowedLink;

    @FXML
    private Hyperlink browseLink;

    @FXML
    private Hyperlink homeLink;


    @FXML
    private Label logoLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private HBox navBar;

    private Member member;

    public void initialize() {
        member = Session.get().getMember();
        adminButton.setVisible(member != null && member.getIsAdmin());
        loadContent("/view/HomeContent.fxml");
    }

    public void handleLogout(ActionEvent actionEvent) {
        try {
            Session.get().setMember(null);
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

    private void loadContent(String urlStr) {
        URL url = getClass().getResource(urlStr);
        try{
            Node scrollPane = FXMLLoader.load(url);

            mainPane.setCenter(scrollPane);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void handleHome(ActionEvent actionEvent) {
        loadContent("/view/HomeContent.fxml");
    }

    public void handleAccount(ActionEvent actionEvent) {
        loadContent("/view/Profile.fxml");
    }

    public void handleBrowse(ActionEvent actionEvent) {
        loadContent("/view/Search.fxml");
    }

    public void handleBorrowed(ActionEvent actionEvent) {
        loadContent("/view/Borrowed.fxml");
    }

    public void handleAdmin(ActionEvent actionEvent) {
        loadContent("/view/AdminDashboard.fxml");
    }
}
