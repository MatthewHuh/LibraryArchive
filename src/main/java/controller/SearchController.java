package controller;

import DAO.BookInfoDao;
import POJO.BookInfo;
import POJO.Singleton.GlobalDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class SearchController  {

    @FXML
    public TableView<BookInfo> resultsTable;

    @FXML
    public TableColumn<BookInfo, String> colTitle;

    @FXML
    public TableColumn<BookInfo, String> colAuthor;

    @FXML
    public TableColumn<BookInfo, String> colISBN;

    @FXML
    public TableColumn<BookInfo, String> colGenre;

    @FXML
    public TableColumn<BookInfo, LocalDate> colYear;

    public TextField titleField;
    public TextField authorField;
    public TextField isbnField;
    public ChoiceBox genreBox;
    public TextField yearFromField;
    public TextField yearToField;

    public TextField basicSearchField;


    private ObservableList<BookInfo> allBooks = FXCollections.observableArrayList();
    private FilteredList<BookInfo> filteredBooks;



    public void initialize() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        resultsTable.setItems(allBooks);

        BookInfoDao bookInfoDao = GlobalDAO.getInstance().getBookInfoDAO();
        List<BookInfo> initialBooks = bookInfoDao.getAll();
        if (initialBooks != null) {
            allBooks.addAll(initialBooks);
        }


        filteredBooks = new FilteredList<>(allBooks, p -> true);
        resultsTable.setItems(filteredBooks);
    }

    public void onBasicSearch(ActionEvent actionEvent) {
        String titleFilter = basicSearchField.getText().toLowerCase();
        if (titleFilter == null || titleFilter.isEmpty()) {
            filteredBooks.setPredicate(book -> true);
        } else {
            String lowerCaseSearchText = titleFilter.toLowerCase();
            filteredBooks.setPredicate(book -> book.getTitle().toLowerCase().contains(lowerCaseSearchText));
        }

    }
    
    public void onClearAdvanced(ActionEvent actionEvent) {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        yearFromField.clear();
        yearToField.clear();

        if (!genreBox.getItems().isEmpty()) {
            genreBox.setValue(null);
        }

        filteredBooks.setPredicate(null);
    }

    @FXML
    public void onApplyAdvanced(ActionEvent actionEvent) {
        String titleFilter = titleField.getText().toLowerCase();
        String authorFilter = authorField.getText().toLowerCase();
        String isbnFilter = isbnField.getText().toLowerCase();
        String genreFilter = (String) genreBox.getValue();
        String yearFromFilter = yearFromField.getText();
        String yearToFilter = yearToField.getText();

        filteredBooks.setPredicate(book -> {
            boolean titleMatch = titleFilter.isEmpty() || book.getTitle().toLowerCase().contains(titleFilter);
            boolean authorMatch = authorFilter.isEmpty() || book.getAuthor().toLowerCase().contains(authorFilter);
            boolean isbnMatch = isbnFilter.isEmpty() || book.getISBN().toLowerCase().contains(isbnFilter);
            boolean genreMatch = genreFilter == null || genreFilter.isEmpty() || book.getGenre().equals(genreFilter);

            int fromYear = -1;
            try {
                if (!yearFromFilter.isEmpty()) {
                    fromYear = Integer.parseInt(yearFromFilter);
                }
            } catch (NumberFormatException e) {

            }

            int toYear = -1;
            try {
                if (!yearToFilter.isEmpty()) {
                    toYear = Integer.parseInt(yearToFilter);
                }
            } catch (NumberFormatException e) {

            }

            int bookYear = book.getReleaseDate().getYear();
            boolean yearMatch = (yearFromFilter.isEmpty() || bookYear >= fromYear) &&
                    (yearToFilter.isEmpty() || bookYear <= toYear);

            return titleMatch && authorMatch && isbnMatch && genreMatch && yearMatch;
        });
    }
}
