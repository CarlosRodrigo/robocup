package simple_soccer_lib.perception;

import simple_soccer_lib.comm.PlayerInfo;
import simple_soccer_lib.utils.Vector2D;


public class PlayerPerception extends ObjectPerception {
	private String team;
	private int number; 
	
	private Vector2D direction;
	
	boolean goalie = false;
	private int side;
	
	public PlayerPerception(PlayerInfo playerInfo) {
		super();
		this.number = playerInfo.getTeamNumber();
		this.team = playerInfo.getTeamName();
		this.goalie = playerInfo.isGoalie();
		this.direction = null;
	}
	
	public PlayerPerception() {
		this.number = 0; //not being able to see number
	}

	public String getTeam(){
		return this.team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	
	public int getUniformNumber(){
		return this.number;
	}
	public void setUniformNumber(int num) {
		this.number = num;
	}
	
	public Vector2D getDirection() {
		return direction;
	}
	Vector2D getHeadDirection() {
		return direction;
	}
	
	public void setDirection(Vector2D dir) {
		this.direction = dir;
	}	
	@Deprecated
	void setHeadDirection(Vector2D dir) {
		this.direction = dir;
	}
	
	public boolean isGoalie() {
		return goalie;
	}
	public void setGoalie(boolean isGoalie) {
		this.goalie = isGoalie;
	}
	
//	public PlayerPerception copy() {
//	}

	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

	@Override
	public String toString(){
		return "Team:	"+team+
				"\nNumber:	"+number+
				"\nGoalie:	"+goalie+
				"\nDirection:	"+direction+
				"\nSide:	"+side;
	}
}