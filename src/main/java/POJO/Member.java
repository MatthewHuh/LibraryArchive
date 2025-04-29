package POJO;

import java.time.LocalDate;

public class Member {
	private int borrowRecordID;
	private int memberID;
	private int bookID;
	private int lateFee;
	private LocalDate returnDate;
	
	public Member(int borrowRecordID, int memberID, int bookID, 
			int lateFee, LocalDate returnDate) {
		this.borrowRecordID = borrowRecordID;
		this.memberID = memberID;
		this.bookID = bookID;
		this.lateFee = lateFee;
		this.returnDate = returnDate;
	}
	
	public int getBookID() {
		return bookID;
	}
	
	public int getBorrowRecordID() {
		return borrowRecordID;
	}
	
	public int getLateFee() {
		return lateFee;
	}
	
	public int getMemberID() {
		return memberID;
	}
	
	public LocalDate getReturnDate() {
		return returnDate;
	}
	
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}
	
	public void setBorrowRecordID(int borrowRecordID) {
		this.borrowRecordID = borrowRecordID;
	}
	
	public void setLateFee(int lateFee) {
		this.lateFee = lateFee;
	}
	
	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}
	
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
}
