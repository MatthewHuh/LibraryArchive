package controller;

import DAO.BorrowRecordDAO;
import POJO.BookInfo;
import POJO.BorrowDisplayObject;
import POJO.Singleton.GlobalDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class BorrowBookController {
    @FXML
    public TableView<BorrowDisplayObject> borrowBookTable;
    @FXML
    public TableColumn<BorrowDisplayObject, String> colBorrowTitle;
    @FXML
    public TableColumn<BorrowDisplayObject, String> colBorrowISBN;
    @FXML
    public TableColumn<BorrowDisplayObject, String> colBorrowDueDate;
    @FXML
    public TableColumn<BorrowDisplayObject, String> colLibraryName;
    @FXML
    public TableColumn<BorrowDisplayObject, String> colLibraryAddress;
    private BookInfo currentBook;

    private ObservableList<BorrowDisplayObject> borrowableBooksLists = FXCollections.observableArrayList();


    public void handleBorrowButton(ActionEvent actionEvent) {
        // Implement borrow logic here, using currentBook if needed
    }

    public void setBookInfo(BookInfo book) {
        this.currentBook = book;
        // Load the borrowable books now that we have the currentBook
        loadBorrowableBooks();
    }

    public void initialize() {
        colBorrowTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colBorrowISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colLibraryName.setCellValueFactory(new PropertyValueFactory<>("LibraryName"));
        colLibraryAddress.setCellValueFactory(new PropertyValueFactory<>("LibraryAddress"));
        colBorrowDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate")); // Assuming "dueDate" property

        borrowBookTable.setItems(borrowableBooksLists);
    }

    private void loadBorrowableBooks() {
        if (currentBook != null) {
            BorrowRecordDAO borrowRecordDAO = GlobalDAO.getInstance().getBorrowRecordDAO();
            List<BorrowDisplayObject> borrowDisplayObjects = borrowRecordDAO.getBorrowDisplayObject(currentBook.getISBN());
            if (borrowDisplayObjects != null) {
                borrowableBooksLists.clear();
                borrowableBooksLists.addAll(borrowDisplayObjects);
            } else {
                borrowableBooksLists.clear(); // Ensure the table is empty if no data
            }
        } else {
            System.err.println("Error: currentBook is null while trying to load borrowable books.");
            borrowableBooksLists.clear();
        }
    }
}