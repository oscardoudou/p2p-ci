import java.io.*;
import java.net.Socket;
/**
 * DataInputStream 是最底层的
 * 需要reader读（inputStreamReader就是一种）
 * reader需要bufferreader读
 * bufferreader.readLine() 出来的就是string
 * done store request for further search
 * done check whether rfc exist
 * //                System.out.println(dir.getCanonicalFile().getParent());
 * //                File rfc6666 = new File("rfc6666.txt");
 * //                rfc6666.createNewFile();
 * //                String absolutePath = rfc6666.getAbsolutePath();
 *                 //权威地址不带. 即parent/p2pclient所有她再往上倒一个parent就是parent/
 * //                String canonicalPath = rfc6666.getCanonicalPath();
 * //                System.out.println(dir.getAbsolutePath());
 * //                System.out.println(dir.getCanonicalPath());
 *
 * todo peer Server data transfer
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
                //size could be 4
                String[] request = new String[5];
                int i = 0;
                String line;
                BufferedReader inFromPeer = new BufferedReader(new InputStreamReader(inputStreamFromPeer));
                while((line = inFromPeer.readLine()) != null){
                    if(line.equals("END"))
                        break;
                    request[i++] = line;
                    System.out.println(line);
                }

                //hard code should be determined by request
                int status = 404;
                // TODO: 4/23/18 check version unmatch case
                String version = request[0].substring(request[0].length() - 10,request[0].length());
                //tackle different rfc number length
                String temp = request[0].substring(8,9);
                i = 9;
                while(request[0].charAt(i) != ' ')
                    temp += request[0].charAt(i++);
                //could be more bad request case
                if(temp.length() >= 5)
                    status = 400;
                int no =Integer.parseInt(temp);

                //search current directory rfc name
                //For IDE . means p2pclient/.  in other words it search file under p2pclient ingore file folder like src and out
                File curdir = new File(".");
                String parentPath = curdir.getCanonicalFile().getParent();
                parentPath += "/rfc";
                //mimic new File (".")
                File rfcdir = new File(parentPath + "/.");
                File[] fileList = rfcdir.listFiles();
                for(File file : fileList){
                    //System.out.println(file.getName());
                    if(file.getName() == "rfc" + no + ".txt" );
                        status = 200;
                }

                String response = "P2P-CI/1.0 " + status + "\r\n";
                response += "Date: " + java.time.LocalDate.now() + " " + java.time.LocalTime.now() +"\r\n";
                response += "OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + "\r\n";
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
