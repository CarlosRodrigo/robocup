package simple_soccer_lib.positions_monitor;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.FieldPerception;
import simple_soccer_lib.perception.ParsePerception;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.Patterns;

/**
 * This class implements a monitor that just parses the positions of the players and the ball.
 */
public class MonitorCommunicator {
	private static final int  MSG_SIZE = 2052; // Size of socket buffer

	private DatagramSocket socket; // socket to communicate with server
	private InetAddress    host;   // server address
	private int            port;   // server port
	
	MonitorMessageParser parser;
	
	public MonitorCommunicator(InetAddress serverAddress) {
		host = serverAddress;
		port = 6000;
	}

	public void connect() throws IOException {
		socket = new DatagramSocket(); //passar socket e address aqui? (e remover do send)
		
		byte[] buffer = new byte[MSG_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE);

		send("(dispinit)"); //connect to the server

		socket.receive(packet); //esta primeira mensagem é descartada

		port = packet.getPort();
		//System.out.println("Porta recebida --> " + port); //diferente da que e usada para conectar
		
		parser = new MonitorMessageParser();
	}
	
	// This destructor closes socket
	public void finalize() {
		socket.close();
	}

	// This is main method, that should be called regularly
	public boolean update(FieldPerception field) throws IOException {
		byte[] buffer = new byte[MSG_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE);

		socket.receive(packet);
		
		boolean hasPerceptions = parser.parse(buffer); 
		
		if (hasPerceptions) {
			field.overwrite( parser.getFieldPerception() );			
			return true;
		}

		return false;
	}

	// This function sends via socket message to the server
	private void send(String message) {
		byte[] buffer = Arrays.copyOf(message.getBytes(), MSG_SIZE);
		try {
			DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE, host, port);
			socket.send(packet);
		} catch (IOException e) {
			System.err.println("socket sending error " + e);
		}

	}


}
