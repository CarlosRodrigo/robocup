package keyboard_team;

import java.awt.event.KeyEvent;

import simple_soccer_lib.PlayerCommander;

public class KeyboardPlayer extends Thread {
	private int LOOP_INTERVAL = 100;  //0.1s
	
	private PlayerCommander commander;
	private KeyboardController keyboard;
	
	public KeyboardPlayer(PlayerCommander player, boolean standardKeys) {
		commander = player;
		if (standardKeys) {
			keyboard = new KeyboardController();
		} else {
			keyboard = new KeyboardController(KeyEvent.VK_UP, KeyEvent.VK_LEFT,
												KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN,
												KeyEvent.VK_NUMPAD0);
		}
	}

	@Override
	public void run() {
		System.out.println(">> Main loop ...");
		long nextIteration = System.currentTimeMillis() + LOOP_INTERVAL;
		
		while (true) {
			ActionPressed action = keyboard.getAction();
			
			if (action == null) continue; //"salta" o restante do loop
			
			switch(action) {
			case KICK:
				System.out.println("Kick!");
				commander.doKick(100.0d, 0.0d);
				break;
			case RUN:
				System.out.println("Run!");
				commander.doDash(100.0d);
				break;
			case TURN_BACK:
				System.out.println("Turn!");
				commander.doTurn(180.0d);
				break;
			case TURN_LEFT:
				System.out.println("Left!");
				commander.doTurn(-20.0d);
				break;
			case TURN_RIGHT:
				System.out.println("Right!");
				commander.doTurn(20.0d);
				break;
			default:
				break;			
			}

			sleepUntil(nextIteration);
			nextIteration += LOOP_INTERVAL;
		}
		
	}

	private void sleepUntil(long timeMillis) {
		long diff = timeMillis - System.currentTimeMillis();
		if (diff < 0) {
			System.out.println("System is too slow or this client is processing too much!)");
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
