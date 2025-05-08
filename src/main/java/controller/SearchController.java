package controller;

import DAO.BookDAO;
import DAO.BookInfoDao;
import POJO.BookInfo;
import POJO.Singleton.CommonObjs;
import POJO.Singleton.GlobalDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;



import java.io.IOException;
import java.net.URL;
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
    public ChoiceBox<String> genreBox;
    public TextField yearFromField;
    public TextField yearToField;

    public TextField basicSearchField;
    public CheckBox availableOnlyCheck;


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
        allBooks.addAll(initialBooks);
        filteredBooks = new FilteredList<>(allBooks, book -> true);
        resultsTable.setItems(filteredBooks);
        resultsTable.setOnMouseClicked(this::handleRowClick);

        genreBox.getItems().addAll(bookInfoDao.getGenre());
    }


    private void handleRowClick(javafx.scene.input.MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2 || mouseEvent.getClickCount() == 1) {
            BookInfo selectedBook = resultsTable.getSelectionModel().getSelectedItem();
            try {
                URL url = getClass().getResource("/view/BorrowBook.fxml");
                FXMLLoader loader = new FXMLLoader(url);
                BorrowBookController controller = new BorrowBookController(selectedBook);
                loader.setController(controller);
                Parent root = loader.load();

                CommonObjs.getInstance().getBorderPane().setCenter(root);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
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

        availableOnlyCheck.setSelected(false);
    }

    @FXML
    public void onApplyAdvanced(ActionEvent actionEvent) {
        String titleFilter = titleField.getText().toLowerCase();
        String authorFilter = authorField.getText().toLowerCase();
        String isbnFilter = isbnField.getText().toLowerCase();
        String genreFilter = (String) genreBox.getValue();
        String yearFromFilter = yearFromField.getText();
        String yearToFilter = yearToField.getText();
        boolean availOnly = availableOnlyCheck.isSelected();

        BookDAO bookDAO = GlobalDAO.getInstance().getBookDAO();

        filteredBooks.setPredicate(book -> {
            boolean titleMatch = titleFilter.isEmpty() || book.getTitle().toLowerCase().contains(titleFilter);
            boolean authorMatch = authorFilter.isEmpty() || book.getAuthor().toLowerCase().contains(authorFilter);
            boolean isbnMatch = isbnFilter.isEmpty() || book.getISBN().toLowerCase().contains(isbnFilter);
            boolean genreMatch = genreFilter == null || genreFilter.isEmpty() || book.getGenre().equals(genreFilter);
            boolean availableMatch = true;
            if (availableOnlyCheck.isSelected()) {
                availableMatch = bookDAO.hasAvailableCopies(book.getISBN());
            }

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

            return titleMatch && authorMatch && isbnMatch && genreMatch && yearMatch && availableMatch;
        });
    }

}
