package simple_soccer_lib.comm;

//
//	File:			Krislet.java
//	Author:		Krzysztof Langner
//	Date:			1997/04/28
//
//********************************************
//      Updated:               2008/03/01
//      By:               Edgar Acosta
//
//********************************************
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
 * This class implements a player.
 * 
 * Adapted from code available in: http://www.nmai.ca/research-projects/agent-imitation/
 */
public class KrisletCommunicator {
	private static final int  MSG_SIZE = 4096; // Size of socket buffer

	private DatagramSocket socket; // socket to communicate with server
	private InetAddress    host;   // server address
	private int            port;   // server port
	
	private String         team;   // team name
	
	private boolean        isPlaying;
	
	private int  initialUniformNumber;
	private char initialSide;
	
	
	// This constructor opens socket for connection with server
	public KrisletCommunicator(InetAddress serverAddress, int serverPort, String teamName) {
		host = serverAddress;
		port = serverPort;
		team = teamName;
	}

	public void connect() throws IOException {
		socket = new DatagramSocket();
		
		byte[] buffer = new byte[MSG_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE);

		// first we need to initialize connection with server
		sendInitialCommand();
		isPlaying = true;
		
		socket.receive(packet);
		parseInitialResponse(new String(buffer));
		port = packet.getPort();
	}
	
	public boolean isActive() {
		return isPlaying; //TODO: melhorar: testar se o socket está conectado, etc.
	}
	
	public char getInitialSide() {
		return initialSide;
	}
	
	public int getInitialUniformNumber() {
		return initialUniformNumber;
	}
	
	// This destructor closes socket
	public void finalize() {
		isPlaying = false;
		socket.close();
	}

	// ===============================================
	// Protected member functions
	// -----------------------------------------------

	/** 
	 * Atualiza informacoes do player e dos objetos do campo.
	 * 
	 * Retorna true/false para indicar se as percepções foram atualizadas.
	 */
	public boolean update(PlayerPerception self, FieldPerception field) {  //PlayerCommander pc) {
		String message = receive();

		Matcher m = Patterns.MESSAGE_PATTERN.matcher(message);
		if (!m.matches()) {
			throw new Error("Invalid message: " + message);
		}
		
		if (m.group(1).equals("see")) {
			VisualInfo info = new VisualInfo(message);
			info.parse();

			if (self != null && field != null) {
				ParsePerception.parse(self, field, info);
			}
			
			return true;						
		} 
//		else if (m.group(1).equals("hear")) {
//			System.out.println("\"Hear\" perceptions not supported yet!");
//			parseHear(message);
//		}
		
		return false;
	}
	
	// This is main loop for player
//	protected void mainLoop() throws IOException {
//		// Now we should be connected to the server
//		// and we know side, player number and play mode
//		while (isPlaying)
//			parseSensorInformation(receive());
//		finalize();
//	}

	// ===============================================
	// Implementation of SendCommand Interface
	// -----------------------------------------------

	// This function sends move command to the server
	public void move(double x, double y) {
		send("(move " + Double.toString(x) + " " + Double.toString(y) + ")");
	}

	// This function sends turn command to the server
	public void turn(double moment) {
		send("(turn " + Double.toString(moment) + ")");
	}

	public void turn_neck(double moment) {
		send("(turn_neck " + Double.toString(moment) + ")");
	}

	// This function sends dash command to the server
	public void dash(double power) {
		send("(dash " + Double.toString(power) + ")");
	}

	// This function sends kick command to the server
	public void kick(double power, double direction) {
		send("(kick " + Double.toString(power) + " " + Double.toString(direction) + ")");
	}

	// This function sends say command to the server
	public void say(String message) {
		send("(say " + message + ")");
	}

	// This function sends chage_view command to the server
	public void changeView(String angle, String quality) {
		send("(change_view " + angle + " " + quality + ")");
	}

	// This function sends bye command to the server
	public void bye() {
		isPlaying = false;
		send("(bye)");
	}	

	
	// ===============================================
	// Collection of communication functions
	// -----------------------------------------------

	// This function sends initialization command to the server
	private void sendInitialCommand() {
		send("(init " + team + " (version 9))");
	}

	// This function parses initial message from the server
	protected void parseInitialResponse(String message) throws IOException {
		Matcher m = Pattern.compile("^\\(init\\s([lr])\\s(\\d{1,2})\\s(\\w+)\\).*$").matcher(message);
		if (!m.matches()) {
			throw new IOException(message);
		}
		initialSide = m.group(1).charAt(0);                  // l ou r
		initialUniformNumber = Integer.parseInt(m.group(2));
		System.out.println("Status inicial da partida: " + m.group(3)); 
	}	

	// This function parses sensor information
//	private void parseSensorInformation(String message) throws IOException {
//		// First check kind of information
//		Matcher m = Patterns.MESSAGE_PATTERN.matcher(message);
//		if (!m.matches()) {
//			throw new IOException(message);
//		}
//		if (m.group(1).equals("see")) {
//			VisualInfo info = new VisualInfo(message);
//			info.parse();
//			//brain.see(info);
//		} else if (m.group(1).equals("hear")) {
//			parseHear(message);
//		}
//	}

	// This function parses hear information
//	private void parseHear(String message) throws IOException {
//		// get hear information
//		Matcher m = Patterns.HEAR_PATTERN.matcher(message);
//		int time;
//		String sender;
//		String uttered;
//		if (!m.matches()) {
//			throw new IOException(message);
//		}
//		time = Integer.parseInt(m.group(1));
//		sender = m.group(2);
//		uttered = m.group(3);
		
		//ignorada!
		
//		if (sender.equals("referee"))
//			brain.hear(time, uttered);
//		else if (! sender.equals("self"))
//			brain.hear(time, Integer.parseInt(sender), uttered);
//	}

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

	// This function waits for new message from server
	private String receive() {
		byte[] buffer = new byte[MSG_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE);
		try {
			socket.receive(packet);
		} catch (SocketException e) {
			System.err.println("shutting down...");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("socket receiving error " + e);
			e.printStackTrace();
		}
		return new String(buffer);
	}

}
