package gui;

import game.GameSession;
import game.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import util.AnimatedImageUtil;
import util.Logger;
import util.Mode;

/**
 * @author Administrator
 * 
 */
public abstract class AbstractMainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final GameFrame parentFrame;
	private BoardPanel boardPanel;
	private int currPlayerNumber;
	private int currRound;
	private Mode mode;

	/**
	 * Constructor.
	 * 
	 * @param parentFrame
	 *            GameFrame
	 * @param xmlData
	 *            String
	 */
	protected AbstractMainPanel(final GameFrame parentFrame,
			final String xmlData) {
		super();
		currPlayerNumber = 0;
		currRound = 1;
		this.parentFrame = parentFrame;
		if (xmlData == null) {
			boardPanel = new BoardPanel(this, null);
		} else {
			StringReader reader = null;
			try {
				reader = new StringReader(xmlData);
				final SAXBuilder builder = new SAXBuilder();
				final Document doc = builder.build(reader);
				final Element elGame = doc.getRootElement();

				currPlayerNumber = Integer.parseInt(elGame
						.getAttributeValue("currPlayerNumber"));

				currRound = Integer.parseInt(elGame
						.getAttributeValue("currRound"));

				GameSession.gameOptions.setData(elGame.getChild("options"));

				// Process images
				for (final Player player : GameSession.gameOptions.getPlayers()) {
					player.setPlayerImage(AnimatedImageUtil.createMyImage(
							player.getPlayerImageName(),
							new Color(player.getPlayerColor())));
				}

				// Informationen zusammenfuegen
				boardPanel = new BoardPanel(this, elGame.getChild("board"));

			} catch (final JDOMException exception) {
				Logger.getInstance().log(exception.getMessage());
			} catch (final IOException exception) {
				Logger.getInstance().log(exception.getMessage());
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		}
		setLayout(new BorderLayout());
		initialize();
		setPreferredSize(getSize());
		SwingUtilities.updateComponentTreeUI(parentFrame);
	}

	/**
	 * Initialize the class.
	 */
	protected abstract void initialize();

	/**
	 * @param filename
	 *            String
	 */
	protected final void saveGame(final String filename) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(filename);
			writer.write(getXmlData());
			writer.flush();
		} catch (final IOException exception) {
			Logger.getInstance().log(exception.getMessage());
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (final IOException exception) {
				Logger.getInstance().log(exception.getMessage());
			}
		}
	}

	/**
	 * @param mouseEvent
	 *            MouseEvent
	 */
	protected abstract void boardClick(final MouseEvent mouseEvent);

	/**
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param override
	 *            boolean
	 * @return boolean
	 */
	protected abstract boolean checkClick(final int idX, final int idY,
			final boolean override);

	/**
	 * @return GameFrame
	 */
	protected final GameFrame getParentFrame() {
		return parentFrame;
	}

	/**
	 * @return BoardPanel
	 */
	protected final BoardPanel getBoardPanel() {
		return boardPanel;
	}

	/**
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @return BoardPanel
	 */
	protected final FieldComponent getFieldComponent(final int idX,
			final int idY) {
		return boardPanel.getFieldComponent(idX, idY);
	}

	/**
	 * @return int
	 */
	protected final int getCurrPlayerNumber() {
		return currPlayerNumber;
	}

	/**
	 * Wechselt zum naechsten Spieler. Wenn alle Spieler an der Reihe waren,
	 * wird die Runde um eins hochgezaehlt. Wenn ein Rundenwechsel erfolgt ist,
	 * wird true zurueckgegeben. Ansonsten false.
	 * 
	 * @return boolean
	 */
	protected final boolean nextPlayer() {
		boolean changeRound = false;
		do {
			// Naechster Spieler (inaktive Spieler werden uebersprungen)
			currPlayerNumber++;
			if (currPlayerNumber >= GameSession.gameOptions.getPlayers().size()) {
				currPlayerNumber = 0;
				currRound++;
				changeRound = true;
			}
		} while (!getCurrPlayer().isActive());
		return changeRound;
	}

	/**
	 * @return Player
	 */
	protected final Player getCurrPlayer() {
		return GameSession.gameOptions.getPlayer(currPlayerNumber);
	}

	/**
	 * @return int
	 */
	protected final int getCurrRound() {
		return currRound;
	}

	/**
	 * @param mode
	 *            Mode
	 */
	protected void setMode(final Mode mode) {
		this.mode = mode;
	}

	/**
	 * @return boolean
	 */
	protected final boolean isEditMode() {
		return mode == Mode.MODE_EDIT;
	}

	/**
	 * @return boolean
	 */
	protected final boolean isPlayMode() {
		return mode == Mode.MODE_PLAY;
	}

	/**
	 * @return boolean
	 */
	protected final boolean isRepairMode() {
		return mode == Mode.MODE_REPAIR;
	}

	/**
	 * @return String
	 */
	protected final String getXmlData() {
		StringWriter writer = null;
		try {
			final Document doc = new Document();
			final Element root = new Element("game");
			doc.setRootElement(root);
			root.setAttribute("currPlayerNumber",
					Integer.toString(currPlayerNumber));

			root.setAttribute("currRound", Integer.toString(currRound));
			root.addContent(GameSession.gameOptions.getData());
			root.addContent(getBoardPanel().getData());

			final XMLOutputter serializer = new XMLOutputter();
			writer = new StringWriter();
			serializer.output(doc, writer);
			writer.flush();
			final String str = writer.getBuffer().toString();

			return str;
		} catch (final IOException exception) {
			Logger.getInstance().log(exception.getMessage());
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (final IOException exception) {
				Logger.getInstance().log(exception.getMessage());
			}
		}
		return null;
	}
}