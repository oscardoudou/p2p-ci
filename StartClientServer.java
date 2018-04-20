import java.net.ServerSocket;
import java.net.Socket;

public class StartClientServer extends Thread{

    private int upload_portno;

    public StartClientServer(int upload_portno){
        this.upload_portno = upload_portno;
    }

    @Override
    public void run() {
        try{

            ServerSocket clientserverSocket = new ServerSocket(upload_portno);
            System.out.println("Listening on port:" + clientserverSocket.getLocalPort());

            Socket socket = clientserverSocket.accept();
            String peer_ip = socket.getRemoteSocketAddress().toString();
            System.out.println("PeerServerThread initializing...");
            System.out.println("Connecting to " + peer_ip);
            PeerServerThread peerServerThread = new PeerServerThread(socket);
            peerServerThread.start();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
