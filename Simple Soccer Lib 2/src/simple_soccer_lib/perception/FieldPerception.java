package simple_soccer_lib.perception;

import java.util.ArrayList;

/**
 * Guarda apenas os objetos móveis do campo: jogadores e bola.
 *
 */
public class FieldPerception {
	private int time; //tempo!
	
	private ArrayList<PlayerPerception> players; 	
	private ObjectPerception ball;
	
	public FieldPerception(int time, ArrayList<PlayerPerception> players, ObjectPerception ball) {
		this.time = time;
		this.players = players;
		this.ball = ball;
	}

	public FieldPerception() {
		this.players = new ArrayList<PlayerPerception>();
		this.ball = new ObjectPerception();
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public ArrayList<PlayerPerception> getAllPlayers() {
		return players;
	}

	public ArrayList<PlayerPerception> getTeamPlayers(String teamName) {
		ArrayList<PlayerPerception> lpp = new ArrayList<PlayerPerception>();
		for (PlayerPerception playerPerception : players) {
			if (playerPerception.getTeam().equals(teamName)) {
				lpp.add(playerPerception);
			}
		}
		return lpp;
	}
	
	public ObjectPerception getBall() {
		return ball;
	}

	public void setBall(ObjectPerception ball) {
		this.ball = ball;
	}

	//ATENCAO: este metodo faz "this" compartilhar objetos com "other"
	public void overwrite(FieldPerception other) {
		this.time = other.time;
		this.ball = other.ball;
		this.players.clear();
		this.players.addAll(other.players);
	}

	public PlayerPerception getTeamPlayer(String teamName, int uniformNumber) {
		ArrayList<PlayerPerception> lpp = new ArrayList<PlayerPerception>();
		for (PlayerPerception player : players) {
			if (player.getTeam().equals(teamName)
					&& player.getUniformNumber() == uniformNumber) {
				return player;
			}
		}
		return null;
	}
	
	//metodos get diversos...
	
	//metodo para retonar os players por time...
	
	
//	public FieldPerception copy() {
//	}
	
}