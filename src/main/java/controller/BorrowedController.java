package controller;

import DAO.BookDAO;
import DAO.BookInfoDao;
import DAO.BorrowRecordDAO;
import POJO.BookInfo;
import POJO.BorrowRecord;
import POJO.Member;
import POJO.Singleton.GlobalDAO;
import POJO.Singleton.Session;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;

public class BorrowedController {
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

        // Load & show only this member’s borrow records
        List<BorrowRecord> recs = brDao.getMemberBorrow(memberId);
        allBorrows.setAll(recs);
        borrowedTable.setItems(allBorrows);

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

    }
}
