package POJO.Singleton;

import DAO.*;

public class GlobalDAO {
    private static final GlobalDAO instance =  new GlobalDAO();

    private BookDAO bookDAO;
    private BookInfoDao bookInfoDAO;
    private BorrowRecordDAO borrowRecordDAO;
    private LibraryDao libraryDao;
    private MemberDAO memberDAO;

    private GlobalDAO() {}

    public static GlobalDAO getInstance() {
        return instance;
    }

    public BookDAO getBookDAO() {
        return bookDAO;
    }

    public BookInfoDao getBookInfoDAO() {
        return bookInfoDAO;
    }

    public LibraryDao getLibraryDao() {
        return libraryDao;
    }

    public MemberDAO getMemberDAO() {
        return memberDAO;
    }

    public BorrowRecordDAO getBorrowRecordDAO() {
        return borrowRecordDAO;
    }

    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public void setBookInfoDAO(BookInfoDao bookInfoDAO) {
        this.bookInfoDAO = bookInfoDAO;
    }

    public void setLibraryDao(LibraryDao libraryDao) {
        this.libraryDao = libraryDao;
    }

    public void setMemberDAO(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    public void setBorrowRecordDAO(BorrowRecordDAO borrowRecordDAO) {
        this.borrowRecordDAO = borrowRecordDAO;
    }

    public void setAll(BookDAO bookDao, BookInfoDao bookInfoDAO, LibraryDao libraryDao, MemberDAO memberDAO, BorrowRecordDAO borrowRecordDAO) {
        this.bookDAO = bookDao;
        this.bookInfoDAO = bookInfoDAO;
        this.libraryDao = libraryDao;
        this.memberDAO = memberDAO;
        this.borrowRecordDAO = borrowRecordDAO;
    }
}
