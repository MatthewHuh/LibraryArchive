package controller;

import DAO.BookInfoDao;
import DAO.BorrowRecordDAO;
import POJO.BookInfo;
import POJO.BorrowDisplayObject;
import POJO.BorrowRecord;
import POJO.Member;
import POJO.Singleton.CommonObjs;
import POJO.Singleton.GlobalDAO;
import POJO.Singleton.Session;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class BorrowBookController {
    public TableView<BorrowDisplayObject> borrowBookTable;

    public TableColumn colBorrowTitle;
    public TableColumn colBorrowISBN;
    public TableColumn colBorrowDueDate;
    public TableColumn colLibraryName;
    public TableColumn colLibraryAddress;
    public TableColumn<BorrowDisplayObject, Boolean> colIsAvailable;

    public TextField borrowStatusField;

    private BookInfo currentBook;

    private FilteredList<BorrowDisplayObject> filteredBooks;

    private ObservableList<BorrowDisplayObject> borrowableBooksList = FXCollections.observableArrayList();

    public BorrowBookController(BookInfo currentBook) {
        this.currentBook = currentBook;
    }


    public void handleReturnToBrowsingButton(ActionEvent actionEvent) {
        try{
            URL url = getClass().getResource("/view/Search.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            CommonObjs.getInstance().getBorderPane().setCenter(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public void handleBorrowButton(ActionEvent actionEvent) {
        BorrowDisplayObject selectedRecord = borrowBookTable.getSelectionModel().getSelectedItem();
        if (selectedRecord != null && selectedRecord.getAvailable()) {
            Member user = Session.get().getMember();
            BorrowRecord borrowRecord = new BorrowRecord(null, user.getMemberId(), selectedRecord.getBookID(), selectedRecord.getReturnDate(), false);
            GlobalDAO.getInstance().getBorrowRecordDAO().insert(borrowRecord);
            borrowStatusField.textProperty().setValue("Successfully borrowed Book");
            borrowBookTable.getSelectionModel().getSelectedItem().setAvailable(false);
            borrowBookTable.refresh();
        }
        else if (selectedRecord != null && !selectedRecord.getAvailable()){
            borrowStatusField.textProperty().setValue("Book is unavailable");
        } else {
            borrowStatusField.textProperty().setValue("Please select a book to borrow.");
        }
    }



    public void initialize() {
        colBorrowTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colBorrowISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colLibraryName.setCellValueFactory(new PropertyValueFactory<>("LibraryName"));
        colLibraryAddress.setCellValueFactory(new PropertyValueFactory<>("LibraryAddress"));
        colBorrowDueDate.setCellValueFactory(new PropertyValueFactory<>("ReturnDate"));
        colIsAvailable.setCellValueFactory(cellData -> new SimpleBooleanProperty(!cellData.getValue().getAvailable()));
        colIsAvailable.setCellFactory(tc -> new CheckBoxTableCell<>());
        colIsAvailable.setEditable(false);



        List<BorrowDisplayObject> borrowDisplayObjects = GlobalDAO.getInstance().getBorrowRecordDAO().getBorrowDisplayObject(currentBook.getISBN());
        borrowableBooksList.addAll(borrowDisplayObjects);
        filteredBooks = new FilteredList<>(borrowableBooksList, BorrowDisplayObject::getAvailable);
        borrowBookTable.setItems(filteredBooks);
    }



    public void setBookInfo(BookInfo book) {
        this.currentBook = book;
    }
}