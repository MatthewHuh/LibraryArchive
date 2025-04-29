package POJO;

public class Book {
	private int bookID;
	private int bookInfoID;
	private int libraryID;
	
	public Book(int bookID, int bookInfoID, int libraryID) {
		this.bookID = bookID;
		this.bookInfoID = bookInfoID;
		this.libraryID = libraryID;
	}
	
	public int getBookID() {
		return bookID;
	}
	
	public int getBookInfoID() {
		return bookInfoID;
	}
	
	public int getLibraryID() {
		return libraryID;
	}
	
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}
	
	public void setBookInfoID(int bookInfoID) {
		this.bookInfoID = bookInfoID;
	}
	
	public void setLibraryID(int libraryID) {
		this.libraryID = libraryID;
	}
}
