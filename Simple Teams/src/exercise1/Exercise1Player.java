package exercise1;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.FieldPerception;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.Vector2D;


public class Exercise1Player extends Thread {
	private static final double ERROR_RADIUS = 2.0d;

	private PlayerCommander commander;
	
	private PlayerPerception selfPerc;
	private FieldPerception  fieldPerc;
	
	private Vector2D target;
	
	public Exercise1Player(PlayerCommander player, double x, double y) {
		commander = player;
		target = new Vector2D(x, y);
	}

	@Override
	public void run() {
		System.out.println(">> 1. Waiting initial perceptions...");
		selfPerc  = commander.perceiveSelfBlocking();
		fieldPerc = commander.perceiveFieldBlocking();
		
		System.out.println(">> 2. Movendo para uma posição inicial aleatoria...");
		commander.doMoveBlocking(Math.random() * -52.0, (Math.random() * 78.0) - 34.0);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		selfPerc  = commander.perceiveSelfBlocking();  //para ler a nova posicao (após o move)
		fieldPerc = commander.perceiveFieldBlocking();
		
		System.out.println(">> 3. Turning to the target...");
		turnToTarget();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(">> 4. Going to the target...");
		while (commander.isActive() && !arrivedAtTarget()) {
			if (!isAlignedToTarget()) {
				System.out.println(">>    - turning to the target...");
				turnToTarget();
			} else {
				commander.doDashBlocking(100.0d);
			}
			
			updatePerceptions(); //non-blocking
		}
			
	}

	private boolean arrivedAtTarget() {
		Vector2D myPos = selfPerc.getPosition();
		return Vector2D.distance(myPos, target) <= ERROR_RADIUS;
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

	private void turnToTarget() {
		Vector2D myPos = selfPerc.getPosition();
		System.out.println(" => Target = " + target + " -- Player = " + myPos);
		
		Vector2D newDirection = target.sub(myPos);
		
		commander.doTurnToDirectionBlocking(newDirection);
		commander.doTurnToDirectionBlocking(newDirection);
	}
	
	private boolean isAlignedToTarget() {
		Vector2D myPos = selfPerc.getPosition();
		
		double angle = selfPerc.getDirection().angleFrom(target.sub(myPos));
		
		return angle < 15.0d && angle > -15.0d;
	}
	
}

