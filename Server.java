import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * Server should enable multi-thread to support multi client connection, but client should not halt main thread UI
 * In other words, main UI should not wait for other thread connection
 * The thing is we could simply achieve multi-thread to tackle multiple client. However, the main UI would halt if no new thread created(no client connect in)
 * So in order to let my main thread could do its own following operation(which after creating listening ServerSocket),
 * we need first add a thread which take care of listening.
 * In our case, StartServer is the thread separate from main thread to complete above task
 * This is why not only ServerThread extend thread
 *
 * Server       ||  Client
 * Real Server  ||  Real Client, peer Server, peer Client
 */
public class Server{

    public static void main (String[] args)throws Exception{
        int well_known_port = 7734;
        new StartServer().start();
        //UI part
        System.out.println("sun rise as always");
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("please interact with server as you want ");
            String s = sc.nextLine();
            System.out.println(s);
        }
//        try{
//            //create a server-side socket,namely ServerSocket, assign binding port, start listening
//            //ServerSocket 就像是一头热的挑子， 开了个口，始终在等对面的信，接收到了对面的信儿才变成真正socket
//            //相比之下client side就不需要这种东西，连成功了就是socket，没连成就是错误的socket，不存在中间状态
////            ServerSocket listeningSocket = new ServerSocket(well_known_port);
//            System.out.println("----------------");
//            while(true){
////                //accept() is used for listening to port, wait for clients' connection
////               // Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
////                Socket connectionSocket = listeningSocket.accept();
//////                //create a new thread
//////                ServerThread serverThread = new ServerThread(connectionSocket);
////                ServerThread serverThread = new ServerThread(connectionSocket);
////                //start vs run, start actually create a new thread so that the runnable's run method is executed in parallel
////                serverThread.start();
//
//                System.out.println("A serverThread is running..");
//
//            }
//
//        }
//        catch(IOException e){
//            System.out.println(e);
//        }
    }

}
