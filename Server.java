import java.net.*;
import java.io.*;

public class Server{

    public static void main (String[] args)throws Exception{
        try{
            //create a server-side socket,namely ServerSocket, assign binding port
            ServerSocket welcomeSocket = new ServerSocket(5678);
            while(true){
                //accept() listen to port, wait for clients' connection
                Socket connectionSocket = welcomeSocket.accept();
                //create a new thread
                ServerThread serverThread = new ServerThread(connectionSocket);
                serverThread.start();
            }
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

}