package exercise1;

import simple_soccer_lib.AbstractTeam;
import simple_soccer_lib.PlayerCommander;


public class Exercise1Team extends AbstractTeam {
	private boolean leftSide;

	public Exercise1Team(String suffix, boolean left) {
		super("EX1" + suffix, 2);
		this.leftSide = left;
	}

	@Override
	protected void launchPlayer(int ag, PlayerCommander commander) {
		double targetX, targetY;
		
		if (ag == 0) {
			targetY = 34.0d / 2;
		} else {
			targetY = -34.0d / 2;
		}
		
		if (leftSide) {
			targetX = 52.5d / 2;
		} else {
			targetX = -52.5d / 2;
		}
		
		Exercise1Player pl = new Exercise1Player(commander, targetX, targetY);
		pl.start();
	}

}
