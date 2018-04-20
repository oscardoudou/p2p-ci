import java.net.ServerSocket;
import java.net.Socket;

public class PeerServerThread extends Thread{
    ServerSocket p2pserverSocket;

    public PeerServerThread(ServerSocket serverSocket){
        p2pserverSocket = serverSocket;
    }

    @Override
    public void run() {
        System.out.println("now client work as server role");
    }
}
