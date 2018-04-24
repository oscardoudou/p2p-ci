package p2pserver;
public class RFCInfo {
    RFCInfo next = null;
    public int no;
    public String title;
    public String hostname;
    public RFCInfo(int no, String title, String hostname){
        this.no = no;
        this.title = title;
        this.hostname = hostname;
    }
    public String toString(){
        return no + title + hostname ;
    }
}