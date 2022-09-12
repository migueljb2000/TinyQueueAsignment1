package b22migji;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.json.*;

public class Clienttest {
	public static void main(String [] args) {
		
		if (args.length > 0) {
			try (ZContext context = new ZContext()){
				Socket socket = context.createSocket(SocketType.DEALER);
				
				System.out.println(joinqueue(socket, args, "miguel"));
				System.out.println(subscribe(socket, args));
				//heartbeat(socket, args);
			} ;
		}else {
			System.out.println("Please specify an url");
		}
		
	}
	
	public static String subscribe(Socket socket, String [] args) {
		
		socket.connect(args[0]);
		JSONObject msg = new JSONObject();
		msg.put("subscribe", "true");
		socket.send(msg.toString());
		byte[] response = socket.recv();
		 
		return new String(response, ZMQ.CHARSET);
	}
	
	public static String joinqueue(Socket socket, String [] args, String name) {
		
		socket.connect(args[0]);
		JSONObject login = new JSONObject();
		login.put("enterQueue", "true");
		login.put("name", name);
		socket.send(login.toString());
		byte[] ticket = socket.recv();
		System.out.println("hello");
		System.out.println("hello");
		
		return new String(ticket, ZMQ.CHARSET);
		
	}
	
	public static void heartbeat(Socket socket, String [] args) {
		/* wait is giving java.lang.IllegalMonitorStateException: current thread is not owner
		*  I'm trying to set up the heartbeat signal so it gets from the server the period 
		*  between heartbeats and it waits that amount of time. 
		*  
		*  Waiting for the server to send a heartbeat to send our heartbeat would be better 
		*  but I haven't seen a specific method for that
		*
		*/
		Long time = (long) socket.getHeartbeatTtl();
		byte[] heartbeat = socket.getHeartbeatContext();
		while (1 < 2) {
			socket.send("");
			try {
				socket.wait(4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
