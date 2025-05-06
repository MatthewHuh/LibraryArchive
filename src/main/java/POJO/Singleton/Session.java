package POJO.Singleton;

import POJO.Member;

public class Session {
    private static final Session instance = new Session();
    private Member currentMember;
    private Session() {}
    public static Session get() { return instance; }

    public Member getMember() { return currentMember; }
    public void setMember(Member m) { this.currentMember = m; }
}