package util;

import java.io.File;

/**
 * @author Administrator
 * 
 */
public final class Constants {

	private Constants() {
		// Instanziierung unterbinden
	}

	public static final String NAME = "Overtake";
	private static final String VERSION = "5.0";
	public static final String USERPATH = System.getProperty("user.dir");
	public static final File USERDIR = new File(USERPATH);

	// Konstanten fuer Netzwerk-Komponenten
	public static final int NET_ALL = -1;
	public static final String NET_DIVIDER = "|";
	public static final String NET_DIVIDER_ESCAPED = "\\|";
	public static final String NET_DIVIDER_DOUBLE = "||";
	public static final String NET_DIVIDER_DOUBLE_ESCAPED = "\\|\\|";
}
