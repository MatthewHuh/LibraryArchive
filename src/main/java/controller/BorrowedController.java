package controller;

import DAO.BookDAO;
import DAO.BookInfoDao;
import DAO.BorrowRecordDAO;
import POJO.BookInfo;
import POJO.BorrowRecord;
import POJO.Member;
import POJO.Singleton.GlobalDAO;
import POJO.Singleton.Session;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;

public class BorrowedController {
    @FXML
    public Button returnButton;

    @FXML
    public TextField statusField;

    @FXML
    public Button filterReturned;

    public TableColumn<BorrowRecord, Boolean> colReturned;

    @FXML
    private TableView<BorrowRecord> borrowedTable;

    @FXML
    private TableColumn<BorrowRecord, String> colAuthor;

    @FXML
    private TableColumn<BorrowRecord, Long> colDaysLate;

    @FXML
    private TableColumn<BorrowRecord, LocalDate> colDueDate;

    @FXML
    private TableColumn<BorrowRecord, String> colFee;

    @FXML
    private TableColumn<BorrowRecord, String> colTitle;

    @FXML
    private Label totalFeesLabel;

    private ObservableList<BorrowRecord> allBorrows = FXCollections.observableArrayList();
    private FilteredList<BorrowRecord> filteredList;

    @FXML
    private void initialize() {
        // DAOs
        BorrowRecordDAO brDao = GlobalDAO.getInstance().getBorrowRecordDAO();
        BookInfoDao    biDao = GlobalDAO.getInstance().getBookInfoDAO();
        BookDAO boDao = GlobalDAO.getInstance().getBookDAO();
        int memberId = Session.get().getMember().getMemberId();

        // 1) Title column: look up BookInfo by bookID
        colTitle.setCellValueFactory(cell -> {
            BorrowRecord br = cell.getValue();
            BookInfo info = biDao.get(boDao.get(br.getBookID()).getBookInfoID());
            return new SimpleStringProperty(info.getTitle());
        });

        // 2) Author column
        colAuthor.setCellValueFactory(cell -> {
            BorrowRecord br = cell.getValue();
            BookInfo info = biDao.get(boDao.get(br.getBookID()).getBookInfoID());
            return new SimpleStringProperty(info.getAuthor());
        });

        // 3) Due‐date column (returnDate in POJO)
        colDueDate.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getReturnDate().toLocalDate())
        );

        // 4) Days late (0 if not late)
        colDaysLate.setCellValueFactory(cell -> {
            LocalDate due = cell.getValue().getReturnDate().toLocalDate();
            long daysLate = ChronoUnit.DAYS.between(due, LocalDate.now());
            return new SimpleLongProperty(Math.max(0, daysLate)).asObject();
        });

        // 5) Fee = daysLate * 0.15, formatted as currency
        colFee.setCellValueFactory(cell -> {
            LocalDate due = cell.getValue().getReturnDate().toLocalDate();
            long daysLate = ChronoUnit.DAYS.between(due, LocalDate.now());
            double fee = daysLate > 0 ? daysLate * 0.15 : 0;
            String fmt = NumberFormat.getCurrencyInstance().format(fee);
            return new SimpleStringProperty(fmt);
        });

        colReturned.setCellValueFactory(cell ->
                new SimpleBooleanProperty(cell.getValue().isReturned()).asObject()
        );

        colReturned.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean returned, boolean empty) {
                super.updateItem(returned, empty);
                setText(empty ? "" : (returned ? "Yes" : "No"));
            }
        });

        // Load & show only this member’s borrow records
        List<BorrowRecord> recs = brDao.getMemberBorrow(memberId);
        allBorrows.setAll(recs);
        borrowedTable.setItems(allBorrows);

        // Start showing only not returned
        filteredList = new FilteredList<>(allBorrows, br -> !br.isReturned());
        borrowedTable.setItems(filteredList);
        filterReturned.setText("Show Returned");

        // Compute total fees
        double total = recs.stream()
                .mapToDouble(br -> {
                    long daysLate = ChronoUnit.DAYS.between(
                            br.getReturnDate().toLocalDate(),
                            LocalDate.now());
                    return daysLate > 0 ? daysLate * 0.15 : 0;
                })
                .sum();
        totalFeesLabel.setText(NumberFormat.getCurrencyInstance().format(total));

        // Disable return button unless an un-returned row is selected
        returnButton.setDisable(true);
        borrowedTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean canReturn = newSel != null && !newSel.isReturned();
            returnButton.setDisable(!canReturn);
            statusField.clear();
        });

    }

    public void handleReturn(ActionEvent actionEvent) {
        BorrowRecordDAO brDao = GlobalDAO.getInstance().getBorrowRecordDAO();
        BorrowRecord br = borrowedTable.getSelectionModel().getSelectedItem();
        if (br == null || br.isReturned()) return;

        // 1) Update the DB
        br.setIsReturned(true);
        brDao.update(br);

        // 2) Update the local object & refresh table
        borrowedTable.refresh();

        // 3) Disable button & show status
        returnButton.setDisable(true);
        statusField.setText("Returned on " + LocalDate.now());
    }

    public void handleFilter(ActionEvent actionEvent) {
        if ("Show Returned".equals(filterReturned.getText())) {
            filteredList.setPredicate(br -> true);
            filterReturned.setText("Hide Returned");
        } else {
            filteredList.setPredicate(br -> !br.isReturned());
            filterReturned.setText("Show Returned");
        }
    }
}
