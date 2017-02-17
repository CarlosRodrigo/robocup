package ball_follower_team;

import java.net.UnknownHostException;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.FieldPerception;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.Vector2D;


public class BallFollowerPlayer extends Thread {
	private PlayerCommander commander;
	
	private PlayerPerception selfPerc;
	private FieldPerception  fieldPerc;
	
	
	public BallFollowerPlayer() throws UnknownHostException {
		commander = new PlayerCommander("RED", "localhost", 6000);		
	}
	
	public BallFollowerPlayer(PlayerCommander player) {
		commander = player;		
	}

	@Override
	public void run() {
		System.out.println(">> 1. Waiting initial perceptions...");
		selfPerc  = commander.perceiveSelfBlocking();
		fieldPerc = commander.perceiveFieldBlocking();
		
		System.out.println(">> 2. Moving to initial position...");
		commander.doMoveBlocking(-25.0d, 0.0d);
		
		selfPerc  = commander.perceiveSelfBlocking();
		fieldPerc = commander.perceiveFieldBlocking();
		
		System.out.println(">> 3. Now starting...");
		while (commander.isActive()) {
			
			if (isAlignedToBall()) {
				runToBall();
			} else {
				turnToBall();
			}

			updatePerceptions(); //non-blocking
		}
			
	}

	private boolean isAlignedToBall() {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		Vector2D myPos = selfPerc.getPosition();
		
		if (ballPos == null || myPos == null) {
			return false;			
		}
		
		double angle = selfPerc.getDirection().angleFrom(ballPos.sub(myPos));
		//System.out.println("Vetores: " + ballPos.sub(myPos) + "  " + selfPerc.getDirection());
		//System.out.println(" => Angulo agente-bola: " + angle);
		
		return angle < 15.0d && angle > -15.0d;
	}
	
	//para debugar
	double angleToBall() {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		Vector2D myPos = selfPerc.getPosition();
		
		return selfPerc.getDirection().angleFrom(ballPos.sub(myPos));
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

	private void turnToBall() {
		Vector2D ballPos = fieldPerc.getBall().getPosition();
		Vector2D myPos = selfPerc.getPosition();
		System.out.println(" => Angulo agente-bola: " + angleToBall() + " (desalinhado)");
		System.out.println(" => Posicoes: ball = " + ballPos + ", player = " + myPos);
		
		Vector2D newDirection = ballPos.sub(myPos);
		System.out.println(" => Nova direcao: " + newDirection);
		
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		commander.doTurnToDirectionBlocking(newDirection);		
	}
	
	private void runToBall() {
		commander.doDashBlocking(100.0d);
	}

}
