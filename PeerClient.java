import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class PeerClient {
    Socket socket = null;
    String request = "";
    String hostname;
    String rfc_no = "";

    public void run(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter client's ip you want to reach:");
        hostname  = sc.nextLine();
        System.out.println("Specify the upload port no:");
        int portno = Integer.parseInt(sc.nextLine());

        try{
            socket =  new Socket(hostname,portno);
            System.out.println("Connection should be created");

            while (true) {
                System.out.println("Whether you want to request rfc from Peer" + "[" + hostname + ":" + portno + "]");
                String choice = sc.nextLine();
                System.out.println("Input the rfc you want to get:");
                rfc_no = sc.nextLine();
                switch (choice) {
                    case "yes":
                        get();
                        break;
                    default:
                        socket.close();
                        System.out.println("socket closed");
                        return;
                }
                String line;
                DataOutputStream outputStreamToPeer = new DataOutputStream(socket.getOutputStream());
                DataInputStream inputStreamFromPeer = new DataInputStream(socket.getInputStream());
                BufferedReader outToPeer = new BufferedReader(new StringReader(request));
                request = "";
                while ((line = outToPeer.readLine()) != null) {
//                if (line.equals("END"))
//                    break;
                    //这里读进来的line没有\r\n
                    outputStreamToPeer.writeBytes(line + "\n");
                }

                System.out.println("Response from PeerServer: ");
                line = "";
                BufferedReader inFromPeer = new BufferedReader(new InputStreamReader(inputStreamFromPeer));
                while ((line = inFromPeer.readLine()) != null){
                    if (line.equals("END"))
                        break;
                    System.out.println(line);
                }
            }

        }catch(Exception e){
//            e.printStackTrace();
            System.out.println(e);
        }

    }
    public void get(){
       // String line;
        //try {


            request += "GET RFC " + rfc_no + " P2P-CI/1.0" + "\r\n";
            request += "Host: " + hostname + "\r\n";
            request += "OS: MAC" + "\r\n";
            request += "\r\n" + "END";



//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
