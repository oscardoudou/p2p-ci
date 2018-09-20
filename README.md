# Peep to Peer System with Centralized Index
## Inspiration
I am taking SDN course this semester which need to read a lot of RFCs according to traffic engineering PCE and stuff like that. Professor didn't give us the whole list of RFCs reading assignment. As a result, we struggled to survive the quiz. None of student know excat RFCs we need to read, but each of us did know part of them. So I came up the idea to exchange the RFC in a p2p system. In order to know the other peer's ip and port no, I finally decide to build a peer to peer with centralized index system.
Since the reading assignment for RFCs is not as large as real RFC amount, for sake of memory efficiency, I finally choose linkedlist as data structure. To guarantees RFCs delivery, obviously we don't want to read RFC with mistakes, all communication among peers or between a peer and the server will take place over TCP. Also to be user-friendly, I introduce the role of client so that people won't mess around and ask who am I.
## How P2P-CI system works?
+ there is a centralized server, running on a well-known host and listening on a well-known port, which keeps information about the active peers and maintains an index of the RFCs available at each active peer.
+ when a peer decides to join the p2p-ci system, it opens a connection to the server to register itself and provide information about the RFCs that it makes available to other peers. This connection remains open as long as the peer remains active; the peer closes the connection when it leaves the system (becomes inactive). 
+ since the server may have connections open to multiple peers simultaneously, it spawns a new process to handle the communication to each new peer. 
+ when a peer wishes to download a specific RFC, it provides the RFC number to the server over the open connection, and in response the server provides the peer with a list of other peers who have the RFC; if no such active peer exists, an appropriate message is transmitted to the requesting peer. Additionally, each peer may at any point query the server to obtain the whole index of RFCs available at all other active peers. 
+ each peer runs a upload server process that listens on a port specific to the peer ; in other words, this port is not known in advance to any of the peers. When a peer A needs to download an RFC from a peer B, it opens a connection to the upload port of peer B, provides the RFC number to B, and B responds by sending the (text) file containing the RFC to A over the same connection; once the file transmission is completed, the connection is closed. 
## Running the test
>The project is based on java8
### Centralized server
```
bash server.sh
```

### Client

```
bash client.sh
```
## Interact with prompt
1. you input your upload port no
2. enter role you want to play 
	* client 
	* peerclient
3. client 
	* ADD
 	* LOOKUP 
	* LIST
4. peerclient 
	* GET
## Acknowledgement
+ Rememeber enter upload portno for client first, the system put service other in the first place
+ There is only two roles for client: client,peerclient
+ If you want to test peer to peer feature, make sure you see "listening on XXX port" on the other client
+ All operation except LEAVE works as will even you try to run clients on shared ip
+ Make sure you do have exact rfc text file locally, which corresponds to rfc_no you want to ADD 
+ All operation input is case sensitive
+ Enter YES instead of GET when your client work as peerclient
+ For ADD and LOOKUP method, you need enter RFC number follow up
+ Find the host name you interested in by utilizing LOOKUP and LIST method
+ Enter LEAVE in client role would make client INACTIVE. 
+ If you want to re-establish connection, just run client again
+ No need to enter rfc title, system automatically extract title from rfc.
+ Input other operation in client role would bring you back to role selection phase (connection is still alive)
+ Input other operation in peerclient role would also bring you back to role selection and also shut down peer connection
