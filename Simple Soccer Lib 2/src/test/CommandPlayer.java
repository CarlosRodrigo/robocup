package test;

import java.awt.Button;
import java.awt.Color;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.FieldPerception;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.Vector2D;

public class CommandPlayer extends Thread {
	private int LOOP_INTERVAL = 100;  //0.1s
	
	private PlayerCommander commander;
	private PlayerPerception selfPerc;
	private FieldPerception  fieldPerc;
	
	private String comm;
	private boolean run;
	
	/**
	 * MOVE:							M -25 0			-> X e Y do ponto
	 * KICK:							K 100 20		-> Intensidade e angulo relativo
	 * RUN:								R 100			-> Intensidade
	 * TURN:							T 90			-> Angulo
	 * TURN TO DIRECTION:               TD  0 1         -> Vetor de direcao X, Y
	 * TURN TO POINT: 					TTP	-10 5		-> X e Y do ponto
	 * TURN TO BALL: 					TTB
	 * RUN TO POINT:					RTP -10 5 3		-> X, Y do ponto e taxa de erro
	 * RUN TO BALL: 					RTB 3			-> Taxa de erro
	 * SHOW PLAYER INFO (ALL):			SP
	 * SHOW PLAYER POSITION:			SPPOS
	 * SHOW PLAYER DIRECTION:			SPDIR
	 * SHOW PLAYER TEAM NAME:			SPTEA
	 * SHOW PLAYER UNIFORM NUMBER:		SPUNI
	 * SHOW TIME:						STIME
	 * SHOW QNT. PLAYERS IN VIEW:		SQPLS
	 * SHOW PLAYER INFO:				SPLIN BTEAM 5	-> Nome do time e numero do jogador
	 * SHOW QNT. PLAYERS SPECIFC TEAM:	SQPSS BTEAM		-> Nome do time
	 * SHOW BALL POSITION:				SBPOS
	 **/
	
	/* Observações:
	 * - Posições bagunçadas do jogador e da bola (Ex: x = -400 e 0);
	 * - Leitura das percepções do monitor pode estar causando esse erro;
	 * - Turn 50 seguido de -50 não retorna a posicição
	 * - Run 100 é andar aproximadamente 1 unidade de distancia, run 50 é andar aproximadamente metade e assim por diante
	 * - Kick 100 é chutar aproximadamente 40 unidades de distancia, kick 50 é chutar aproximadamente metade e assim por diante.
	 */
	
	public CommandPlayer(PlayerCommander player) {
		commander = player;
	}

