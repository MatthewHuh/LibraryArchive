package controller;

import DAO.BookInfoDao;
import DAO.BorrowRecordDAO;
import POJO.BookInfo;
import POJO.BorrowDisplayObject;
import POJO.BorrowRecord;
import POJO.Member;
import POJO.Singleton.GlobalDAO;
import POJO.Singleton.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class BorrowBookController {
    public TableView<BorrowDisplayObject> borrowBookTable;
    public TableColumn colBorrowTitle;
    public TableColumn colBorrowISBN;
    public TableColumn colBorrowDueDate;
    public TableColumn colLibraryName;
    public TableColumn colLibraryAddress;

    private BookInfo currentBook;
    private BorrowRecordDAO borrowRecordDAO;

    public BorrowBookController(BookInfo currentBook) {
        this.currentBook = currentBook;
    }
    private ObservableList<BorrowDisplayObject> borrowableBooksLists = FXCollections.observableArrayList();


    public void handleBorrowButton(ActionEvent actionEvent) {
        BorrowDisplayObject selectedRecord = borrowBookTable.getSelectionModel().getSelectedItem();
        System.out.println(selectedRecord.getBookID());
        Member user = Session.get().getMember();

        BorrowRecord borrowRecord = new BorrowRecord(null, user.getMemberId(), selectedRecord.getBookID(), 0, selectedRecord.getReturnDate());
        borrowRecordDAO.insert(borrowRecord);
    }



    public void initialize() {
        colBorrowTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colBorrowISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colLibraryName.setCellValueFactory(new PropertyValueFactory<>("LibraryName"));
        colLibraryAddress.setCellValueFactory(new PropertyValueFactory<>("LibraryAddress"));
        colBorrowDueDate.setCellValueFactory(new PropertyValueFactory<>("ReturnDate"));
        this.borrowRecordDAO = GlobalDAO.getInstance().getBorrowRecordDAO();

        if(currentBook != null) {
            List<BorrowDisplayObject> borrowDisplayObjects = borrowRecordDAO.getBorrowDisplayObject(currentBook.getISBN());
            if (borrowDisplayObjects != null) {
                borrowableBooksLists.addAll(borrowDisplayObjects);
            }
        }


        borrowBookTable.setItems(borrowableBooksLists);
    }
    public void setBookInfo(BookInfo book) {
        this.currentBook = book;
        System.out.println(book.getISBN());
    }
}