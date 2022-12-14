package b22migji;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import java.util.Scanner;

import org.json.*;

public class Clienttest {
	public static void main(String [] args) {
		
		if (args.length > 0) {
			try (ZContext context = new ZContext()){
				Socket socket = context.createSocket(SocketType.DEALER);
				System.out.println("Enter the login name");
				Scanner s = new Scanner(System.in);
				String name = s.nextLine();
				System.out.println(joinqueue(socket, args, name));
				System.out.println(subscribe(socket, args));
				heartbeat(socket, args);
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
		Integer counter = 0;
		while(!Thread.currentThread().isInterrupted()) {
			//byte[] heartbeat = socket.recv();
			JSONObject heartbeatresp = new JSONObject();
			counter += 1;
			//System.out.println("hearbeat received " + counter + heartbeat.toString());
			socket.send(heartbeatresp.toString());
			//System.out.println("sent heartbeat");
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		/*
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
		*/
	}


