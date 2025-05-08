package controller;

import DAO.BookInfoDao;
import POJO.*;
import POJO.Singleton.GlobalDAO;
import POJO.Singleton.Session;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.List;
import java.util.function.UnaryOperator;

public class AdminDashboardController {

    //delete form components
    @FXML
    private TextField deleteIsbnField;
    @FXML
    private TextField deleteStatusField;
    @FXML
    private TableColumn colBorrowTitle;
    @FXML
    private TableColumn colBorrowISBN;
    @FXML
    private TableColumn colLibraryName;
    @FXML
    private TableColumn colLibraryAddress;
    @FXML
    public TableColumn<BorrowDisplayObject, Boolean> colIsAvailable;
    @FXML
    private TableView<BorrowDisplayObject> bookTable;


    private FilteredList<BorrowDisplayObject> deletableBooksFiltered;

    private ObservableList<BorrowDisplayObject> deletableBooks = FXCollections.observableArrayList();

    @FXML
    private Button addEditButton;

    @FXML
    private Button addToLibraryButton;

    @FXML
    private Label bookInfoStatusField;

    @FXML
    private Label bookInfoStatusLable;

    @FXML
    private Label bookStatusField;

    @FXML
    private Label bookStatusLable;

    @FXML
    private Button declareButton;

    @FXML
    private Label editAuthorError;

    @FXML
    private TextField editAuthorField;

    @FXML
    private Label editDateError;

    @FXML
    private DatePicker editDateField;

    @FXML
    private Label editGenreError;

    @FXML
    private TextField editGenreField;

    @FXML
    private Label editIsbnError;

    @FXML
    private TextField editIsbnField;

    @FXML
    private Label editStatusField;

    @FXML
    private Label editStatusLable;

    @FXML
    private Label editTitleError;

    @FXML
    private TextField editTitleField;

    @FXML
    private Label isbnError;

    @FXML
    private TextField isbnField;

    @FXML
    private ComboBox<Library> libraryComboBox;

    @FXML
    private Label libraryError;

    @FXML
    private Label newAuthorError;

    @FXML
    private TextField newAuthorField;

    @FXML
    private Label newDateError;

    @FXML
    private DatePicker newDateField;

    @FXML
    private Label newGenreError;

    @FXML
    private TextField newGenreField;

    @FXML
    private Label newIsbnError;

    @FXML
    private TextField newIsbnField;

    @FXML
    private Label newTitleError;

    @FXML
    private TextField newTitleField;


    @FXML
    public void initialize() {
        UnaryOperator<TextFormatter.Change> authorFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("[a-zA-Z ,]{0,255}") ? change : null;
        };
        newAuthorField.setTextFormatter(new TextFormatter<>(authorFilter));
        editAuthorField.setTextFormatter(new TextFormatter<>(authorFilter));

