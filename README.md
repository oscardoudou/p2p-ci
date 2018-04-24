# peep to peer system with centralized index
Internet protocol standards are defined in documents called “Requests for Comments” (RFCs). RFCs are available for 	download from the [IETF](http://www.ietf.org/) web site. Rather than using this centralized server for downloading RFCs, you will build a p2p-ci system in which peers who wish to download an RFC that they do not have in their hard drive, may download it from another active peer who does. All communication among peers or between a peer and the server will take place over TCP.  
## how each component of p2p-ci system works?
+ there is a centralized server, running on a well-known host and listening on a well-known port, which keeps information about the active peers and maintains an index of the RFCs available at each active peer.
+ when a peer decides to join the p2p-ci system, it opens a connection to the server to register itself and provide information about the RFCs that it makes available to other peers. This connection remains open as long as the peer remains active; the peer closes the connection when it leaves the system (becomes inactive). 
+ since the server may have connections open to multiple peers simultaneously, it spawns a new process to handle the communication to each new peer. 
+ when a peer wishes to download a specific RFC, it provides the RFC number to the server over the open connection, and in response the server provides the peer with a list of other peers who have the RFC; if no such active peer exists, an appropriate message is transmitted to the requesting peer. Additionally, each peer may at any point query the server to obtain the whole index of RFCs available at all other active peers. 
+ each peer runs a upload server process that listens on a port specific to the peer ; in other words, this port is not known in advance to any of the peers. When a peer A needs to download an RFC from a peer B, it opens a connection to the upload port of peer B, provides the RFC number to B, and B responds by sending the (text) file containing the RFC to A over the same connection; once the file transmission is completed, the connection is closed. 
## commands
>the project is based on java8
### centralized Server
```
bash server.sh
```

### client

```
bash client.sh
```
## tnteract with prompt
>this project introduces two role of Client (client,peerclient)
+ first, you input your upload port no
+ enter role you want to play 
	+ client (input is case sensitive)
	+ peerclient(only one method get, enter yes instead of get)
+ client 
	+ ADD
 	+ LOOKUP 
	+ LIST
+ peerclient 
	+ GET
+ for ADD and LOOKUP method, you need enter RFC number follow up
+ if you want to test peer to peer feature, make sure you see "listening on XXX port" on the other client
+ retrieve host name by utilizing LOOKUP and LIST method
+ enter LEAVE in client role would make client INACTIVE. If you want to re-establish connection, just run client again
+ input other operation in client role would bring you back to role selection phase (connection is still alive)
+ input other operation in peerclient role would also bring you back to role selection and also shut down peer connection
