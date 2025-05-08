
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

public class JDBCTest {
	public static void main(String[] args) {
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
			Connection conn = DriverManager.getConnection(url, user, password);
			insertBookInfo(conn);
			insertMembers(conn);
			Map<String,Integer> libs = insertLibraries(conn);
			insertBooks(conn, libs);
			insertBorrowRecords(conn);
			System.out.println("Dummy data loaded successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
    }

	private static void insertBookInfo(Connection conn) throws SQLException {
		String sql = "INSERT IGNORE INTO book_info (isbn, author, genre, title, release_date) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			Object[][] books = {
					{"9780140449136", "Homer", "Epic", "The Odyssey", LocalDate.of(1998,11,1)},
					{"9780439139601", "J.K. Rowling", "Fantasy", "Harry Potter and the Goblet of Fire", LocalDate.of(2000,7,8)},
					{"9780061120084", "Harper Lee", "Fiction", "To Kill a Mockingbird", LocalDate.of(2006,5,23)}
			};
			for (Object[] b : books) {
				ps.setString(1, (String)b[0]);
				ps.setString(2, (String)b[1]);
				ps.setString(3, (String)b[2]);
				ps.setString(4, (String)b[3]);
				ps.setDate(5, Date.valueOf((LocalDate)b[4]));
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}

	private static void insertMembers(Connection conn) throws SQLException {
		String sql = "INSERT IGNORE INTO members (first_name, last_name, phone_number, email, address, hashed_password, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			Object[][] users = {
					{"Alice",   "Anderson", "5551234567", "alice@example.com",   "123 Maple St, Anytown CA 12345", "password1", false},
					{"Bob",     "Brown",    "5559876543", "bob@example.com",     "456 Oak Ave, Othertown NY 67890", "password2", false},
					{"Admin",   "User",     "5550000000", "admin@example.com",   "789 Pine Rd, Admintown TX 54321", "adminpass",   true}
			};
			for (Object[] u : users) {
				String rawPwd = (String)u[5];
				String hash   = BCrypt.hashpw(rawPwd, BCrypt.gensalt(12));

				ps.setString(1, (String)u[0]);
				ps.setString(2, (String)u[1]);
				ps.setString(3, (String)u[2]);
				ps.setString(4, (String)u[3]);
				ps.setString(5, (String)u[4]);
				ps.setString(6, hash);
				ps.setBoolean(7, (Boolean)u[6]);
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}

	private static Map<String,Integer> insertLibraries(Connection conn) throws SQLException {
		String sql = "INSERT IGNORE INTO libraries (address, name) VALUES (?, ?)";
		Map<String,Integer> map = new HashMap<>();
		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			Object[][] libs = {
					{"100 Main Library Way, Metropolis NY 10001", "Central Library"},
					{"200 Westside Blvd, Metro NY 10002",       "Westside Branch"}
			};
			for (Object[] l : libs) {
				ps.setString(1, (String)l[0]);
				ps.setString(2, (String)l[1]);
				ps.executeUpdate();
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						map.put((String)l[1], rs.getInt(1));
					}
				}
			}
		}
		return map;
	}

	private static void insertBooks(Connection conn, Map<String,Integer> libs) throws SQLException {
		String sql = "INSERT IGNORE INTO books (isbn, library_id, is_available) VALUES (?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			String[] isbns = {"9780140449136", "9780439139601", "9780061120084"};
			int centralId = libs.get("Central Library");
			int westId    = libs.get("Westside Branch");

			// first two copies at central, one at west
			ps.setString(1, isbns[0]); ps.setInt(2, centralId); ps.setBoolean(3, true); ps.addBatch();
			ps.setString(1, isbns[1]); ps.setInt(2, centralId); ps.setBoolean(3, true); ps.addBatch();
			ps.setString(1, isbns[2]); ps.setInt(2, westId);    ps.setBoolean(3, true); ps.addBatch();

			ps.executeBatch();
		}
	}

	private static void insertBorrowRecords(Connection conn) throws SQLException {
		String sql = "INSERT IGNORE INTO borrow_record (member_id, book_id, return_date, is_returned) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			Object[][] recs = {
					{1, 1, LocalDate.now().plusDays(14), false},
					{2, 2, LocalDate.now().minusDays(2), true}
			};
			for (Object[] r : recs) {
				ps.setInt(1, (Integer)r[0]);
				ps.setInt(2, (Integer)r[1]);
				ps.setDate(3, Date.valueOf((LocalDate)r[2]));
				ps.setBoolean(4, (Boolean)r[3]);
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}
}


	/*
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

			Connection connection = DriverManager.getConnection(url, user, password);

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
	}*/
