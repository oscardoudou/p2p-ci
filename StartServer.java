import java.net.ServerSocket;
import java.net.Socket;

public class StartServer extends Thread{
    @Override
    public void run() {
        try{
            ServerSocket serverSocket = new ServerSocket(7734);
            System.out.println("Listening on port: "+ serverSocket.getLocalPort());
            while(true){
                Socket socket = serverSocket.accept();
                String client_ip = socket.getRemoteSocketAddress().toString();
                int client_port_no = socket.getPort();
                System.out.println("ServerThread initializing... ");
                System.out.println("Connecting  to " + client_ip);
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }


}
