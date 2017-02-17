package ball_follower_team;

import java.net.UnknownHostException;


public class MainBall {

	public static void main(String[] args) throws UnknownHostException {
		BallFollowerTeam team1 = new BallFollowerTeam("a");
		//Team1 team2 = new Team1("b");
		
		team1.launchTeamAndServer();
		//team2.launchTeam();
	}
	
}



//public static void main(String[] args) throws UnknownHostException {
//Player1[] players = new Player1[7];
//
//try {
//	System.out.println(" >> Iniciando servidor...");
//	
//	Runtime r = Runtime.getRuntime();
//	Process p = r.exec("cmd /c tools\\startServer.cmd");
//	p.waitFor();
//	BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
//	String line = "";
//
////	while ((line = b.readLine()) != null) {
////	  System.out.println(line);
////	  System.out.println(".");
////	}
////	b.close();
//
//} catch(Exception e) {
//	e.printStackTrace();
//	System.out.println("Não pode iniciar o servidor!");
//	return;
//}
//
//try {
//	Thread.sleep(1000);
//} catch (InterruptedException e1) {
//	e1.printStackTrace();
//}
//
//System.out.println(" >> Iniciando jogadores...");
//for (int i = 0; i < players.length; i ++) {
//	try {
//		players[i] = new Player1();
//	} catch (UnknownHostException e) {
//		e.printStackTrace();
//		System.out.println("Erro iniciando os agentes.");
//	}
//	players[i].start();
//}
//
//System.out.println(" >> Aguardando jogadores encerrarem...");
////for (int i = 0; i < players.length; i ++) {
////	try {
////		players[i].join();
////	} catch (InterruptedException e) {
////		e.printStackTrace();
////	};
////}
//
//System.out.println("Todos os jogadores encerrados com sucesso!");
//}
