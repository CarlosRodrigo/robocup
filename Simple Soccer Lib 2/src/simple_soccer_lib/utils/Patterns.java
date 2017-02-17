package simple_soccer_lib.utils;

import java.util.regex.Pattern;

public class Patterns {
	public final static Pattern P_INFO = Pattern.compile("\\s");
	public final static int CASE_FLAG = Pattern.CASE_INSENSITIVE;
	public final static Pattern P_PLAYER = Pattern.compile("^(player|p)$", CASE_FLAG);
	public final static Pattern P_BALL   = Pattern.compile("^(ball|b)$", CASE_FLAG);
	public final static Pattern P_GOAL   = Pattern.compile("^(goal|g)$", CASE_FLAG);
	public final static Pattern P_FLAG   = Pattern.compile("^(flag|f)$", CASE_FLAG);
	public final static Pattern P_LINE   = Pattern.compile("^(line|l)$", CASE_FLAG);
	public final static Pattern P_QUOTE  = Pattern.compile("\"");
	public final static Pattern P_TYPE   = Pattern.compile("^(p|g)$");
	public final static Pattern P_NUMBER = Pattern.compile("^\\d{2}$");
	public final static Pattern P_LR     = Pattern.compile("^(l|r)$");
	public final static Pattern P_LRC    = Pattern.compile("^(l|r|c)$");
	public static Pattern MESSAGE_PATTERN = Pattern.compile("^\\((\\w+?)\\s.*");
	public static Pattern HEAR_PATTERN = Pattern.compile("^\\(hear\\s(\\w+?)\\s(\\w+?)\\s(.*)\\).*");
}
