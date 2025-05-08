package POJO;

import java.sql.Date;

public class BorrowDisplayObject {
    private Integer bookID;
    private String title;
    private String isbn;
    private boolean isAvailable;
    private Date returnDate;
    private String LibraryName;
    private String LibraryAddress;


    public BorrowDisplayObject(Integer BookID, String title, String isbn, boolean isAvailable, Date returnDate, String LibraryName, String LibraryAddress) {
        this.bookID = BookID;
        this.title = title;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
        this.returnDate = returnDate;
        this.LibraryName = LibraryName;
        this.LibraryAddress = LibraryAddress;
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getISBN() {
        return isbn;
    }

    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    public boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getLibraryName() {
        return LibraryName;
    }

    public void setLibraryName(String libraryName) {
        LibraryName = libraryName;
    }

    public String getLibraryAddress() {
        return LibraryAddress;
    }

    public void setLibraryAddress(String libraryAddress) {
        LibraryAddress = libraryAddress;
    }
}
