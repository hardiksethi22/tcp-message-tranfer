package chatprojectserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class Server implements Runnable{
	
	private List<ServerClient> clients = new ArrayList<ServerClient>();
	private List<Integer> clientResponse = new ArrayList<Integer>();
   
	private int port;
    private DatagramSocket socket;
    private boolean running = false;
    private Thread run, manage, send, receive;
    
    private boolean raw = false;
    
    private final int MAX_ATTEMPTS = 5;
   
   public Server(int port){
	   this.port = port;
	   try {
		socket = new DatagramSocket(port);
	} catch (SocketException e) {
		e.printStackTrace();
		return;
	}
	   run = new Thread(this, "Server");
	   run.start();
   }
    
    public void run(){
    	running = true;
    	System.out.println("Server started on port : "+port);
    	manageClients();
    	receive();
    	Scanner scanner = new Scanner(System.in);
    	while(running){
    		String text = scanner.nextLine();
    		if(!text.startsWith("/")){
    			sendToAll("/m/Server: " + text + "/e/");
    		    continue;	
    		}
    		text = text.substring(1);
    		if(text.equals("raw")){
    			raw = !raw;
    		}else if(text.equals("clients")){
    			System.out.println("Clients");
    			System.out.println("============");
    			for(int i=0 ; i<clients.size(); i++){
    				ServerClient c = clients.get(i);
    				System.out.println(c.name + "(" + c.getID() + "): " + c.address.toString() + ":" + c.port);
    			}
    			System.out.println("============");
    		}else if(text.startsWith("kick")){
    			String name = text.split(" ")[1];
    			boolean number = true;
    			int id = -1;
    			try{
    				id = Integer.parseInt(text);
    			}catch(NumberFormatException e){
    				number = false;
    			}
    			if(number){
    				boolean exists = false;
    				for(int i=0;i<clients.size() ; i++){
    					if(clients.get(i).getID() == id){
    						exists = true;
    						break;
    					}
    				}
    				if(exists) disconnect(id, true);
    				else System.out.println("Clients with this id doesn't exists");
    			}else{
    				for(int i=0;i<clients.size() ; i++){
    					ServerClient c = clients.get(i);
    					if(name.equals(c.name)){
    						disconnect(c.getID(), true);
    						break;
    					}
    				}
    			}
    		}
    	}
    }
    
    public void manageClients(){
    	manage = new Thread("Manage"){
    	public void run(){
    		while(running){
    			sendToAll("/i/server");
    			sendStatus();
    			try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    			for(int  i = 0; i < clients.size(); i++){
    				ServerClient c = clients.get(i);
    				if(!clientResponse.contains(clients.get(i).getID())){
    					if(c.attempt >= MAX_ATTEMPTS){
    						disconnect(c.getID(), false);
    					}
    					else{
    						c.attempt++;
    					}
    				}else{
    					clientResponse.remove(new Integer(c.getID()));
    					c.attempt = 0;
    				}
    			}
    		}
    	}
      };
      manage.start();
    }
    
    private void sendStatus(){
    	if(clients.size() <=0) return;
    	String users = "/u/";
    	for(int  i = 0; i < clients.size() - 1; i++){
    		users += clients.get(i).name + "/n/";
    	}
    	users += clients.get(clients.size()-1) + "/e/";
    	sendToAll(users);
    }
    
    public void receive(){
    	receive = new Thread("Receive"){
    		public void run(){
    			while(running){
    				byte[] data = new byte[1024];
        			DatagramPacket packet = new DatagramPacket(data, data.length);
        			try {
    					socket.receive(packet);

    					
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
                    process(packet);
        		}
    			
    		}
    	};
    	receive.start();
    }
    
    private void sendToAll(String message){
		if(message.startsWith("/m/")){
    	   String text = message.substring(3);
		   text = text.split("/e/")[0];
		   System.out.println(message);
		}
		
    	for(int i = 0; i < clients.size(); i++){
    		ServerClient client = clients.get(i);
    		send(message.getBytes(), client.address, client.port);
    	}
    }
    
    private void send(final byte[] data, InetAddress address, int port){
    	send = new Thread("Send"){
    		public void run(){
    			DatagramPacket packet = new DatagramPacket(data, data.length , address, port);
    			try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	};
    	send.start();
    	
    }
    
    private void send(String message, InetAddress address, int port){
    	message += "/c/";
    	send(message.getBytes(), address, port);
    	
    }
    
    private void process(DatagramPacket packet){
    	String string = new String(packet.getData());
    	if(raw) System.out.println(string);
    	if (string.startsWith("/c/")){
    		//UUID id = UUID.randomUUID();
    		int id = UniqueIdentifier.getIdentifier();
    		String name = string.split("/c/|/e/")[1];
    		System.out.println("Identifier: " + id + " ; " + name + " : connected");
    		clients.add(new ServerClient(name, packet.getAddress(), packet.getPort(), id));
    		//System.out.println(string.substring(3, string.length()));
    		String ID = "/c/" + id;
    		send(ID, packet.getAddress(), packet.getPort());
    	}
    	else if(string.startsWith("/m/")){
    		sendToAll(string);
    	}
    	else if(string.startsWith("/d/")){
    		String id = string.split("/d/|/e/")[1];
    		disconnect(Integer.parseInt(id), true);
    	}
    	else if(string.startsWith("/i/")){
    		clientResponse.add(Integer.parseInt(string.split("/i/|/e/")[1]));
    	}
    	else{
    		System.out.println(string);
    	}
    }
    
    private void disconnect(int id, boolean status){
    	ServerClient c = null;
    	boolean existed = false;
    	String name = "";
    	for(int i=0; i < clients.size(); i++){
    			if(clients.get(i).getID() == id){
    				c = clients.get(i);
    				clients.remove(i);
    				existed = true;
    				break;
    			}
    	}
    	if(!existed) return;
    	String message = "";
    	if(status){
    		message = "Client disconnected is : " + c.name + " " + c.getID() + " @ " + c.address.toString() + " : " + c.port + " disconnected";
    	}else{
    		message = "Client timed out is : " + c.name + " " + c.getID() + " @ " + c.address.toString() + " : " + c.port + " timed out";
    	}
    	System.out.println(message);
    }
    
}
