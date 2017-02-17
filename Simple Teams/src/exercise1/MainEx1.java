package exercise1;

import java.net.UnknownHostException;


public class MainEx1 {

	public static void main(String[] args) throws UnknownHostException {
		Exercise1Team team1 = new Exercise1Team("a", true);
		Exercise1Team team2 = new Exercise1Team("b", false);
		
		team1.launchTeamAndServer();
		team2.launchTeam();
	}
	
}
