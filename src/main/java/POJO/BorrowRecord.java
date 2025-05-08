package POJO;

import java.sql.Date;

public class BorrowRecord {
	private Integer borrowRecordID;
	private int memberID;
	private int bookID;
	private Date returnDate;
	
	public BorrowRecord(Integer borrowRecordID, int memberID, int bookID, Date returnDate) {
		this.borrowRecordID = borrowRecordID;
		this.memberID = memberID;
		this.bookID = bookID;
		this.returnDate = returnDate;
	}
	
	public int getBookID() {
		return bookID;
	}
	
	public Integer getBorrowRecordID() {
		return borrowRecordID;
	}
	
	public int getMemberID() {
		return memberID;
	}
	
	public Date getReturnDate() {
		return returnDate;
	}
	
	public void setBookID(Integer bookID) {
		this.bookID = bookID;
	}
	
	public void setBorrowRecordID(int borrowRecordID) {
		this.borrowRecordID = borrowRecordID;
	}
	
	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}
	
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
}
