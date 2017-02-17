package simple_soccer_lib.comm;

public class GoalInfo extends ObjectInfo {
	private char m_side;

	// Initialization member functions
	public GoalInfo() {
		super("goal");
		m_side = ' ';
	}

	public GoalInfo(char side) {
		super("goal " + side);
		m_side = side;
	}

	public char getSide() {
		return m_side;
	}
}
