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

    }

}
