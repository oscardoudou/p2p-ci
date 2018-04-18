import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ServerThread extends Thread {

    public static List<PeerInfo> list = new LinkedList<>();
    public static List<RFCInfo> index = new LinkedList<>();

    String[] request = new String[5];
    Socket socket = null;
    //response should be initialize as "" not null, latter would cause null ahead of message
    String response = "";
    String version;

    public ServerThread(Socket socket){
        this.socket = socket;
    }

    public void run (){
        try{
            String client_ip = socket.getRemoteSocketAddress().toString();
            int client_port_no = socket.getPort();
            System.out.println("Connecting  to "+ client_ip);

            DataInputStream inputStreamFromClient = new DataInputStream(socket.getInputStream());
            InputStreamReader clientStreamReader = new InputStreamReader(inputStreamFromClient);
            BufferedReader inFromClient = new BufferedReader(clientStreamReader);

            System.out.println("Request from client: ");
            String line;
            int i = 0;
            while((line = inFromClient.readLine())!= null){
                if("END".equals(line))
                    break;
                request[i++] = line;
//                if(i <= 3)
//                    System.out.println(request[0].substring(0,3));
                System.out.println(line);
            }
            version = request[0].substring(request[0].length()-10,request[0].length());
            //System.out.println(request[0].substring(0,3));
            switch(request[0].substring(0,3)){
                case "ADD":
                    echoBack(request);
                    break;
                case "LOO":
                    break;
                case "LIS":
                    break;
                default:
                    System.out.println("can not parse request type");
                    break;
            }

            BufferedReader outToClient = new BufferedReader(new StringReader(response));
            DataOutputStream outputStreamToClient = new DataOutputStream(socket.getOutputStream());
            line = "";
            while((line = outToClient.readLine())!=null){
                //since line has get rid of \r\n to format multiple line into hierarchic message
                outputStreamToClient.writeBytes(line + '\n');
            }

        }
        catch(IOException e){
            System.out.println(e);
        }
    }
    public void echoBack(String[] request){
        String hostname = request[1].substring(6);
        int port_no = Integer.parseInt(request[2].substring(6));
        //int no = Integer.parseInt(request[0].substring(8,12));
        //solve different rfc number length
        String temp = request[0].substring(8,9);
        int i = 9;
        while(request[0].charAt(i) != ' ')
            temp += request[0].charAt(i++);
        int no = Integer.parseInt(temp);
        String title = request[3].substring(7);
        //here don't check if hostname duplicate, we solve this later when actually return list
        //来之不拒就是加
        list.add(new PeerInfo(hostname, port_no));
        index.add(new RFCInfo(no, title, hostname));
        response += version + " 200 OK\r\n";
        response += "RFC " + no + " " + title + " " + hostname + " " + port_no +"\r\n";
        response += "\r\n"+"END";
//        Iterator it = list.iterator();
//        while(it.hasNext()){
//            System.out.println(it.next());
//        }
//        it = index.iterator();
//        while(it.hasNext()){
//            System.out.println(it.next());
//        }
    }
}