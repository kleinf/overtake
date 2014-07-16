package game;

import java.awt.Color;

/**
 * @author Administrator
 * 
 */
public class NetOptions {

	private String host;
	private int port;
	private String playerName;
	private int playerColor;
	private String netId;

	/**
	 * Constructor.
	 */
	public NetOptions() {
		host = "localhost";
		port = 4321;
		playerName = "Spieler";
		playerColor = Color.DARK_GRAY.getRGB();
	}

	/**
	 * @param host
	 *            String
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 * @return String
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param port
	 *            int
	 */
	public void setPort(final int port) {
		this.port = port;
	}

	/**
	 * @return int
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param name
	 *            String
	 */
	public void setPlayerName(final String name) {
		playerName = name;
	}

	/**
	 * @return String
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param playerColor
	 *            int
	 */
	public void setPlayerColor(final int playerColor) {
		this.playerColor = playerColor;
	}

	/**
	 * @return int
	 */
	public int getPlayerColor() {
		return playerColor;
	}

	/**
	 * @param netId
	 *            String
	 */
	public void setId(final String netId) {
		this.netId = netId;
	}

	/**
	 * @return String
	 */
	public String getId() {
		return netId;
	}
}