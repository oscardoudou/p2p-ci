import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * DataInputStream 是最底层的
 * 需要reader读（inputStreamReader就是一种）
 * reader需要bufferreader读
 * bufferreader.readLine() 出来的就是string
 * todo peer Server prompt
 */


public class PeerServerThread extends Thread{
    Socket socket;

    public PeerServerThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        System.out.println("now client could always works like a server role");

        try{

            while(true){
                DataInputStream inputStreamFromPeer = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStreamToPeer = new DataOutputStream(socket.getOutputStream());

                String line;
                BufferedReader inFromPeer = new BufferedReader(new InputStreamReader(inputStreamFromPeer));
                while((line = inFromPeer.readLine()) != null){
                    if(line.equals("END"))
                        break;
                    System.out.println(line);
                }
                String response = "P2P-CI/1.0 200 OK" + "\r\n";
                response += "Date: " + "\r\n";
                response += "OS: " + "\r\n";
                response += "Last-Modified: " + "\r\n";
                response += "\r\n" + "END";
                BufferedReader outToPeer = new BufferedReader(new StringReader(response));

                while((line = outToPeer.readLine()) != null){
                    outputStreamToPeer.writeBytes(line + "\n");
                }

            }

        }catch(IOException e){
            e.printStackTrace();

        }

    }
}
