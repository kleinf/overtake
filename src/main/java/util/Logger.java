package util;

/**
 * @author Administrator
 * 
 */
public final class Logger {

	private static Logger log;

	private Logger() {
	}

	/**
	 * @return Logger
	 */
	public static synchronized Logger getInstance() {
		if (log == null) {
			log = new Logger();
		}
		return log;
	}

	/**
	 * @param s
	 *            String
	 */
	public void log(final String s) {
		System.out.println(s);
	}
}