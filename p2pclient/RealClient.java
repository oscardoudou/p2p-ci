package p2pclient;
import java.net.Inet4Address;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class RealClient {
    //不同函数都要访问的，某些函数写，某些函数改
    Socket socket = null;
    private int upload_portno;
    String request = "";
    String rfc_no;
    String request_choice;

    public RealClient(Socket socket, int upload_portno){
        this.socket = socket;
        this.upload_portno = upload_portno;
    }

    public void run(){
        try{
            String response;
            
            

            while(true){

                System.out.println("Construct the request to send to server.");
                System.out.println("Input request type(ADD/LOOKUP/LIST/LEAVE:");
                System.out.println("!Enter LEAVE only to leave the system, rerun the client to become active again");
                
                
                
                Scanner sc = new Scanner(System.in);
                request_choice = sc.nextLine();
                //need refractor 
                switch(request_choice){
                    case "ADD":
                        System.out.println("Input rfc no you want to ADD:");
                        rfc_no = sc.nextLine();
                        request += request_choice + " RFC " + rfc_no + " P2P-CI/1.0\r\n";
                        break;
                    case "LOOKUP":
                        System.out.println("Input rfc no you want to LOOKUP:");
                        rfc_no = sc.nextLine();
                        request += request_choice + " RFC " + rfc_no + " P2P-CI/1.0\r\n";
                        break;
                    case "LIST":
                        request += request_choice + " ALL" + " P2P-CI/1.0\r\n";
                        break;
                        //to RealClient/Peer select
                    case "LEAVE":
                        try{
                            socket.close();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                        System.out.println("Left the system, irreversible operation. Only restart the program could register in again");
                        return;
                    default:
                        System.out.println("Invalid input returning to role selection");
                        return;
                    }

                constructRequest();
//
                BufferedReader outToServer = new BufferedReader(new StringReader(request));
                //!!!request must set to "" otherwise the 2nd request would follow by "END"of 1st request, which can't not detect, further lead to variable request[] in server out of range 5
                request = "";
                DataOutputStream outputStreamToServer = new DataOutputStream(socket.getOutputStream());
                String line;
                while((line = outToServer.readLine())!=null){
//                    //really don't know why I have to comment this statement, anyway if do help end the server wait
//                    if("END".equals(line))
//                        break;
                    outputStreamToServer.writeBytes(line + '\n');
                }

                DataInputStream inputStreamFromServer = new DataInputStream(socket.getInputStream());
                InputStreamReader serverStreamReader = new InputStreamReader(inputStreamFromServer);
                BufferedReader inFromServer = new BufferedReader(serverStreamReader);

                System.out.println("Response from server: ");
                line = "";
                while((line = inFromServer.readLine())!=null){
                    if("END".equals(line))
                        break;
                    System.out.println(line);
                }
            }
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    public void constructRequest(){
        
        
        try{
            request += "Host: " + Inet4Address.getLocalHost().getHostAddress() + "\r\n";
        }catch(Exception e){
            e.printStackTrace();
        }
        request += "Port: " + upload_portno + "\r\n";
        if(!request_choice.equals("LIST"))
            request += "Title: " + "PCE Requirement" +"\r\n" ;
        request += "\r\n" +"END";
    }
    //*todo
    public void retrieveRFCTitle(){

    }

}
