import DAO.*;
import POJO.Member;
import POJO.Singleton.GlobalDAO;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Library App");
			primaryStage.show();

			//initialize all daos
			BookDAO bookDAO = new BookDAO();
			BookInfoDao  bookInfoDao = new BookInfoDao();
			BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
			LibraryDao libraryDao = new LibraryDao();
			MemberDAO memberDAO = new MemberDAO();
			//initialize the global singelton
			GlobalDAO.getInstance().setAll(bookDAO, bookInfoDao, libraryDao, memberDAO, borrowRecordDAO);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		launch(args);
	}
}
