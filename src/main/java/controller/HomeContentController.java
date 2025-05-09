package controller;

import DAO.BookDAO;
import DAO.BookInfoDao;
import DAO.BorrowRecordDAO;
import POJO.Book;
import POJO.BookInfo;
import POJO.BorrowRecord;
import POJO.Singleton.CommonObjs;
import POJO.Singleton.GlobalDAO;
import POJO.Singleton.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class HomeContentController {

    public Label welcomeLabel;
    public TextField searchField;
    @FXML
    private ListView<String> dueSoonList;
    private CommonObjs commonObjs = CommonObjs.getInstance();

    private final ObservableList<BorrowRecord> masterList = FXCollections.observableArrayList();
    private FilteredList<BorrowRecord> filteredList;

    public void initialize() {
        welcomeLabel.setText("Welcome back, " + Session.get().getMember().getFirstName() + "!");

        BorrowRecordDAO brDao = GlobalDAO.getInstance().getBorrowRecordDAO();
        BookDAO boDao = GlobalDAO.getInstance().getBookDAO();
        BookInfoDao biDao = GlobalDAO.getInstance().getBookInfoDAO();
        int memberId = Session.get().getMember().getMemberId();

        List<BorrowRecord> recs = brDao.getMemberBorrow(memberId);
        masterList.setAll(recs);
        // … your existing home-page setup …

        // Populate “Due Soon”
        List<String> soon = masterList.stream()
                .filter(br -> {
                    LocalDate due = br.getReturnDate().toLocalDate();
                    long days = ChronoUnit.DAYS.between(LocalDate.now(), due);
                    return days >= 0 && days <= 3 && !br.isReturned();
                })
                .map(br -> {
                    Book book = boDao.get(br.getBookID());
                    BookInfo info = biDao.get(book.getBookInfoID());
                    long days = ChronoUnit.DAYS.between(LocalDate.now(),
                            br.getReturnDate().toLocalDate());
                    return info.getTitle() + " (due in " + days + " days)";
                })
                .collect(Collectors.toList());
        dueSoonList.setItems(FXCollections.observableArrayList(soon));
    }


    public void handelSearch(ActionEvent actionEvent) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) return;

        try {
            // 1) Load your Search.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Search.fxml"));
            Parent searchRoot = loader.load();

            // 2) Grab its controller and seed the basicSearchField
            SearchController sc = loader.getController();
            sc.basicSearchField.setText(query);
            sc.onBasicSearch(null);      // apply the filter immediately

            // 3) Swap it into your main layout (e.g. a BorderPane center)
            CommonObjs.getInstance()
                    .getBorderPane()
                    .setCenter(searchRoot);

        } catch (IOException e) {
            e.printStackTrace();
            // you might show an Alert here…
        }
    }
}
