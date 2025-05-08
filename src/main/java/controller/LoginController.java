package controller;

import DAO.MemberDAO;
import POJO.Singleton.CommonObjs;
import POJO.Member;
import POJO.Singleton.GlobalDAO;
import POJO.Singleton.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

public class LoginController {
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField emailField;
    @FXML
    public Label passwordError;
    @FXML
    public Label emailError;

    private boolean isInput() {
        boolean flag = true;
        if (emailField.getText().isEmpty()) {
            emailError.setText("Please enter your email");
            emailError.setVisible(true);
            flag = false;
        } else {emailError.setVisible(false);}
        if (passwordField.getText().isEmpty()) {
            passwordError.setText("Please enter your password");
            passwordError.setVisible(true);
            flag = false;
        }  else {passwordError.setVisible(false);}
        return flag;
    }

    public void handleLogin(ActionEvent actionEvent) {
        if(isInput()) {
            String email = emailField.getText();
            String inputPassword = passwordField.getText();

            MemberDAO memberDAO = GlobalDAO.getInstance().getMemberDAO();
            Member member = memberDAO.getMember(email);
            if(member != null) {
                String storedHash = member.getHashedPassword();
                if (BCrypt.checkpw(inputPassword, storedHash)) {
                    try {
                        Session.get().setMember(member);
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
                else {
                    passwordError.setText("Email or password is incorrect");
                    passwordError.setVisible(true);
                }
            }
            else {
                passwordError.setText("Email or password is incorrect");
                passwordError.setVisible(true);
            }

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
