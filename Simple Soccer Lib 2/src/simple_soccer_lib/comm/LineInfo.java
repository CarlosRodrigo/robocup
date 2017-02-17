package simple_soccer_lib.comm;

public class LineInfo extends ObjectInfo {
	public char m_kind; // l|r|t|b

	// Initialization member functions
	public LineInfo() {
		super("line");
	}

	public LineInfo(char kind) {
		super("line");
		m_kind = kind;
	}
}
