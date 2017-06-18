package game;

import java.util.ArrayList;
import java.util.List;

import java.awt.Color;

import org.jdom2.Element;

/**
 * @author Administrator
 * 
 */
public final class GameOptions {

	private int numFieldsWidth;
	private int numFieldsHeight;
	private int fieldWidth;
	private int fieldHeight;
	private boolean fieldWidthRelative;
	private boolean fieldHeightRelative;
	private String fieldName;
	private float fieldAlpha;
	private int maxPlayers;
	private List<Player> players;
	private GameGoal gameGoal;
	private boolean setWhileFree;
	private boolean borderless;
	private boolean sticky;
	private boolean overloadOnEqual;
	private boolean emptyOverloaded;
	private boolean looseOverloaded;
	private int network;
	private String host;
	private int port;
	private String boardBgImageName;
	private boolean boardBgImageTiled;
	private int maxOverload;

	/**
	 * Constructor.
	 */
	public GameOptions() {
		numFieldsWidth = 8;
		numFieldsHeight = 10;
		fieldWidth = 40;
		fieldHeight = 40;
		fieldWidthRelative = true;
		fieldHeightRelative = true;
		fieldAlpha = 1.0F;
		maxPlayers = 2;
		players = new ArrayList<>();
		players.add(new Player(0, new Color(204, 204, 204).getRGB(), false));
		players.add(new Player(1, new Color(153, 153, 153).getRGB(), true));
		// players.add(new Player(2, new Color(102, 102, 102).getRGB(), false));
		// players.add(new Player(3, new Color(51, 51, 51).getRGB(), true));
		gameGoal = GameGoal.LAST_MAN_STANDING;
		setWhileFree = false;
		borderless = false;
		sticky = false;
		overloadOnEqual = true;
		emptyOverloaded = true;
		looseOverloaded = true;
		network = 0;
		host = "localhost";
		port = 4321;
		boardBgImageName = "images/backgrounds/steinhell3.gif";
		boardBgImageTiled = true;
		maxOverload = 0;
	}

	/**
	 * @param numFieldsWidth
	 *            int
	 */
	public void setNumFieldsWidth(final int numFieldsWidth) {
		if (numFieldsWidth < 2) {
			this.numFieldsWidth = 2;
		} else if (numFieldsWidth > 25) {
			this.numFieldsWidth = 25;
		} else {
			this.numFieldsWidth = numFieldsWidth;
		}
	}

	/**
	 * @return int
	 */
	public int getNumFieldsWidth() {
		return numFieldsWidth;
	}

	/**
	 * @param numFieldsHeight
	 *            int
	 */
	public void setNumFieldsHeight(final int numFieldsHeight) {
		if (numFieldsHeight < 2) {
			this.numFieldsHeight = 2;
		} else if (numFieldsHeight > 25) {
			this.numFieldsHeight = 25;
		} else {
			this.numFieldsHeight = numFieldsHeight;
		}
	}

	/**
	 * @return int
	 */
	public int getNumFieldsHeight() {
		return numFieldsHeight;
	}

	/**
	 * @param fieldWidth
	 *            int
	 */
	public void setFieldWidth(final int fieldWidth) {
		this.fieldWidth = fieldWidth;
	}

	/**
	 * @return int
	 */
	public int getFieldWidth() {
		return fieldWidth;
	}

	/**
	 * @param fieldHeight
	 *            int
	 */
	public void setFieldHeight(final int fieldHeight) {
		this.fieldHeight = fieldHeight;
	}

	/**
	 * @return int
	 */
	public int getFieldHeight() {
		return fieldHeight;
	}

	/**
	 * @param fieldWidthRelative
	 *            boolean
	 */
	public void setFieldWidthRelative(final boolean fieldWidthRelative) {
		this.fieldWidthRelative = fieldWidthRelative;
	}

	/**
	 * @return boolean
	 */
	public boolean getFieldWidthRelative() {
		return fieldWidthRelative;
	}

	/**
	 * @param fieldHeightRelative
	 *            int
	 */
	public void setFieldHeightRelative(final boolean fieldHeightRelative) {
		this.fieldHeightRelative = fieldHeightRelative;
	}

	/**
	 * @return boolean
	 */
	public boolean getFieldHeightRelative() {
		return fieldHeightRelative;
	}

	/**
	 * @param fieldName
	 *            String
	 */
	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return String
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldAlpha
	 *            float
	 */
	public void setFieldAlpha(final float fieldAlpha) {
		this.fieldAlpha = fieldAlpha;
	}

	/**
	 * @return float
	 */
	public float getFieldAlpha() {
		return fieldAlpha;
	}

