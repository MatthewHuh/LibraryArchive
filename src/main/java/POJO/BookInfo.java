package POJO;

import java.sql.Date;

public class BookInfo {
	private int bookInfoID;
	private String author;
	private String genre;
	private String title;
	private Date releaseDate;
	
	public BookInfo(int bookInfoID, String author, String genre, 
			String title, Date releaseDate) {
		this.bookInfoID = bookInfoID;
		this.author = author;
		this.genre = genre;
		this.title = title;
		this.releaseDate = releaseDate;
	}
	
	
	public int getBookInfoID() {
		return bookInfoID;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setBookInfoID(int bookInfoID) {
		this.bookInfoID = bookInfoID;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
