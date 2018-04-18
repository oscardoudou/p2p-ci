public class PeerInfo {
    PeerInfo next = null;
    public String hostname;
    public int portno;
    public PeerInfo(String hostname, int portno){
        this.hostname = hostname;
        this.portno = portno;
    }
    public String toString(){
        return hostname + "||" + portno;
    }
}