	/**
	 * @param maxPlayers
	 *            int
	 */
	public void setMaxPlayers(final int maxPlayers) {
		if (maxPlayers < 1) {
			this.maxPlayers = 1;
		} else if (maxPlayers > 4) {
			this.maxPlayers = 4;
		} else {
			this.maxPlayers = maxPlayers;
		}

		players = new ArrayList<>();
	}

	/**
	 * @return int
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}

	/**
	 * @return List<Player>
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @param playerId
	 *            int
	 * @return Player
	 */
	public Player getPlayer(final int playerId) {
		return players.get(playerId);
	}

	/**
	 * @param gameGoal
	 *            int
	 */
	public void setGameGoal(final GameGoal gameGoal) {
		this.gameGoal = gameGoal;
		if (gameGoal == GameGoal.LAST_MAN_STANDING) {
			setWhileFree = false;
		} else if (gameGoal == GameGoal.DIVIDE_ET_IMPERA) {
			setWhileFree = true;
		} else if (gameGoal == GameGoal.DOMINATION) {
			setWhileFree = true;
		}
	}

	/**
	 * @return int
	 */
	public GameGoal getGameGoal() {
		return gameGoal;
	}

	/**
	 * @param setWhileFree
	 *            boolean
	 */
	public void setSetWhileFree(final boolean setWhileFree) {
		this.setWhileFree = setWhileFree;
	}

	/**
	 * @return boolean
	 */
	public boolean isSetWhileFree() {
		return setWhileFree;
	}

	/**
	 * @param borderless
	 *            boolean
	 */
	public void setBorderless(final boolean borderless) {
		this.borderless = borderless;
	}

	/**
	 * @return boolean
	 */
	public boolean isBorderless() {
		return borderless;
	}

	/**
	 * @param sticky
	 *            boolean
	 */
	public void setSticky(final boolean sticky) {
		this.sticky = sticky;
	}

	/**
	 * @return boolean
	 */
	public boolean isSticky() {
		return sticky;
	}

	/**
	 * @param overloadOnEqual
	 *            boolean
	 */
	public void setOverloadOnEqual(final boolean overloadOnEqual) {
		this.overloadOnEqual = overloadOnEqual;
	}

	/**
	 * @return boolean
	 */
	public boolean isOverloadOnEqual() {
		return overloadOnEqual;
	}

	/**
	 * @param emptyOverloaded
	 *            boolean
	 */
	public void setEmptyOverloaded(final boolean emptyOverloaded) {
		this.emptyOverloaded = emptyOverloaded;
	}

	/**
	 * @return boolean
	 */
	public boolean isEmptyOverloaded() {
		return emptyOverloaded;
	}

	/**
	 * @param looseOverloaded
	 *            boolean
	 */
	public void setLooseOverloaded(final boolean looseOverloaded) {
		this.looseOverloaded = looseOverloaded;
	}

	/**
	 * @return boolean
	 */
	public boolean isLooseOverloaded() {
		return looseOverloaded;
	}

	/**
	 * @param network
	 *            int
	 */
	public void setNetwork(final int network) {
		this.network = network;
	}

