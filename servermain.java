package chatprojectserver;


public class servermain {
	
	private int port;
	private Server server;
	
	public servermain(int port){
	   this.port = port;
	   server = new Server(port);
	}
   
	public static void main(String[] args){
		int port;
		if(args.length != 1){
			System.out.println("Usage : java -jar chatproject.jar [port]");
			return;
		}
		port = Integer.parseInt(args[0]);
		new servermain(port);
	}
	
}
