package util;

/**
 * @author Administrator
 * 
 */
public final class PseudoLogger {

	private static PseudoLogger log;

	private PseudoLogger() {
	}

	/**
	 * @return Logger
	 */
	public static synchronized PseudoLogger getInstance() {
		if (log == null) {
			log = new PseudoLogger();
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