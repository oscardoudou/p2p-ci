package p2pserver;
import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * work as server client done
 * real server done
 * the infinite loop is to iteratively tackle client's request as long as client don't quit
 */

public class ServerThread extends Thread {

    public static List<PeerInfo> list = new LinkedList<>();
    public static List<RFCInfo> index = new LinkedList<>();

    String[] request = new String[5];
    Socket socket = null;
    //response should be initialize as "" not null, latter would cause null ahead of message
    String response = "";
    String hostname = "";
    int port_no ;
    String version;
    String title;

    public ServerThread(Socket socket){
        this.socket = socket;
    }

    public void run (){
        try{
            while(true){

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
                //avoid broken pipe write exception, know and find this solution by debug step by step
                if(line == null){
                    removeRecord();
                    //System.out.println("remove executed");
                    return;
                }
                    
                version = request[0].substring(request[0].length()-10,request[0].length());
                hostname = request[1].substring(6);
                port_no = Integer.parseInt(request[2].substring(6));
                if(!request[0].substring(0,3).equals("LIS"))
                    title = request[3].substring(7);
                //System.out.println(request[0].substring(0,3));
                switch(request[0].substring(0,3)){
                    case "ADD":
                        responseADD(request);
                        break;
                    case "LOO":
                        responseLOOKUP(request);
                        break;
                    case "LIS":
                        responseLIST();
                        break;
                    default:
                        System.out.println("can not parse request type");
                        break;
                }

                BufferedReader outToClient = new BufferedReader(new StringReader(response));
                response = "";
                DataOutputStream outputStreamToClient = new DataOutputStream(socket.getOutputStream());
                line = "";
                while((line = outToClient.readLine())!=null){
                    //since line has get rid of \r\n to format multiple line into hierarchic message
                    outputStreamToClient.writeBytes(line + '\n');
                }
            }

        }
        catch(IOException e){
            //System.out.println(e); print grey Exception prompt, while printStackTrace print red prompt
            e.printStackTrace();
        }
    }
    public void responseADD(String[] request){
        //int no = Integer.parseInt(request[0].substring(8,12));
        //solve different rfc number length
        String temp = request[0].substring(8,9);
        int i = 9;
        while(request[0].charAt(i) != ' ')
            temp += request[0].charAt(i++);
        int no = Integer.parseInt(temp);

        //here don't check if hostname duplicate, we solve this later when actually return list
        //来之不拒就是加
        list.add(new PeerInfo(hostname, port_no));
        index.add(new RFCInfo(no, title, hostname));
        response += version + " 200 OK\r\n";
        response += "RFC " + no + " " + title + " " + hostname + " " + port_no +"\r\n";
        response += "\r\n"+"END";

    }
    public void responseLIST(){
        response += version + " 200 OK\r\n";
        response += "\r\n";
        //如果你只想打印出来，这里甚至可以不拓展<E>，但若想付给RFCInfo引用，必须拓展
        Iterator<RFCInfo> it = index.iterator();
        while(it.hasNext()){
             RFCInfo rfc = it.next();
             Iterator<PeerInfo> peerit = list.iterator();
             int uploadportno = 0;
             while(peerit.hasNext()){
                 PeerInfo peerinfo = peerit.next();
                 if(peerinfo.hostname == rfc.hostname){
                    //there would only one in whole peerlist reference the exact same string(hostname) in rfc, since they are created together in add
                     uploadportno = peerinfo.portno;
                     response += "RFC " + rfc.no + " " + rfc.title + " " + rfc.hostname + " " + uploadportno +"\r\n";
                    }
                //below same stmt in if could move out, would have same result, cause there is only one reference match
                //response += "RFC " + rfc.no + " " + rfc.title + " " + rfc.hostname + " " + uploadportno +"\r\n";
            }
        }
        response += "\r\n" + "END";
        checkRecordStatus();
    }
    public void responseLOOKUP(String[] request){
        response += version + " 200 OK\r\n";
        response += "\r\n";

        String temp = request[0].substring(11,12);
        int i = 12;
        while(request[0].charAt(i) != ' ')
            temp += request[0].charAt(i++);
        int no = Integer.parseInt(temp);

        Iterator<RFCInfo> it = index.iterator();
        while(it.hasNext()){
            //here rfc stand for rfc_entry
            RFCInfo rfc = it.next();
            if(rfc.no == no){
                //there could be multiple rfc_entry matched
                int uploadportno = 0;
                Iterator<PeerInfo> peerit = list.iterator();
                while(peerit.hasNext()){
                    PeerInfo peerinfo = peerit.next();
                    //here it has to be reference same obj, reason see p2pplan
                    if(peerinfo.hostname == rfc.hostname)
                        //for each matched rfc_entry, there could only be one unique portno
                        //effective solve the problem that test could only be operated on single machine, which still show correct result
                        uploadportno = peerinfo.portno;
                }
                response += "RFC " + rfc.no + " " + rfc.title + " " + rfc.hostname + " " + uploadportno +"\r\n";
            }
        }
        response += "\r\n" + "END";
    }
    public void removeRecord(){
        // checkRecordStatus();
        String inactive_hostname = hostname;

        // System.out.println("-------------------------");
        // System.out.println(System.identityHashCode(hostname));
        // System.out.println(System.identityHashCode(inactive_hostname));
        // System.out.println("-------------------------");
        //dont user iterator and modify method at same loop, which will throw concurrent exception
        // System.out.println(list.size());
        for(int i = 0 ; i < list.size() ; i ++){
            //== is used to compare whether reference same object, equal is usedt to compare string
            // System.out.println(System.identityHashCode(list.get(i).hostname));
            // System.out.println(i + "" + (list.get(i).hostname.equals(inactive_hostname)));
            if(list.get(i).hostname.equals(inactive_hostname)){
                list.remove(i);
                i--;
            }
                
        } 
        // System.out.println("-------------------------");
        // System.out.println(index.size());
        for(int i = 0 ; i < index.size() ; i ++){
            // System.out.println(System.identityHashCode(index.get(i).hostname));
            // System.out.println(i + "" + (index.get(i).hostname.equals(inactive_hostname)));
            if(index.get(i).hostname.equals(inactive_hostname)){
                index.remove(i);
                i--;
            }
                
        }

        // System.out.println("-------------------------");
        // checkRecordStatus();
        
    }
    public void checkRecordStatus(){
        Iterator<PeerInfo> peerit_final = list.iterator();
        while(peerit_final.hasNext()){
            PeerInfo peerinfo_final = peerit_final.next();
            System.out.println(peerinfo_final);
        }
        Iterator<RFCInfo> it_final = index.iterator();
        while(it_final.hasNext()) {
            RFCInfo rfc_final = it_final.next();
            System.out.println(rfc_final);
        }
    }
}