	@Override
	public void run() {
		System.out.println(">> Main loop ...");
		long nextIteration = System.currentTimeMillis() + LOOP_INTERVAL;
		
		JFrame frame = new JFrame();
		frame.setTitle("COMM");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(new Color(31, 160, 31));
		frame.setSize(224, 80);
		
		final TextField tf = new TextField();
		tf.setBounds(16, 10, 125, 25);
		tf.addActionListener(new ActionListener() { 
			@Override public void actionPerformed(ActionEvent ev) { 
				comm = tf.getText();
				run = true;
			}
		});
		frame.add(tf);
		
		final Button btn = new Button("do");
		btn.setBounds(151, 10, 50, 25);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comm = tf.getText();
				run = true;
			}
		});
		frame.add(btn);
		
		String[] action = null;
		while (true) {
			try{
				if(action == null) commander.doMoveBlocking(-25d, 0d);
				
				if(run){
					updatePerceptions();
					
					action = comm.toUpperCase().split(" ");
					
					if(action[0].equalsIgnoreCase("M")){
						try{
							commander.doMoveBlocking(Double.parseDouble(action[1]), Double.parseDouble(action[2]));
							System.out.println(comm);
						}catch(Exception e){
							System.err.println("Comando inválido: "+comm+".\n"
									+"Ex: M 100 20	-> X e Y do ponto");
							e.printStackTrace();
						}
						
					}else if(action[0].equalsIgnoreCase("K")){
						try{
							commander.doKick(Double.parseDouble(action[1]), Double.parseDouble(action[2]));
							System.out.println(comm);
						}catch(Exception e){
							System.err.println("Comando inválido: "+comm+".\n"
									+"Ex: K 100 20	-> Intensidade e angulo relativo");
							e.printStackTrace();
						}
						
					}else if(action[0].equalsIgnoreCase("R")){
						try{
							commander.doDash(Double.parseDouble(action[1]));
							System.out.println(comm);
						}catch(Exception e){
							System.err.println("Comando inválido: "+comm+".\n"
									+"Ex: R 100		-> Intensidade");
							e.printStackTrace();
						}
						
					}else if(action[0].equalsIgnoreCase("T")){
						try{
							commander.doTurn(Double.parseDouble(action[1]));
							System.out.println(comm);
						}catch(Exception e){
							System.err.println("Comando inválido: "+comm+".\n"
									+"Ex: T 90		-> Angulo");
							e.printStackTrace();
						}
						
					}else if(action[0].equalsIgnoreCase("TD")){
						try{
							Vector2D direction = new Vector2D(Double.parseDouble(action[1]), Double.parseDouble(action[2]));
							commander.doTurnToDirection(direction);
							System.out.println(comm);
						}catch(Exception e){
							System.err.println("Comando inválido: "+comm+".\n"
									+"Ex: TD X Y		-> Vetor de direcao");
							e.printStackTrace();
						}
						
					}else if(action[0].equalsIgnoreCase("TTB")){
						turnToBall();
						System.out.println(comm);
						
					}else if(action[0].equals("RTB")){
						try{
							runToBall(Double.parseDouble(action[1]));
							System.out.println(comm);
						}catch(Exception e){
							System.err.println("Comando inválido: "+comm+".\n"
									+"Ex: RTB 3		-> Taxa de erro");
							e.printStackTrace();
						}
						
					}else if(action[0].equalsIgnoreCase("TTP")){
						try{
							turnToPoint(Double.parseDouble(action[1]), Double.parseDouble(action[2]));
							System.out.println(comm);
						}catch(Exception e){
							System.err.println("Comando inválido: "+comm+".\n"
									+"Ex: TTP	-10 5	-> X e Y do ponto");
							e.printStackTrace();
						}
						
					}else if(action[0].equalsIgnoreCase("RTP")){
						try{
							runToPoint(Double.parseDouble(action[1]), Double.parseDouble(action[2]), Double.parseDouble(action[3]));
							System.out.println(comm);
						}catch(Exception e){
							System.err.println("Comando inválido: "+comm+".\n"
									+"Ex: RTP -10 5 3	-> X, Y do ponto e taxa de erro");
							e.printStackTrace();
						}

					}else if(action[0].equalsIgnoreCase("SP")){
						System.out.println(action[0]+" pos: "+selfPerc.getPosition());
						System.out.println(action[0]+" dir: "+selfPerc.getDirection());
						System.out.printf(action[0]+" team, num: %s, %d %n", selfPerc.getTeam(), selfPerc.getUniformNumber());
						
					}else if(action[0].equalsIgnoreCase("SPPOS")){
						System.out.println(action[0]+": "+selfPerc.getPosition());
						
					}else if(action[0].equalsIgnoreCase("SPDIR")){
						System.out.println(action[0]+": "+selfPerc.getDirection());
						
					}else if(action[0].equalsIgnoreCase("SPTEA")){
						System.out.println(action[0]+": "+selfPerc.getTeam());
						
					}else if(action[0].equalsIgnoreCase("SPUNI")){
						System.out.println(action[0]+": "+selfPerc.getUniformNumber());
						
					}else if(action[0].equalsIgnoreCase("STIME")){
						System.out.println(action[0]+": "+fieldPerc.getTime());
						
					}else if(action[0].equalsIgnoreCase("SQPLS")){
						System.out.println(action[0]+": "+fieldPerc.getAllPlayers().size());
						
					}else if(action[0].equalsIgnoreCase("SPLIN")){
						try{
							System.out.println(action[0]+":\n"+fieldPerc.getTeamPlayer(action[1], Integer.parseInt(action[2])));
						}catch(Exception e){
							System.err.println("Comando inválido: "+comm+".\n"
									+"Ex: SPLIN BTEAM 5	-> Nome do time e numero do jogador");
							e.printStackTrace();
						}
						
					}else if(action[0].equalsIgnoreCase("SQPSS")){
						try{
							System.out.println(action[0]+": "+fieldPerc.getTeamPlayers(action[1]).size());
						}catch(Exception e){
							System.err.println("Comando inválido: "+comm+".\n"
									+"Ex: SQPSS BTEAM	-> Nome do time");
							e.printStackTrace();
						}
					}else if(action[0].equalsIgnoreCase("SBPOS")){
						System.out.println(action[0]+": "+fieldPerc.getBall().getPosition());
						
					}else{
						System.err.println("Comando inválido: "+comm);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			run = false;
			updatePerceptions();
			sleepUntil(nextIteration);
			nextIteration += LOOP_INTERVAL;
		}
		
	}
	
	private void turnToBall() {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		Vector2D myPos = selfPerc.getPosition();
		
		Vector2D newDirection = ballPos.sub(myPos);
		System.out.println(" => Ball = " + ballPos + " -- Player = " + myPos + " -- New Direction = " + newDirection);
		
		commander.doTurnToDirectionBlocking(newDirection);		
	}
	
	private void runToBall(double erro) {
		while (Math.abs(selfPerc.getPosition().getX() - fieldPerc.getBall().getPosition().getX()) > erro ||
				Math.abs(selfPerc.getPosition().getY() - fieldPerc.getBall().getPosition().getY()) > erro) {
			turnToBall();
			commander.doDashBlocking(100.0d);
			updatePerceptions();
		}
	}
	
	private void turnToPoint(double x, double y){
		Vector2D myPos = selfPerc.getDirection();
		Vector2D point = new Vector2D(x, y);
		Vector2D newDirection = point.sub(myPos);
		
		System.out.println(" => Point = " + point + " -- Player = " + myPos + " -- New Direction = " + newDirection);
		commander.doTurnToDirectionBlocking(newDirection);		
	}
	
	private void runToPoint(double x, double y, double erro) {
		Vector2D point = new Vector2D(x, y);
		while (Math.abs(selfPerc.getPosition().getX() - point.getX()) > erro ||
				Math.abs(selfPerc.getPosition().getY() - point.getY()) > erro) {
			turnToPoint(x, y);
			commander.doDashBlocking(100.0d);
			updatePerceptions();
		}
	}
	
	private boolean isAlignedToBall(double error) {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		Vector2D myPos = selfPerc.getPosition();
		
		if (ballPos == null || myPos == null) {
			return false;			
		}
		//System.out.println("Vetores: " + ballPos.sub(myPos) + "  " + selfPerc.getDirection());
		
		//double angle = Vector2D.angle(ballPos.sub(myPos), selfPerc.getDirection());
		double angle = selfPerc.getDirection().angleFrom(ballPos.sub(myPos));

		System.out.println(" => Angulo agente-bola: " + angle);
		
		return angle < error && angle > error; // 15 e -15
	}
		
	private void updatePerceptions() {
		PlayerPerception newSelf = commander.perceiveSelf();
		FieldPerception newField = commander.perceiveField();
		
		if (newSelf != null) {
			this.selfPerc = newSelf;
		}
		if (newField != null) {
			this.fieldPerc = newField;
		}
	}

	private void sleepUntil(long timeMillis) {
		long diff = timeMillis - System.currentTimeMillis();
		if (diff < 0) {
//			System.out.println("System is too slow or this client is processing too much!)");
			//this.LOOP_INTERVAL = 30;
		} else { 
			try {
				Thread.sleep(diff);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				System.out.println("Sleep interrupted, but going on...");
			}
		}
	}

}
