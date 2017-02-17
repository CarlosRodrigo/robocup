package simple_soccer_lib.positions_monitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import simple_soccer_lib.perception.FieldPerception;
import simple_soccer_lib.perception.ObjectPerception;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.Vector2D;


public class MonitorMessageParser {
	private byte[] buffer;
	private int nextPosition;
	
	private FieldPerception fieldPerception;

	public MonitorMessageParser() {
		this.buffer = null;
		this.nextPosition = -1;
	}
	
	public boolean parse(byte[] buffer) {
		this.buffer = buffer;
		this.nextPosition = 0;
		
		return parse_dispinfo_t();
	}
	
	public FieldPerception getFieldPerception() {
		return fieldPerception;
	}

	/* Reconhece esta struct em C, enviada pelo servidor:
		typedef struct {
			short mode;
			union {
 				show_info_t show;
 				msginfo_t msg;
 				drawinfo_t draw; //ignore
			} body;
		} dispinfo_t;
 	*/
	private boolean parse_dispinfo_t() {
		short mode = readShort();
		
		switch (mode) {
		case DispInfoMode.SHOW_MODE:
			parse_showinfo_t();
			return true;
		
		case DispInfoMode.MSG_MODE:
			parse_msginfo_t();
			break;
		
		default:
			printn("Unsupported mode: %d", mode);			
		}
		
		return false;
	}
	
	/*
		typedef struct {
 	 		char pmode;
 	 		team_t team[2];
 	 		pos_t pos[23]; //0 is the ball, 1-11 is team[0], 12-22 is team[1]
 			short time;
		} showinfo_t;	
 	*/
	private void parse_showinfo_t() {
		char pmode = readChar(); //playmode of the game ---> ver valores no manual (pp. 53-54), criar enum e usar em MatchInfo!
		printn("Game play mode: %d", (int)pmode);
		
		readChar(); //para pular um byte! porque o o servidor aparentemente adiciona, aqui, um byte de alinhamento, para que a próxima variável comece na proxima "palavra" (16 bytes) da memória
		
		String time0 = parse_team_t();
		String time1 = parse_team_t();
		
		ObjectPerception ball = new ObjectPerception();
		parse_pos_t_Ball(ball);
		
		ArrayList<PlayerPerception> players = new ArrayList<>(22);
		
		//time 0
		for (int i = 0; i < 11; i++) {
			parse_pos_t_Player(time0, players);
		}
		
		//time 1
		for (int i = 0; i < 11; i++) {
			parse_pos_t_Player(time1, players);
		}
		
		short time = readShort();
		printn("Tempo: %d", time);
		
		this.fieldPerception = new FieldPerception(time, players, ball);
	}

	/*
	typedef struct {
 		char name[16];
 		short score;
	} team_t;
	*/
	private String parse_team_t() {
		String name = readStringEndedInZero(16);
		short score = readShort();
		printn("Nome do time: \"%s\" , Score: %d", name, score);
		return name;
	}

	/*
	typedef struct {
 		short enable;
 		short side;
 		short unum;
 		short angle;
 		short x;
 		short y;
	} pos_t;
	*/
	private double getValueScaled(short value){
		return ((double)value/16);
	}
	private void parse_pos_t_Ball(ObjectPerception ball) {
					
		short enable = readShort();
		short side = readShort();
		short unum = readShort();
		short angle = readShort();						
		short x = readShort();
		short y = readShort();
		
		//840x544
		//52,5x34
	
		assert(side == 0);
		
		ball.setPosition(new Vector2D(getValueScaled(x), getValueScaled(y)));
	}
	
	private void parse_pos_t_Player(String team, List<PlayerPerception> players) {
		short enable = readShort();
		short side = readShort();
		short unum = readShort();
		short angle = readShort();
		short x = readShort();
		short y = readShort();
		
		if (enable == PlayerStatus.DISABLE) {
			return;
		}
		
		PlayerPerception player = new PlayerPerception();
		player.setTeam(team);
		player.setPosition(new Vector2D(getValueScaled(x), getValueScaled(y)));
		player.setGoalie(enable == PlayerStatus.GOALIE);
		player.setUniformNumber(unum);
		player.setDirection(new Vector2D((double)angle)); 
		player.setSide(side);
		players.add(player);
	}

	
	/*
	typedef struct {
 		short board;
 		char message[2048];
	} msginfo_t;
	*/
	private void parse_msginfo_t() {
		short board = readShort();
		String message = readStringEndedInZero(2048);
		
		printn("MSG: board=%d, msg=\"%s\"", board, message);
	}

	/////// METODOS BASICOS ///////

	//short (in C) with 16 bytes --> analogous to "char" in Java (an integer with 16 bytes!)
	private short readShort() {
//		int result = buffer[nextPosition++];
//		result = (result << 8) | buffer[nextPosition++];
//		return (short)result;
		
		byte b[] = new byte[2];
		b[0] = buffer[nextPosition++];
		b[1] = buffer[nextPosition++];
		return ByteBuffer.wrap(b).getShort();
	}
	
	//char (in C) with 8 bytes
	private char readChar() {
		return (char)buffer[nextPosition++];
	}
	
	//reads a string given as an array with the given size, but whose useful characters end before a '0' value
	private String readStringEndedInZero(int maxSize) {
		StringBuffer str = new StringBuffer();
		boolean foundZero = false;
		char currChar;
		
		for (int i = 0; i < maxSize; i++) {
			currChar = readChar();
			
			foundZero = foundZero || (currChar == 0);
			if (!foundZero) {
				str.append(currChar);
			}
		}		
		return str.toString();
	}	
	
	//for debugging purposes
	private void printn(String format, Object... args) {
//		System.out.printf("[MONITOR]" + format + "%n", args);
	}

}


class DispInfoMode { 
	public static final int 
		NO_INFO = 0, 
		SHOW_MODE = 1, 
		MSG_MODE = 2, 
		DRAW_MODE = 3, 
		BLANK_MODE = 4;
}


class MsgInfoTypes {
	public static final int 
		MSG_BOARD = 1, 
		LOG_BOARD = 2;
}


class PlayerStatus {
	public static final int
		DISABLE 	= 0,
		STAND 		= 0x01,
		KICK		= 0x02,
		KICK_FAULT 	= 0x04,
		GOALIE		= 0x08,
		CATCH		= 0x10,
		CATCH_FAULT = 0x20;
}
