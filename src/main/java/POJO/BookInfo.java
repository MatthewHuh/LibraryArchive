package POJO;

import java.sql.Date;
import java.time.LocalDate;

public class BookInfo {
	private String isbn;
	private String author;
	private String genre;
	private String title;
	private LocalDate releaseDate;
	
	public BookInfo(String isbn, String author, String genre,
			String title, LocalDate releaseDate) {
		this.isbn = isbn;
		this.author = author;
		this.genre = genre;
		this.title = title;
		this.releaseDate = releaseDate;
	}
	
	
	public String getISBN() {
		return isbn;
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
	
	public LocalDate getReleaseDate() {
		return releaseDate;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setISBN(String isbn) {
		this.isbn = isbn;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
