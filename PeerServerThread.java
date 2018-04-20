import java.net.ServerSocket;
import java.net.Socket;
/**
 * todo peer Server prompt
 */


public class PeerServerThread extends Thread{
    Socket socket;

    public PeerServerThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("now client could always works like a server role");
    }
}