        UnaryOperator<TextFormatter.Change> titleFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("[a-zA-Z ,1234567890]{0,255}") ? change : null;
        };
        newTitleField.setTextFormatter(new TextFormatter<>(titleFilter));
        editTitleField.setTextFormatter(new TextFormatter<>(titleFilter));

        UnaryOperator<TextFormatter.Change> isbnFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d{0,13}") ? change : null;
        };
        newIsbnField.setTextFormatter(new TextFormatter<>(isbnFilter));
        isbnField.setTextFormatter(new TextFormatter<>(isbnFilter));
        editIsbnField.setTextFormatter(new TextFormatter<>(isbnFilter));

        UnaryOperator<TextFormatter.Change> genreFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("[a-zA-Z ]{0,50}") ? change : null;
        };
        newGenreField.setTextFormatter(new TextFormatter<>(genreFilter));
        editGenreField.setTextFormatter(new TextFormatter<>(genreFilter));

        List<Library> libraries = GlobalDAO.getInstance().getLibraryDao().getAll();
        for (Library library : libraries) {
            libraryComboBox.getItems().add(library);
        }

        //delete table initialization
        colBorrowTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colBorrowISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colLibraryName.setCellValueFactory(new PropertyValueFactory<>("LibraryName"));
        colLibraryAddress.setCellValueFactory(new PropertyValueFactory<>("LibraryAddress"));
        colIsAvailable.setCellValueFactory(cellData -> new SimpleBooleanProperty(!cellData.getValue().getAvailable()));
        colIsAvailable.setCellFactory(tc -> new CheckBoxTableCell<>());
        colIsAvailable.setEditable(false);
    }

    private boolean isInputBookInfo() {
        boolean flag = true;
        if(newAuthorField.getText().trim().isEmpty()) {
            newAuthorError.setText("Please enter the author name(s)");
            newAuthorError.setVisible(true);
            flag = false;
        } else {newAuthorError.setVisible(false);}
        if(newGenreField.getText().trim().isEmpty()) {
            newGenreError.setText("Please enter the genre");
            newGenreError.setVisible(true);
            flag = false;
        } else {newGenreError.setVisible(false);}
        if(newIsbnField.getText().trim().isEmpty()) {
            newIsbnError.setText("Please enter the ISBN");
            newIsbnError.setVisible(true);
            flag = false;
        } else {newIsbnError.setVisible(false);}
        if(newTitleField.getText().trim().isEmpty()) {
            newTitleError.setText("Please enter the title");
            newTitleError.setVisible(true);
            flag = false;
        }  else {newTitleError.setVisible(false);}
        if(newDateField.getValue() == null) {
            newDateError.setText("Please enter the date");
            newDateError.setVisible(true);
            flag = false;
        }  else {newDateError.setVisible(false);}
        return flag;
    }

    private boolean isValidBookInfo() {
        if(newIsbnField.getText().trim().length() != 13) {
            newIsbnError.setText("Please enter a valid ISBN");
            newIsbnError.setVisible(true);
            return false;
        }
        if(GlobalDAO.getInstance().getBookInfoDAO().get(newIsbnField.getText()) != null) {
            newIsbnError.setText("ISBN taken, book already exists");
            newIsbnError.setVisible(true);
            return false;
        }
        return true;
    }

    private boolean isInputBook() {
        boolean flag = true;
        if(isbnField.getText().trim().isEmpty()) {
            isbnError.setText("Please enter an ISBN number");
            isbnError.setVisible(true);
            flag = false;
        } else {isbnError.setVisible(false);}
        if(libraryComboBox.getValue() == null) {
            libraryError.setText("Please enter the library name");
            libraryError.setVisible(true);
            flag = false;
        } else {libraryError.setVisible(false);}
        return flag;
    }

    private boolean isValidBook() {
        if(isbnField.getText().trim().length() != 13) {
            isbnError.setText("Please enter a valid ISBN");
            isbnError.setVisible(true);
            return false;
        }
        if(GlobalDAO.getInstance().getBookInfoDAO().get(isbnField.getText()) == null) {
            isbnError.setText("Book Not Found");
            isbnError.setVisible(true);
            return false;
        }
        return true;
    }

    private boolean isInputEdit() {
        boolean flag = true;
        if(editIsbnField.getText().trim().isEmpty()) {
            editIsbnError.setText("Please enter an ISBN");
            editIsbnError.setVisible(true);
            flag = false;
        }  else {editIsbnError.setVisible(false);}
        if(editTitleField.getText().trim().isEmpty() &&
            editAuthorField.getText().trim().isEmpty() &&
            editGenreField.getText().trim().isEmpty() &&
            editDateField.getValue() == null) {
            editTitleError.setText("Please enter the title or");
            editTitleError.setVisible(true);
            editAuthorError.setText("Please enter the author or");
            editAuthorError.setVisible(true);
            editGenreError.setText("Please enter the genre");
            editGenreError.setVisible(true);
            editDateError.setText("Please enter the date");
            editDateError.setVisible(true);
            flag = false;
        } else {
            editTitleError.setVisible(false);
            editAuthorError.setVisible(false);
            editGenreError.setVisible(false);
            editDateError.setVisible(false);
        }
        return flag;
    }

    private boolean isValidEdit() {
        if(editIsbnField.getText().trim().length() != 13) {
            editIsbnError.setText("Please enter a valid ISBN");
            editIsbnError.setVisible(true);
            return false;
        }
        if(GlobalDAO.getInstance().getBookInfoDAO().get(editIsbnField.getText()) == null) {
            editIsbnError.setText("Book Not Found");
            editIsbnError.setVisible(true);
            return false;
        }
        return true;
    }

    public void handleDeclareBook(ActionEvent actionEvent) {
        bookInfoStatusField.setVisible(false);
        if(isInputBookInfo() && isValidBookInfo()) {
            BookInfo bookInfo = getBookInfo();
            if(GlobalDAO.getInstance().getBookInfoDAO().insert(bookInfo) == 1) {
                bookInfoStatusField.setText("Book Info inserted successfully");
                bookInfoStatusField.setVisible(true);
                newAuthorField.clear();
                newGenreField.clear();
                newIsbnField.clear();
                newTitleField.clear();
                newDateField.setValue(null);
            } else {
                bookInfoStatusField.setText("Book Info insert failed");
                bookInfoStatusField.setVisible(true);
            }
        }
    }

    private BookInfo getBookInfo() {
        String author = newAuthorField.getText().trim();
        String genre = newGenreField.getText().trim();
        String isbn = newIsbnField.getText().trim();
        String title = newTitleField.getText().trim();
        LocalDate year = newDateField.getValue();
        return new BookInfo(isbn, author, genre, title, year);
    }

    public void handleAddToLibrary(ActionEvent actionEvent) {
        bookStatusField.setVisible(false);
        if(isInputBook() && isValidBook()) {
            Book book = getBook();
            if(GlobalDAO.getInstance().getBookDAO().insert(book) == 1) {
                bookStatusField.setText("Book Info inserted successfully");
                bookStatusField.setVisible(true);
                isbnField.clear();
                libraryComboBox.setValue(null);
            }
            else {
                bookStatusField.setText("Book Info insert failed");
                bookStatusField.setVisible(true);
            }
        }
    }

    private Book getBook() {
        String isbn = isbnField.getText().trim();
        Library library = libraryComboBox.getValue();
        return new Book(null,isbn, library.getLibraryID(), true);
    }

    public void handleAddEdit(ActionEvent actionEvent) {
        if(isInputEdit() && isValidEdit()) {
            String isbn = editIsbnField.getText().trim();
            BookInfo newBookInfo = getEditBookInfo(GlobalDAO.getInstance().getBookInfoDAO().get(isbn));
            if(GlobalDAO.getInstance().getBookInfoDAO().update(newBookInfo) == 1) {
                editStatusField.setText("Book Info updated successfully");
                editStatusField.setVisible(true);
                editIsbnField.clear();
                editTitleField.clear();
                editAuthorField.clear();
                editGenreField.clear();
                editDateField.setValue(null);
            }
            else {
                editStatusField.setText("Book Info update failed");
                editStatusField.setVisible(true);
            }
        }
    }

    private BookInfo getEditBookInfo (BookInfo bookInfo) {
        String isbn = bookInfo.getISBN();
        String title = bookInfo.getTitle();
        String author =  bookInfo.getAuthor();
        String genre =  bookInfo.getGenre();
        LocalDate year = bookInfo.getReleaseDate();

        if(!editGenreField.getText().trim().isEmpty()) {
            genre = editGenreField.getText().trim();
        }
        if(!editTitleField.getText().trim().isEmpty()) {
            title = editTitleField.getText().trim();
        }
        if(!editAuthorField.getText().trim().isEmpty()) {
            author = editAuthorField.getText().trim();
        }
        if(editDateField.getValue() != null) {
            year =  editDateField.getValue();
        }
        return new BookInfo(isbn, author, genre, title, year);
    }

    //delete books
    public void onBasicSearch(ActionEvent actionEvent) {
        String isbn = deleteIsbnField.getText().trim();
        List<BorrowDisplayObject> borrowDisplayObjects = GlobalDAO.getInstance().getBorrowRecordDAO().getBorrowDisplayObject(isbn);
        if (borrowDisplayObjects.isEmpty()) {
            deleteStatusField.setText("Books Not Found");
        }
        deletableBooks.addAll(borrowDisplayObjects);
        deletableBooksFiltered = new FilteredList<>(deletableBooks, p->true);
        bookTable.setItems(deletableBooksFiltered);
    }

    public void deleteBook(ActionEvent actionEvent) {
        BorrowDisplayObject selectedItem = bookTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            GlobalDAO.getInstance().getBookDAO().delete(selectedItem.getBookID());
            deletableBooks.remove(selectedItem);
            bookTable.getSelectionModel().clearSelection();
            deleteStatusField.setText("Book deleted successfully");
        }
        else{
            deleteStatusField.setText("Book already deleted successfully");
        }
    }
}
