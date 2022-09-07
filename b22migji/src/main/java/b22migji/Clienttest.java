package b22migji;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public class Clienttest {
	public static void main(String [] args) {
		
		if (args.length > 0) {
			try (ZContext context = new ZContext()){
				Socket socket = context.createSocket(SocketType.DEALER);
				socket.connect(args[0]);
				String msg = "{\"subscribe\":true}";
				socket.send(msg);
				
				byte[] response = socket.recv();
				System.out.println("Reply from server " + new String(response, ZMQ.CHARSET));
			} ;
		}else {
			System.out.println("Please specify an url");
		}
		
	}

}
