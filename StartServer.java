import java.net.ServerSocket;
import java.net.Socket;

/**
 * this is a seperate thread to not interrupt main thread UI, actually thread tackle with server task is ServerThread
 * In order to circulate generating new ServerThread, we add a infinite loop here.
 * As long as it create a new Thread to take care of new client, it would keep on listening (accept())
 */

public class StartServer extends Thread{
    @Override
    public void run() {
        try{
            ServerSocket serverSocket = new ServerSocket(7734);
            System.out.println("Listening on port: "+ serverSocket.getLocalPort());
            while(true){
                //ServerThread 不在有accept()了，移到外面来了，否则非得打开两个client，才能又一个serverThread
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
