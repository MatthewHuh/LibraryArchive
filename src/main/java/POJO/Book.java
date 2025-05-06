package POJO;

public class Book {
	private Integer bookID;
	private String bookInfoID;
	private int libraryID;


	public Book(int bookID, String bookInfoID, int libraryID) {
		this.bookID = bookID;
		this.bookInfoID = bookInfoID;
		this.libraryID = libraryID;
	}

	public int getBookID() {
		return bookID;
	}
	
	public String getBookInfoID() {
		return bookInfoID;
	}
	
	public int getLibraryID() {
		return libraryID;
	}
	
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}
	
	public void setBookInfoID(String bookInfoID) {
		this.bookInfoID = bookInfoID;
	}
	
	public void setLibraryID(int libraryID) {
		this.libraryID = libraryID;
	}

}
