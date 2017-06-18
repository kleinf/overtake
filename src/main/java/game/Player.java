package game;

import org.jdom2.Element;

import swing.util.AnimatedImage;

/**
 * @author Administrator
 * 
 */
public class Player {

	private boolean active;
	private int playerId;
	private String playerName;
	private int playerColor;
	private String playerImageName;
	private AnimatedImage playerImage;
	private int numFields;
	private boolean computer;
	private int repairPoints;

	/**
	 * @param playerId
	 *            int
	 * @param playerColor
	 *            int
	 * @param computer
	 *            boolean
	 */
	protected Player(final int playerId, final int playerColor, final boolean computer) {
		this(playerId, "Player " + (playerId + 1), playerColor, computer);
	}

	/**
	 * @param playerId
	 *            int
	 * @param playerName
	 *            String
	 * @param playerColor
	 *            int
	 * @param computer
	 *            boolean
	 */
	public Player(final int playerId, final String playerName, final int playerColor, final boolean computer) {
		// Netzwerkspieler werden inaktiv initialisiert,
		// da gewartet werden muss, bis sie sich anmelden
		active = true;
		this.playerName = "";
		numFields = 0;
		repairPoints = 0;
		this.playerId = playerId;
		this.playerName = playerName;
		this.playerColor = playerColor;
		this.computer = computer;
	}

	/**
	 * @param data
	 *            Element
	 */
	protected Player(final Element data) {
		active = true;
		playerName = "";
		numFields = 0;
		repairPoints = 0;
		setData(data);
	}

	/**
	 * @param active
	 *            boolean
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}

	/**
	 * @return boolean
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @return int
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerName
	 *            String
	 */
	public void setPlayerName(final String playerName) {
		this.playerName = playerName;
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
	 * @param playerImageName
	 *            String
	 */
	public void setPlayerImageName(final String playerImageName) {
		this.playerImageName = playerImageName;
	}

	/**
	 * @return String
	 */
	public String getPlayerImageName() {
		return playerImageName;
	}

	/**
	 * @param playerImage
	 *            AnimatedImage
	 */
	public void setPlayerImage(final AnimatedImage playerImage) {
		this.playerImage = playerImage;
	}

	/**
	 * @return AnimatedImage
	 */
	public AnimatedImage getPlayerImage() {
		return playerImage;
	}

	/**
	 * @param numFields
	 *            int
	 */
	public void setNumFields(final int numFields) {
		this.numFields = numFields;
	}

	/**
	 * @return int
	 */
	public int getNumFields() {
		return numFields;
	}

	/**
	 * @param computer
	 *            boolean
	 */
	public void setComputer(final boolean computer) {
		this.computer = computer;
	}

	/**
	 * @return boolean
	 */
	public boolean isComputer() {
		return computer;
	}

	/**
	 * @param repairPoints
	 *            int
	 */
	public void setRepairPoints(final int repairPoints) {
		this.repairPoints = repairPoints;
	}

	/**
	 * @return int
	 */
	public int getRepairPoints() {
		return repairPoints;
	}

	/**
	 * 
	 */
	public void addRepairPoints() {
		repairPoints++;
	}

	/**
	 * 
	 */
	public void subRepairPoints() {
		repairPoints--;
	}

	/**
	 * @return Element
	 */
	protected Element getData() {
		final Element player = new Element("player");
		player.setAttribute("active", Boolean.toString(active));
		player.setAttribute("playerId", Integer.toString(playerId));
		player.setAttribute("playerName", playerName);
		player.setAttribute("playerColor", Integer.toString(playerColor));
		player.setAttribute("playerImageName", playerImageName);
		player.setAttribute("numFields", Integer.toString(numFields));
		player.setAttribute("computer", Boolean.toString(computer));
		player.setAttribute("repairPoints", Integer.toString(repairPoints));
		return player;
	}

	/**
	 * @param data
	 *            Element
	 */
	private void setData(final Element data) {
		active = Boolean.parseBoolean(data.getAttributeValue("active"));
		playerId = Integer.parseInt(data.getAttributeValue("playerId"));
		playerName = data.getAttributeValue("playerName");
		playerColor = Integer.parseInt(data.getAttributeValue("playerColor"));
		playerImageName = data.getAttributeValue("playerImageName");
		numFields = Integer.parseInt(data.getAttributeValue("numFields"));
		computer = Boolean.parseBoolean(data.getAttributeValue("computer"));
		repairPoints = Integer.parseInt(data.getAttributeValue("repairPoints"));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return playerName;
	}
}