	/**
	 * @return int
	 */
	public int getNetwork() {
		return network;
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
	 * @param boardBgImage
	 *            String
	 */
	public void setBoardBgImageName(final String boardBgImage) {
		boardBgImageName = boardBgImage;
	}

	/**
	 * @return String
	 */
	public String getBoardBgImageName() {
		return boardBgImageName;
	}

	/**
	 * @param boardBgImageTiled
	 *            boolean
	 */
	public void setBoardBgImageTiled(final boolean boardBgImageTiled) {
		this.boardBgImageTiled = boardBgImageTiled;
	}

	/**
	 * @return boolean
	 */
	public boolean isBoardBgImageTiled() {
		return boardBgImageTiled;
	}

	/**
	 * @param maxOverload
	 *            int
	 */
	public void setMaxOverload(final int maxOverload) {
		this.maxOverload = maxOverload;
	}

	/**
	 * @return int
	 */
	public int getMaxOverload() {
		return maxOverload;
	}

	/**
	 * @return Element
	 */
	public Element getData() {
		final Element gameOptions = new Element("options");

		final Element boardoptions = new Element("boardoptions");
		gameOptions.addContent(boardoptions);
		boardoptions.setAttribute("fieldName", fieldName);
		boardoptions.setAttribute("numFieldsWidth", Integer.toString(numFieldsWidth));
		boardoptions.setAttribute("numFieldsHeight", Integer.toString(numFieldsHeight));
		boardoptions.setAttribute("fieldWidth", Integer.toString(fieldWidth));
		boardoptions.setAttribute("fieldHeight", Integer.toString(fieldHeight));
		boardoptions.setAttribute("fieldWidthRelative", Boolean.toString(fieldWidthRelative));
		boardoptions.setAttribute("fieldHeightRelative", Boolean.toString(fieldHeightRelative));
		boardoptions.setAttribute("fieldAlpha", Float.toString(fieldAlpha));
		boardoptions.setAttribute("sticky", Boolean.toString(sticky));
		boardoptions.setAttribute("maxOverload", Integer.toString(maxOverload));
		boardoptions.setAttribute("boardBgImageName", boardBgImageName);
		boardoptions.setAttribute("boardBgImageTiled", Boolean.toString(boardBgImageTiled));

		final Element playeroptions = new Element("playeroptions");
		gameOptions.addContent(playeroptions);
		playeroptions.setAttribute("maxPlayers", Integer.toString(maxPlayers));
		playeroptions.setAttribute("network", Integer.toString(network));
		playeroptions.setAttribute("host", host);
		playeroptions.setAttribute("port", Integer.toString(port));
		final ArrayList<Element> playerDatas = new ArrayList<>();
		for (int i = 0; i < maxPlayers; i++) {
			playerDatas.add(players.get(i).getData());
		}
		playeroptions.setContent(playerDatas);

		final Element gameoptions = new Element("gameoptions");
		gameOptions.addContent(gameoptions);
		gameoptions.setAttribute("gameGoal", gameGoal.toString());
		gameoptions.setAttribute("setWhileFree", Boolean.toString(setWhileFree));
		gameoptions.setAttribute("borderless", Boolean.toString(borderless));
		gameoptions.setAttribute("overloadOnEqual", Boolean.toString(overloadOnEqual));
		gameoptions.setAttribute("emptyOverloaded", Boolean.toString(emptyOverloaded));
		gameoptions.setAttribute("looseOverloaded", Boolean.toString(looseOverloaded));

		return gameOptions;
	}

	/**
	 * @param data
	 *            Element
	 */
	public void setData(final Element data) {
		final Element boardoptions = data.getChild("boardoptions");
		fieldName = boardoptions.getAttributeValue("fieldName");
		numFieldsWidth = Integer.parseInt(boardoptions.getAttributeValue("numFieldsWidth"));
		numFieldsHeight = Integer.parseInt(boardoptions.getAttributeValue("numFieldsHeight"));
		fieldWidth = Integer.parseInt(boardoptions.getAttributeValue("fieldWidth"));
		fieldHeight = Integer.parseInt(boardoptions.getAttributeValue("fieldHeight"));
		fieldWidthRelative = Boolean.parseBoolean(boardoptions.getAttributeValue("fieldWidthRelative"));
		fieldHeightRelative = Boolean.parseBoolean(boardoptions.getAttributeValue("fieldHeightRelative"));
		fieldAlpha = Float.parseFloat(boardoptions.getAttributeValue("fieldAlpha"));
		sticky = Boolean.parseBoolean(boardoptions.getAttributeValue("sticky"));
		maxOverload = Integer.parseInt(boardoptions.getAttributeValue("maxOverload"));
		boardBgImageName = boardoptions.getAttributeValue("boardBgImageName");
		boardBgImageTiled = Boolean.parseBoolean(boardoptions.getAttributeValue("boardBgImageTiled"));

		final Element playeroptions = data.getChild("playeroptions");
		maxPlayers = Integer.parseInt(playeroptions.getAttributeValue("maxPlayers"));
		network = Integer.parseInt(playeroptions.getAttributeValue("network"));
		host = playeroptions.getAttributeValue("host");
		port = Integer.parseInt(playeroptions.getAttributeValue("port"));
		players = new ArrayList<>();
		for (final Object elPlayer : playeroptions.getChildren("player")) {
			players.add(new Player((Element) elPlayer));
		}

		final Element gameoptions = data.getChild("gameoptions");
		gameGoal = GameGoal.valueOf(gameoptions.getAttributeValue("gameGoal"));
		setWhileFree = Boolean.parseBoolean(gameoptions.getAttributeValue("setWhileFree"));
		borderless = Boolean.parseBoolean(gameoptions.getAttributeValue("borderless"));
		overloadOnEqual = Boolean.parseBoolean(gameoptions.getAttributeValue("overloadOnEqual"));
		emptyOverloaded = Boolean.parseBoolean(gameoptions.getAttributeValue("emptyOverloaded"));
		looseOverloaded = Boolean.parseBoolean(gameoptions.getAttributeValue("looseOverloaded"));
	}
}