package controller;

import DAO.BookInfoDao;
import POJO.BookInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class AdminDashboardController {

    @FXML
    private Button addToLibraryButton;

    @FXML
    private ComboBox<?> bookComboBox;

    @FXML
    private TextField copyCountField;

    @FXML
    private Button declareButton;

    @FXML
    private ComboBox<?> libraryComboBox;

    @FXML
    private TextField newAuthorField;

    @FXML
    private TextField newGenreField;

    @FXML
    private TextField newIsbnField;

    @FXML
    private TextField newTitleField;

    @FXML
    private DatePicker newDateField;



    public void handleDeclareBook(ActionEvent actionEvent) {
        BookInfo bookInfo  = getBookInfo();
        BookInfoDao bookInfoDAO = new BookInfoDao();
        bookInfoDAO.insert(bookInfo);
    }



    private BookInfo getBookInfo() {
        String author = newAuthorField.getText();
        String genre = newGenreField.getText();
        String isbn = newIsbnField.getText();
        String title = newTitleField.getText();
        LocalDate year = newDateField.getValue();
        return new BookInfo(isbn, author, genre, title, year);
    }

    public void handleAddToLibrary(ActionEvent actionEvent) {
    }
}
