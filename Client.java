import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * client have only two process
 * 1. a consistent connection with centralize server to notify it is active
 * 2. a upload process listen other peer's connection(this is the part need multithread, cause it may connect to multiple peer)
 *  client should first establish its own connection to server well-known port
 *  client should call PeerHandler which extend Thread as long as new peer connection accept
 * @author Yichi Zhang
 */
public class Client{
    public static final int SO_TIMEOUT = 2000;

    public static void main (String[] args) throws Exception{
        //hard code here for local run
        String hostname = "127.0.0.1";
        int port = 7734;
        Scanner sc = new Scanner(System.in);
        String role;

        try{

            //this socket is for persistent connection to server, only close when leave system
            Socket clientSocket = new Socket(hostname, port);
            PeerClient peerClient = new PeerClient(clientSocket);

            System.out.println("Please input uploadport (you want to use as PeerServer)");
            int uploadportno = Integer.parseInt(sc.nextLine());
            System.out.println(uploadportno);
            ServerSocket listeningSocket = new ServerSocket(uploadportno);
//            listeningSocket.setSoTimeout(2000);
            System.out.println("Pleas select the role for client:");
            role = sc.nextLine();
            switch (role) {
                case "client":
                    peerClient.run();
                    break;
                case "server":
                    while (true) {
                        PeerServerThread peerServerThread = new PeerServerThread(listeningSocket);
                        peerServerThread.start();
                    }
            }

        }catch(IOException e){
            System.out.println(e);
        }
    }
}