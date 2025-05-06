package controller;

import DAO.MemberDAO;
import POJO.Member;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.control.TextFormatter;
import java.util.function.UnaryOperator;

public class SignUpController {
    @FXML
    public TextField phoneField;
    @FXML
    public Label phoneError;
    @FXML
    private Label addressError;
    @FXML
    private TextField addressField;
    @FXML
    private TextField cityField;
    @FXML
    private Label cityStateZipError;
    @FXML
    private Label confirmError;
    @FXML
    private PasswordField confirmField;
    @FXML
    private Label emailError;
    @FXML
    private TextField emailField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private Label nameError;
    @FXML
    private Label passwordError;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField zipField;

    @FXML
    public void initialize() {
        // 1) Digit+length filter, only active while focused
        UnaryOperator<TextFormatter.Change> digitLimitFilter = change -> {
            if (!phoneField.isFocused()) {
                // allow programmatic or post-focus changes through
                return change;
            }
            // when focused, enforce digits only & max 10 length
            String newText = change.getControlNewText();
            return newText.matches("\\d{0,10}") ? change : null;
        };
        phoneField.setTextFormatter(new TextFormatter<>(digitLimitFilter));

        // 2) Listen for focus changes
        phoneField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            String current = phoneField.getText();
            // focus gained: strip formatting → just digits
            if (!wasFocused && isNowFocused) {
                String digits = current.replaceAll("\\D", "");
                phoneField.setText(digits);
            }
            // focus lost: pretty-print if exactly 10 digits
            else if (wasFocused && !isNowFocused) {
                String digits = current.replaceAll("\\D", "");
                if (digits.length() == 10) {
                    phoneField.setText(String.format("(%s) %s-%s",
                            digits.substring(0, 3),
                            digits.substring(3, 6),
                            digits.substring(6)));
                }
            }
        });

        UnaryOperator<TextFormatter.Change> zipFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d{0,5}") ? change : null;
        };
        zipField.setTextFormatter(new TextFormatter<>(zipFilter));

        UnaryOperator<TextFormatter.Change> stateFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("[a-zA-Z]{0,2}") ? change : null;
        };
        stateField.setTextFormatter(new TextFormatter<>(stateFilter));

        stateField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            String current = stateField.getText();
            // focus lost: pretty-print if exactly 10 digits
            if (wasFocused && !isNowFocused) {
                stateField.setText(current.toUpperCase());
            }
        });
    }

    public void handleSignup(ActionEvent actionEvent) {
        if(isInput() && isValid()) {
            Member member = getMember();
            MemberDAO memberDAO = new MemberDAO();
            if(memberDAO.insert(member) != 1) {
                emailError.setText("Email already in use");
                emailError.setVisible(true);
            }
            else {
                emailError.setVisible(false);
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
        }
    }

    private boolean isInput() {
        boolean flag = true;
        if(addressField.getText().isEmpty()) {
            addressError.setText("Please enter your address");
            addressError.setVisible(true);
            flag = false;
        } else {addressError.setVisible(false);}
        if(emailField.getText().isEmpty()) {
            emailError.setText("Please enter your email");
            emailError.setVisible(true);
            flag = false;
        } else {emailError.setVisible(false);}
        if(phoneField.getText().isEmpty()) {
            phoneError.setText("Please enter your phone number");
            phoneError.setVisible(true);
            flag = false;
        } else {phoneError.setVisible(false);}
        if(firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
            nameError.setText("Please enter your first name and last name");
            nameError.setVisible(true);
            flag = false;
        } else {nameError.setVisible(false);}
        if(cityField.getText().isEmpty() || stateField.getText().isEmpty() || zipField.getText().isEmpty()) {
            cityStateZipError.setText("Please enter your city, state, and zip code");
            cityStateZipError.setVisible(true);
            flag = false;
        }  else {cityStateZipError.setVisible(false);}
        if(passwordField.getText().isEmpty()) {
            passwordError.setText("Please enter your password");
            passwordError.setVisible(true);
            flag = false;
        }else {passwordError.setVisible(false);}
        if(confirmField.getText().isEmpty()) {
            confirmError.setText("Please enter your confirmation password");
            confirmError.setVisible(true);
            flag = false;
        } else {confirmError.setVisible(false);}
        return flag;
    }

    private Member getMember() {
        String address = addressField.getText().trim();
        String city = cityField.getText().trim();
        String state = stateField.getText();
        String zip = zipField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String phoneNumber = phoneField.getText().replaceAll("\\D", "");

        String compAddress = address + ", " + city + " " + state + " " + zip;

        return new Member(null, firstName, lastName, phoneNumber, email, compAddress, password);
    }

    private boolean isValid() {
        boolean flag = true;
        if(phoneField.getText().replaceAll("\\D", "").length() != 10) {
            phoneError.setText("Please enter valid phone number");
            phoneError.setVisible(true);
            flag = false;
        }
        if(stateField.getText().length() != 2 || zipField.getText().length() != 5) {
            cityStateZipError.setText("Please enter valid city, state, or zip code");
            cityStateZipError.setVisible(true);
            flag = false;
        }
        if(!passwordField.getText().equals(confirmField.getText())) {
            confirmError.setText("Passwords do not match");
            confirmError.setVisible(true);
            flag = false;
        }
        return flag;
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
