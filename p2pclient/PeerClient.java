package p2pclient;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * done peerclient retrieve data from response
 * done recieve file date depends on forehead status -> no empty rfc would be generated
 * !!! if serverThread end in excepion program would not go back to position let you choose rfc, instead it go back to role selection, which indicates an abnormal end of peeclient
 * done: 4/23/18 record length to reduce bytearray size
 * done dont prompt rfc number selection  if user want to return
 */

public class PeerClient {

    Socket socket = null;
    String request = "";
    String hostname;
    String rfc_no = "";
    int portno;
    String[] response = new String[7];
    int status;
    int rfc_legnth;

    public void run(){

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter client's ip you want to reach:");
        hostname  = sc.nextLine();
        System.out.println("Specify the upload port no:");
        portno = Integer.parseInt(sc.nextLine());

        try{
            socket =  new Socket(hostname,portno);
            System.out.println("Connection should be created");

            while (true) {
                System.out.println("Whether you want to get rfc from Peer" + "[" + hostname + ":" + portno + "](enter yes to get)");
                String choice = sc.nextLine();

                switch (choice) {
                    case "yes":
                        System.out.println("Input the rfc you want to get:");
                        rfc_no = sc.nextLine();
                        constructRequest();
                        break;
                    default:
                        socket.close();
                        System.out.println("socket closed");
                        return;
                }
                
                sendRequest(socket);

                printResponse(response,socket);

                receiveRFC(response,socket);

            }

        }catch(Exception e){
            System.out.println(e);
        }

    }

    public void constructRequest(){

        request += "GET RFC " + rfc_no + " P2P-CI/1.0" + "\r\n";
            //hostname is from which RFC is requested
        request += "Host: " + hostname + "\r\n";
            //OS of requesting host
        request += "OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + "\r\n";
        request += "\r\n" + "END";

    }

    public void sendRequest(Socket socket){
        String line;
        try{
            DataOutputStream outputStreamToPeer = new DataOutputStream(socket.getOutputStream());
            BufferedReader outToPeer = new BufferedReader(new StringReader(request));
            //reset request ?
            request = "";

            while ((line = outToPeer.readLine()) != null) {
                    //if (line.equals("END"))
                    //  break;
                    //这里读进来的line没有\r\n
                outputStreamToPeer.writeBytes(line + "\n");
        }
        }catch(IOException e){
            e.printStackTrace();
        }
        
    } 

    public void printResponse(String[] response, Socket socket){
        System.out.println("Response from PeerServer: ");
        int i = 0;
        String line = "";
        try{

            BufferedReader inFromPeer = new BufferedReader(new InputStreamReader(new DataInputStream(socket.getInputStream())));
            while ((line = inFromPeer.readLine()) != null){
                if (line.equals("END"))
                    break;
                response[i++] = line;
                System.out.println(line);
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        
    }

    public void receiveRFC(String[] response, Socket socket){

        status = Integer.parseInt(response[0].substring(11,14));
        rfc_legnth = Integer.parseInt(response[4].substring(16,21));

        if(status == 200){

            File curdir = new File(System.getProperty("user.dir"));
            // String parentPath = curdir.getCanonicalFile().getParent();
            // parentPath += "/rfc";
            //server filelist循环时貌似一定要点 这里不循环可以不用
            //System.out.println(System.getProperty("user.dir"));
            
            try{

                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                byte[] bytearray = new byte[rfc_legnth];
                bis.read(bytearray,0,bytearray.length);

                FileOutputStream fileOutputStream = new FileOutputStream(curdir.getCanonicalPath() + "/rfc/rfc" + rfc_no + ".txt");
                BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
                bos.write(bytearray,0,bytearray.length);

            }catch(IOException e){
                e.printStackTrace();
            }
            
        }

    }
}
