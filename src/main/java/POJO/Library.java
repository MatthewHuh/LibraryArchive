package POJO;

public class Library {
	private int libraryID;
	private String address;
	private String name;
	
	public Library(int libraryID, String address, String name) {
		this.libraryID = libraryID;
		this.address = address;
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public int getLibraryID() {
		return libraryID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setLibraryID(int libraryID) {
		this.libraryID = libraryID;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String toString(){
		return name;
	}
}
