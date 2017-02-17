package simple_soccer_lib.comm;

public class FlagInfo extends ObjectInfo {
	public char m_type; // p|g
	public char m_pos1; // t|b|l|c|r
	public char m_pos2; // l|r|t|c|b
	public int m_num;   // 0|10|20|30|40|50
	public boolean m_out;
	public boolean empty;

	// Initialization member functions
	public FlagInfo() {
		super("flag");
		m_type = ' ';
		m_pos1 = ' ';
		m_pos2 = ' ';
		m_num = 0;
		m_out = false;
		empty = true;
	}

	public FlagInfo(String flagType, char type, char pos1, char pos2, int num, boolean out) {
		super(flagType);
		m_type = type;
		m_pos1 = pos1;
		m_pos2 = pos2;
		m_num = num;
		m_out = out;
		empty = false;
	}

	@Override
	public String toString() {
		return "FlagInfo [m_type=" + m_type + ", m_pos1=" + m_pos1
				+ ", m_pos2=" + m_pos2 + ", m_num=" + m_num + ", m_out="
				+ m_out + "]";
	}

//	public FlagInfo(char type, char pos1, char pos2, int num, boolean out) {
//		super("flag");
//		m_type = type;
//		m_pos1 = pos1;
//		m_pos2 = pos2;
//		m_num = num;
//		m_out = out;
//	}
}
