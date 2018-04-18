public class PeerInfo {
    PeerInfo next = null;
    public String hostname;
    public int portname;
    public PeerInfo(String hostname, int portname){
        this.hostname = hostname;
        this.portname = portname;
    }
    public String toString(){
        return hostname + "||" + portname;
    }
}
