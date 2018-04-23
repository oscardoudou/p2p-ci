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
 * finish uploadport listening in background(another thread) could connecte to different peer via this port
 * this listening background doesn't interrupt main thread UI
 * work as server client done
 * real client done
 * todo work as peer Server peer client
 * todo test other peer connection to upload_port
 * todo peer Server prompt
 * todo peer client prompt
 */
public class Client{

    public static void main (String[] args) throws Exception{
        //hard code here for local run
        String hostname = "127.0.0.1";
        int port = 7734;
        Scanner sc = new Scanner(System.in);
        String role;

        try{

            //this socket is for persistent connection to server, only close when leave system
            Socket clientSocket = new Socket(hostname, port);
            RealClient realClient = new RealClient(clientSocket);

            System.out.println("Please input uploadport (you want to use as PeerServer)");
            int uploadportno = Integer.parseInt(sc.nextLine());
            realClient.upload_portno = uploadportno;
            //System.out.println(uploadportno);

            new StartClientServer(uploadportno).start();

            while (true) {
                System.out.println("Pleas select the role for client:");
                role = sc.nextLine();
                switch (role) {
                    case "client":
                        realClient.run();
                        break;
                    case "peerclient":
                        new PeerClient().run();
                        break;

                }
            }

        }catch(IOException e){
            System.out.println(e);
        }
    }
}