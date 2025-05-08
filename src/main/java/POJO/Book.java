package POJO;

public class Book {
	private Integer bookID;
	private String bookInfoID;
	private int libraryID;
	private boolean isAvailable;


	public Book(Integer bookID, String bookInfoID, int libraryID,  boolean isAvailable) {
		this.bookID = bookID;
		this.bookInfoID = bookInfoID;
		this.libraryID = libraryID;
		this.isAvailable = isAvailable;
	}

	public Integer getBookID() {
		return bookID;
	}
	
	public String getBookInfoID() {
		return bookInfoID;
	}
	
	public int getLibraryID() {
		return libraryID;
	}

	public boolean isAvailable() { return  isAvailable; }
	
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}
	
	public void setBookInfoID(String bookInfoID) {
		this.bookInfoID = bookInfoID;
	}
	
	public void setLibraryID(int libraryID) {
		this.libraryID = libraryID;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

}
