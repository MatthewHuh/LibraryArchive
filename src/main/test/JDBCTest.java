
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class JDBCTest {
	public static void main(String args[]) {
		Dotenv dotenv = Dotenv
				.configure()
				.ignoreIfMissing()     // won’t crash if no .env found
				.load();

		String host     = dotenv.get("DB_HOST");
		String port     = dotenv.get("DB_PORT");
		String name     = dotenv.get("DB_NAME");
		String user     = dotenv.get("DB_USER");
		String password = dotenv.get("DB_PASSWORD");

		String url = String.format(
				"jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC",
				host, port, name
		);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection connection = DriverManager.getConnection(url,user,password);
			
			Statement statement = connection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS book_info " +
					"(book_info_id INTEGER not NULL, " + 
					" author VARCHAR(255) not NULL, " +
					" genre VARCHAR(255) not NULL, " +
					" title VARCHAR(255) not NULL, " +
					" release_date DATE not NULL, " +
					" PRIMARY KEY (book_info_id));";
			statement.executeUpdate(sql);
			System.out.println("Table created");
			
			sql = "CREATE TABLE IF NOT EXISTS books " 
					+ " (book_id INTEGER not NULL, "
					+ "book_info_id INTEGER not NULL, "
					+ "library_id INTEGER not NULL, "
					+ "PRIMARY KEY (book_id));";
			statement.executeUpdate(sql);
			System.out.println("Table created");
			
			sql = "CREATE TABLE IF NOT EXISTS members "
					+ "(member_id INTEGER not NULL, "
					+ "first_name VARCHAR(50) not NULL, "
					+ "last_name VARCHAR(50) not NULL, "
					+ "phone_number CHAR(12) not NULL, "
					+ "email VARCHAR(62) not NULL, "
					+ "address VARCHAR(130) not NULL, "
					+ "hashed_password VARCHAR(255) not NULL, " 
					+ "PRIMARY KEY (member_id));";
			statement.executeUpdate(sql);
			System.out.println("Table created");
			
			sql = "CREATE TABLE IF NOT EXISTS library " 
					+ "(library_id INTEGER not NULL, "
					+ "address VARCHAR(250) not NULL, "
					+ "name VARCHAR(250) not NULL, "
					+ "PRIMARY KEY (library_id));";
			statement.executeUpdate(sql);
			System.out.println("Table created");
			
			sql = "CREATE TABLE IF NOT EXISTS borrow_record " 
					+ "(borrow_record_id INTEGER not NULL, "
					+ "member_id INTEGER not NULL, "
					+ "book_id INTEGER not NULL, "
					+ "late_fee INTEGER not NULL, "
					+ "return_date DATE not NULL, "
					+ "PRIMARY KEY (borrow_record_id));";
			statement.executeUpdate(sql);
			System.out.println("Table created");
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}
