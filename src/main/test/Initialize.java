import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class Initialize {
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
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
			try (Connection conn = DriverManager.getConnection(url, user, password)) {
				insertBookInfo(conn);
				insertMembers(conn);
				Map<String,Integer> libs = insertLibraries(conn);
				insertBooks(conn, libs);
				insertBorrowRecords(conn);
				System.out.println("Dummy data loaded successfully.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void insertBookInfo(Connection conn) throws SQLException {
		String sql = "INSERT IGNORE INTO book_info (isbn, author, genre, title, release_date) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			Object[][] books = {
					{"9780140449136","Homer","Epic","The Odyssey", LocalDate.of(1998,11,1)},
					{"9780439139601","J.K. Rowling","Fantasy","Harry Potter and the Goblet of Fire", LocalDate.of(2000,7,8)},
					{"9780061120084","Harper Lee","Fiction","To Kill a Mockingbird", LocalDate.of(2006,5,23)},
					{"9780743273565","F. Scott Fitzgerald","Classic","The Great Gatsby", LocalDate.of(2004,9,30)},
					{"9780451524935","George Orwell","Dystopia","1984", LocalDate.of(1950,6,1)},
					{"9780141439518","Jane Austen","Romance","Pride and Prejudice", LocalDate.of(2002,12,1)},
					{"9780007458424","J.R.R. Tolkien","Fantasy","The Hobbit", LocalDate.of(2012,11,6)},
					{"9781503280786","Herman Melville","Adventure","Moby Dick", LocalDate.of(2013,5,14)},
					{"9780199232765","Leo Tolstoy","Historical","War and Peace", LocalDate.of(2010,3,1)},
					{"9780316769488","J.D. Salinger","Fiction","The Catcher in the Rye", LocalDate.of(1991,5,1)},
					{"9780060850524","Aldous Huxley","Science Fiction","Brave New World", LocalDate.of(2006,10,17)},
					{"9780544003415","J.R.R. Tolkien","Fantasy","The Fellowship of the Ring", LocalDate.of(2012,11,6)},
					{"9780142437209","Charlotte Brontë","Gothic","Jane Eyre", LocalDate.of(2003,12,1)},
					{"9780140439848","Emily Brontë","Gothic","Wuthering Heights", LocalDate.of(2003,10,1)},
					{"9780066236096","C.S. Lewis","Fantasy","The Lion, the Witch and the Wardrobe", LocalDate.of(2002,9,1)},
					{"9780553293357","Frank Herbert","Science Fiction","Dune", LocalDate.of(1990,8,1)},
					{"9780142415436","Mark Twain","Adventure","Adventures of Huckleberry Finn", LocalDate.of(2003,4,1)},
					{"9781593080006","Mary Shelley","Horror","Frankenstein", LocalDate.of(2005,5,1)},
					{"9780486282114","Edgar Allan Poe","Poetry","The Raven and Other Poems", LocalDate.of(1999,9,1)},
					{"9780143105985","Miguel de Cervantes","Classic","Don Quixote", LocalDate.of(2003,11,1)}
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
					{"Alice","Anderson","5551000001","alice1@example.com","1 Main St, Townsville CA 90001","password1",false},
					{"Bob","Brown","5551000002","bob@example.com","2 Oak St, Townsville CA 90001","password2",false},
					{"Carol","Clark","5551000003","carol@example.com","3 Pine St, Townsville CA 90001","password3",false},
					{"Dave","Davis","5551000004","dave@example.com","4 Maple Ave, Townsville CA 90001","password4",false},
					{"Eve","Evans","5551000005","eve@example.com","5 Elm St, Townsville CA 90001","password5",false},
					{"Frank","Franklin","5551000006","frank@example.com","6 Cedar St, Townsville CA 90001","password6",false},
					{"Grace","Green","5551000007","grace@example.com","7 Birch St, Townsville CA 90001","password7",false},
					{"Heidi","Hunt","5551000008","heidi@example.com","8 Walnut St, Townsville CA 90001","password8",false},
					{"Ivan","Ives","5551000009","ivan@example.com","9 Ash St, Townsville CA 90001","password9",false},
					{"Judy","Jones","5551000010","judy@example.com","10 Poplar St, Townsville CA 90001","password10",false},
					{"Mallory","Moore","5551000011","mallory@example.com","11 Spruce St, Townsville CA 90001","password11",false},
					{"Neil","Nelson","5551000012","neil@example.com","12 Cherry St, Townsville CA 90001","password12",false},
					{"Olivia","Olsen","5551000013","olivia@example.com","13 Chestnut St, Townsville CA 90001","password13",false},
					{"Peggy","Peterson","5551000014","peggy@example.com","14 Redwood St, Townsville CA 90001","password14",false},
					{"Super","Admin","5550000000","admin@example.com","789 Pine Rd, Admintown TX 54321","adminpass",true}
			};
			for (Object[] u : users) {
				String raw = (String)u[5];
				String hash = BCrypt.hashpw(raw, BCrypt.gensalt(12));
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
					{"100 Main Library Way, Metropolis NY 10001","Central Library"},
					{"200 Westside Blvd, Metropolis NY 10002","Westside Branch"},
					{"300 Eastside Ave, Metropolis NY 10003","Eastside Branch"},
					{"400 North St, Metropolis NY 10004","Northside Branch"},
					{"500 South Rd, Metropolis NY 10005","Southside Branch"},
					{"10 Downtown Pl, Metropolis NY 10006","Downtown Library"},
					{"20 Uptown Pl, Metropolis NY 10007","Uptown Library"},
					{"30 Midtown Rd, Metropolis NY 10008","Midtown Library"},
					{"40 Campus Dr, Metropolis NY 10009","Campus Library"},
					{"50 Community Cir, Metropolis NY 10010","Community Library"},
					{"60 Children's Ln, Metropolis NY 10011","Children's Library"},
					{"70 Reference Way, Metropolis NY 10012","Reference Library"},
					{"80 Mobile Blvd, Metropolis NY 10013","Mobile Library"},
					{"90 Digital Ave, Metropolis NY 10014","Digital Library"},
					{"100 Archive St, Metropolis NY 10015","Archive"}
			};
			for (Object[] l : libs) {
				ps.setString(1, (String)l[0]);
				ps.setString(2, (String)l[1]);
				ps.executeUpdate();
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) map.put((String)l[1], rs.getInt(1));
				}
			}
		}
		return map;
	}

	private static void insertBooks(Connection conn, Map<String,Integer> libs) throws SQLException {
		String sql = "INSERT IGNORE INTO books (isbn, library_id, is_available) VALUES (?, ?, ?)";
		List<Integer> libIds = new ArrayList<>(libs.values());
		String[] isbns = {
				"9780140449136","9780439139601","9780061120084","9780743273565",
				"9780451524935","9780141439518","9780007458424","9781503280786",
				"9780199232765","9780316769488","9780060850524","9780544003415",
				"9780142437209","9780140439848","9780066236096",
				"9780553293357","9780142415436","9781593080006","9780486282114","9780143105985"
		};
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int i = 0; i < isbns.length; i++) {
				for (int libId : libIds) {
					ps.setString(1, isbns[i]);
					ps.setInt(2, libId);
					ps.setBoolean(3, i % 4 != 0);  // varied availability
					ps.addBatch();
				}
			}
			ps.executeBatch();
		}
	}

	private static void insertBorrowRecords(Connection conn) throws SQLException {
		List<Integer> bookIds = new ArrayList<>();
		try (Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT book_id FROM books")) {
			while (rs.next()) bookIds.add(rs.getInt(1));
		}

		String sql = "INSERT IGNORE INTO borrow_record (member_id, book_id, return_date, is_returned) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			LocalDate today = LocalDate.now();
			for (int i = 0; i < 15; i++) {
				int memberId = 1;
				int bookId   = bookIds.get(i % bookIds.size());
				LocalDate date;
				boolean returned;
				if (i < 3) {
					date = today.minusDays(i + 1);
					returned = false;
				} else if (i < 5) {
					date = today.minusDays(i + 1);
					returned = true;
				} else if (i < 8) {
					date = today.plusDays(i - 5 + 1);
					returned = false;
				} else if (i < 10) {
					date = today.plusDays(i - 5 + 1);
					returned = true;
				} else {
					date = today.plusDays(i + 2);
					returned = false;
				}
				ps.setInt(1, memberId);
				ps.setInt(2, bookId);
				ps.setDate(3, Date.valueOf(date));
				ps.setBoolean(4, returned);
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}
}
