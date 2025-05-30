package controller;

import DAO.MemberDAO;
import POJO.Member;
import POJO.Singleton.CommonObjs;
import POJO.Singleton.GlobalDAO;
import POJO.Singleton.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class ProfileController {
    @FXML
    private Label addressError;

    @FXML
    private TextField addressField;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField cityField;

    @FXML
    private Label cityStateZipError;

    @FXML
    private Label emailError;

    @FXML
    private TextField emailField;

    @FXML
    private Label firstNameError;

    @FXML
    private TextField firstNameField;

    @FXML
    private Label lastNameError;

    @FXML
    private TextField lastNameField;

    @FXML
    private Label phoneError;

    @FXML
    private TextField phoneField;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button saveButton;

    @FXML
    private TextField stateField;

    @FXML
    private TextField zipField;

    private CommonObjs commonObjs = CommonObjs.getInstance();

    private Member member;

    public void initialize() {
        member = Session.get().getMember();
        if (member != null) {
            String[] address = parseAddress(member.getAddress());
            String phoneNumber = member.getPhoneNumber();
            addressField.setText(address[0]);
            cityField.setText(address[1]);
            stateField.setText(address[2]);
            zipField.setText(address[3]);
            emailField.setText(member.getEmail());
            firstNameField.setText(member.getFirstName());
            lastNameField.setText(member.getLastName());
            phoneField.setText(String.format("(%s) %s-%s",
                    phoneNumber.substring(0, 3),
                    phoneNumber.substring(3, 6),
                    phoneNumber.substring(6)));
        }

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
        UnaryOperator<TextFormatter.Change> nameFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("[a-zA-Z]{0,50}") ? change : null;
        };
        firstNameField.setTextFormatter(new TextFormatter<>(nameFilter));
        lastNameField.setTextFormatter(new TextFormatter<>(nameFilter));

        UnaryOperator<TextFormatter.Change> emailfilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("[A-Za-z@.]{0,62}") ? change : null;
        };
        emailField.setTextFormatter(new TextFormatter<>(emailfilter));

        UnaryOperator<TextFormatter.Change> addressFilter = change -> {
            String newText = change.getControlNewText();
            // allow empty (so user can delete) or only letters+digits
            return newText.matches("[A-Za-z0-9 ]{0,60}") ? change : null;
        };
        addressField.setTextFormatter(new TextFormatter<>(addressFilter));

        UnaryOperator<TextFormatter.Change> cityFilter = change -> {
            String newText = change.getControlNewText();
            // allow empty (so user can delete) or only letters+digits
            return newText.matches("[A-Za-z ]{0,59}") ? change : null;
        };
        cityField.setTextFormatter(new TextFormatter<>(cityFilter));
    }

    private String[] parseAddress(String address) {
        // input format: "123 Main Street, Anytown CA 12345"
        String[] parts = address.split(",", 2);
        String street = parts[0].trim();

        // parts[1] is " Anytown CA 12345"
        String[] tokens = parts[1].trim().split("\\s+");
        int    n      = tokens.length;
        String zip    = tokens[n - 1];        // last token
        String state  = tokens[n - 2];        // second last
        // the rest (0 ... n-3) belong to the city:
        String city   = String.join(" ", Arrays.copyOfRange(tokens, 0, n - 2));

        return new String[] {street, city, state, zip};
    }

    public void handleSave(ActionEvent actionEvent) {
        if(isInput() && isValid()){
            Member member = getMember();
            MemberDAO memberDAO = new MemberDAO();
            if(memberDAO.containsEmail(member.getEmail()) && !this.member.getEmail().equals(member.getEmail())) {
                emailError.setText("Email already in use");
                emailError.setVisible(true);
            }
            else {
                memberDAO.update(member);
                emailError.setVisible(false);
                Session.get().setMember(member);
                loadContent("/view/HomeContent.fxml");
            }
        }
    }

    private boolean isInput() {
        boolean flag = true;
        if(addressField.getText().trim().isEmpty()) {
            addressError.setText("Please enter your address");
            addressError.setVisible(true);
            flag = false;
        } else {addressError.setVisible(false);}
        if(emailField.getText().trim().isEmpty()) {
            emailError.setText("Please enter your email");
            emailError.setVisible(true);
            flag = false;
        } else {emailError.setVisible(false);}
        if(phoneField.getText().isEmpty()) {
            phoneError.setText("Please enter your phone number");
            phoneError.setVisible(true);
            flag = false;
        } else {phoneError.setVisible(false);}
        if(firstNameField.getText().trim().isEmpty()) {
            firstNameError.setText("Please enter your first name");
            firstNameError.setVisible(true);
            flag = false;
        } else {firstNameError.setVisible(false);}
        if(lastNameField.getText().trim().isEmpty()) {
            lastNameError.setText("Please enter your last name");
            lastNameError.setVisible(true);
            flag = false;
        } else {lastNameError.setVisible(false);}
        if(cityField.getText().trim().isEmpty() || stateField.getText().isEmpty() || zipField.getText().isEmpty()) {
            cityStateZipError.setText("Please enter your city, state, and zip code");
            cityStateZipError.setVisible(true);
            flag = false;
        }  else {cityStateZipError.setVisible(false);}
        return flag;
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
        String password = member.getHashedPassword();
        String phoneNumber = phoneField.getText().replaceAll("\\D", "");
        Integer memberId = member.getMemberId();

        String compAddress = address + ", " + city + " " + state + " " + zip;

        return new Member(memberId, firstName, lastName, phoneNumber, email, compAddress, password);
    }

    private void loadContent(String urlStr) {
        URL url = getClass().getResource(urlStr);
        try{
            Node scrollPane = FXMLLoader.load(url);

            commonObjs.getBorderPane().setCenter(scrollPane);
        }
        catch(IOException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not return to Login");
            alert.setContentText("Please try again or contact support.");
            alert.showAndWait();
        }
    }

    public void handleCancel(ActionEvent actionEvent) {
        loadContent("/view/HomeContent.fxml");
    }

    public void handleDelete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to proceed?");
        alert.setContentText("This action cannot be undone.");

        // Customize the buttons (optional)
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Show the dialog and wait for a response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            //check if user has checked out books
            if (GlobalDAO.getInstance().getBorrowRecordDAO().hasActiveBorrowRecord(Session.get().getMember().getMemberId())) {
                //alert user they must return books before deleting their account
                Alert returnAlert = new Alert(Alert.AlertType.INFORMATION);
                returnAlert.setTitle("Account Deletion");
                returnAlert.setHeaderText("Account Deletion Failed");
                returnAlert.setContentText("Please return your books before proceeding.");
                returnAlert.showAndWait();
            }
            //delete user
            else {
                GlobalDAO.getInstance().getMemberDAO().delete(Session.get().getMember().getMemberId());
                Alert returnAlert = new Alert(Alert.AlertType.INFORMATION);
                returnAlert.setTitle("Account Deletion");
                returnAlert.setHeaderText("Account Deletion Success");
                returnAlert.showAndWait();

                //signout the user
                try{
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
                Alert navigationError = new Alert(Alert.AlertType.ERROR);
                navigationError.setTitle("Navigation Error");
                navigationError.setHeaderText("Could not load Login Page");
                navigationError.setContentText("Please try again or contact support.");
                navigationError.showAndWait();
                }
            }
        }
    }
}
