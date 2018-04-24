package p2pserver;

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
            //create a server-side socket,namely ServerSocket, assign binding port, start listening
            //ServerSocket 就像是一头热的挑子， 开了个口，始终在等对面的信，接收到了对面的信儿才变成真正socket
            //相比之下client side就不需要这种东西，连成功了就是socket，没连成就是错误的socket，不存在中间状态
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
