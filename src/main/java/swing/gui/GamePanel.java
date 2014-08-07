package swing.gui;

import game.AbstractComputer;
import game.GameGoal;
import game.GameSession;
import game.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JSplitPane;

import net.Net;
import swing.util.AnimatedImageUtil;
import swing.util.FontCreator;
import util.Constants;
import util.ModeEnum;
import util.PseudoLogger;

/**
 * @author Administrator
 * 
 */
public class GamePanel extends AbstractMainPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private final AbstractComputer[] abstractComputers;
	private JSplitPane spMain;
	private JSplitPane spInfo;
	private InfoPanel infoPanel;
	private ChatPanel chatPanel;
	private JLabel status;
	private Thread gameThread;
	private boolean playerTurn = false;
	private boolean turnCompleted = false;
	private int releasePlayerId = -1;
	private Player winner;

	/**
	 * Constructor.
	 * 
	 * @param parentFrame
	 *            GameFrame
	 * @param xmlData
	 *            String
	 */
	protected GamePanel(final GameFrame parentFrame, final String xmlData) {

		super(parentFrame, xmlData);

		getBoardPanel().recalculate();
		infoPanel.refresh();
		abstractComputers = new AbstractComputer[GameSession.gameOptions
				.getMaxPlayers()];
		try {
			// Anzahl potentiell aktiver Spieler setzen
			for (int playerId = 0; playerId < GameSession.gameOptions
					.getMaxPlayers(); playerId++) {
				if (getPlayer(playerId).isComputer()) {
					// Per Reflection die Computer-AI laden
					final AbstractComputer abstractComputer = (AbstractComputer) Class
							.forName(getPlayer(playerId).getPlayerName())
							.newInstance();

					abstractComputer.init(getBoardPanel());
					abstractComputers[playerId] = abstractComputer;
				}
				getPlayer(playerId).setNumFields(
						getBoardPanel().getSumFieldsPlayer(playerId));
			}
		} catch (final ClassNotFoundException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
		} catch (final InstantiationException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
		} catch (final IllegalAccessException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see swing.gui.AbstractMainPanel#initialize()
	 */
	@Override
	protected void initialize() {
		setName("GamePanel");

		spMain = new JSplitPane();
		add(spMain, BorderLayout.CENTER);
		getBoardPanel().setEnabled(true);
		spMain.setLeftComponent(getBoardPanel());
		spMain.setDividerLocation(getBoardPanel().getWidth() + 1);

		infoPanel = new InfoPanel(this, 250, getBoardPanel().getHeight());
		// Bei einem Netzwerkspiel wird im rechten Fensterbereich
		// zusaetzlich noch ein kleiner Chat-Dialog angeboten.
		if (GameSession.gameOptions.getNetwork() > 0) {
			spInfo = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			spInfo.setDividerLocation(200);
			spMain.setRightComponent(spInfo);
			chatPanel = new ChatPanel(getParentFrame());
			spInfo.setTopComponent(infoPanel);
			spInfo.setBottomComponent(chatPanel);
		} else {
			spMain.setRightComponent(infoPanel);
		}
		spMain.setSize(getBoardPanel().getWidth() + infoPanel.getWidth() + 1,
				getBoardPanel().getHeight() + 2);
		spMain.setPreferredSize(spMain.getSize());

		status = new JLabel();
		add(status, BorderLayout.SOUTH);
		status.setFont(FontCreator.createFont("fonts/arial.ttf", Font.BOLD,
				12.0F));
		status.setAlignmentX(Component.CENTER_ALIGNMENT);
		status.setSize(spMain.getWidth(), 20);
		status.setPreferredSize(status.getSize());
		setSize(spMain.getWidth(), spMain.getHeight() + status.getHeight());
		setPreferredSize(getSize());
	}

	/**
	 * @return ChatPanel
	 */
	protected ChatPanel getChatPanel() {
		return chatPanel;
	}

	/**
	 * @param playerId
	 *            int
	 * @param playerName
	 *            String
	 * @param playerColor
	 *            int
	 */
	protected void activatePlayer(final int playerId, final String playerName,
			final int playerColor) {
		final Player player = getPlayer(playerId);
		player.setActive(true);
		player.setPlayerName(playerName);
		player.setPlayerColor(playerColor);
		player.setPlayerImage(AnimatedImageUtil.createMyImage(
				player.getPlayerImageName(), new Color(player.getPlayerColor())));
		if (playerId == getParentFrame().getEigeneId()) {
			chatPanel.getJComboBox().addItem(
					new Player(-1, "Alle", Color.WHITE.getRGB(), false));
		} else {
			chatPanel.getJComboBox().addItem(player);
		}
		infoPanel.refreshNetPlayer(playerId);
	}

	/**
	 * @param playerId
	 *            int
	 */
	protected void deactivatePlayer(final int playerId) {
		getPlayer(playerId).setActive(false);
		releasePlayerId = playerId;
		// Zug des abemeldeten Spielers beenden
		if (releasePlayerId == getCurrPlayerNumber()) {
			completeTurn();
		} else {
			turnCompleted = true;
		}
		chatPanel.getJComboBox().removeItem(getPlayer(playerId));
		infoPanel.refreshNetPlayer(playerId);
	}

	/**
	 * 
	 */
	protected void start() {
		gameThread = new Thread(this, "Game-Thread");
		gameThread.setDaemon(true);
		gameThread.start();
	}

	/**
	 * 
	 */
	protected void stop() {
		if (gameThread != null) {
			gameThread.interrupt();
		}
		gameThread = null;
		getBoardPanel().dispose();
		infoPanel = null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see swing.gui.AbstractMainPanel#boardClick(java.awt.event.MouseEvent)
	 */
	@Override
	protected void boardClick(final MouseEvent mouseEvent) {
		final int idX = ((FieldComponent) mouseEvent.getComponent()).getIdX();
		final int idY = ((FieldComponent) mouseEvent.getComponent()).getIdY();
		if (playerTurn && checkClick(idX, idY, false)) {
			if (GameSession.gameOptions.getNetwork() < 1) {
				doClick(idX, idY);
			} else {
				// Mitspieler im Netz ueber den Zug benachrichtigen
				getParentFrame().send(
						Constants.NET_ALL,
						Net.ZUG.name() + Constants.NET_DIVIDER + idX
								+ Constants.NET_DIVIDER + idY);
			}
		}
	}

	/**
	 * Den Zug eines Computergegners ausfuehren.
	 */
	private void computerClick() {
		final FieldComponent bestField = abstractComputers[getCurrPlayerNumber()]
				.computerClick(getCurrPlayer());

		doClick(bestField.getIdX(), bestField.getIdY());
	}

	/**
	 * Zug eines Mitspielers aus dem Netz ausfuehren.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 */
	protected void netBoardClick(final int idX, final int idY) {
		if (checkClick(idX, idY, true)) {
			doClick(idX, idY);
		}
	}

	/**
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @see swing.gui.GamePanel#takeOver(int, int)
	 */
	private void doClick(final int idX, final int idY) {
		if (isPlayMode()) {
			// Alle Feld-Ueberladungen durchfuehren
			takeOver(idX, idY);

			// Spielzug abschliessen
			completeTurn();
		} else if (isRepairMode()) {
			final FieldComponent currField = getFieldComponent(idX, idY);
			if (getCurrPlayer().getRepairPoints() > 0 && currField.isEnabled()
					&& currField.getOverloads() > 0) {
				currField.subOverload();
				getCurrPlayer().subRepairPoints();
			}
			if (getCurrPlayer().getRepairPoints() == 0) {
				setMode(ModeEnum.MODE_PLAY);
				// Info-Panel aktualisieren, um den Repair-Button auszublenden
				infoPanel.refresh();
			}

			// Flag fuer Reparaturerlaubnis zuruecksetzen
			currField.resetAllowedRepair();

			// Feld nach Reparatur ueber Netzwerk neu zeichnen
			currField.repaint();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see swing.gui.AbstractMainPanel#setMode(util.ModeEnum)
	 */
	@Override
	protected void setMode(final ModeEnum mode) {
		if (GameSession.gameOptions.getNetwork() < 1) {
			super.setMode(mode);
		} else {
			// Mitspieler im Netz ueber den Moduswechsel benachrichtigen
			getParentFrame().send(Constants.NET_ALL,
					Net.MODE.name() + Constants.NET_DIVIDER + mode.name());
		}
	}

	/**
	 * @param mode
	 *            String
	 */
	protected void setNetMode(final String mode) {
		super.setMode(ModeEnum.valueOf(mode));
	}

	/**
	 * @see swing.gui.GamePanel#checkWinner()
	 * @see swing.gui.GamePanel#nextPlayer()
	 */
	private void completeTurn() {
		// Auf Gewinner pruefen
		checkWinner();

		// Wenn kein Gewinner fest steht, zum naechsten Spieler wechseln und
		// auf Rundenwechsel checken (d.h. alle Spieler waren an der Reihe).
		if (winner == null && nextPlayer()) {
			// Nach dem Rundenwechsel werden die Rundencounter
			// aktualisiert und evtl. Felder repariert
			if (GameSession.gameOptions.getMaxOverload() > 0) {
				for (int idY = 0; idY < GameSession.gameOptions
						.getNumFieldsHeight(); idY++) {
					for (int idX = 0; idX < GameSession.gameOptions
							.getNumFieldsWidth(); idX++) {
						final FieldComponent field = getFieldComponent(idX, idY);
						// Es werden nur Felder beruecksichtigt, die in
						// Besitz von Spielern sind.
						if (field.isEnabled() && field.getOwnerId() != -1) {
							field.addRound();
							// Fuer 10 Runden in denen ein Feld durchgehend
							// im Besitz eines Spielers ist, wird es zur
							// Belohnung um eine Stufe repariert, falls es
							// beschaedigt sein sollte.
							if (field.getOverloads() > 0
									&& field.getRounds() % 10 == 0) {
								field.subOverload();
							}
						}
					}
				}
			}
		}
		turnCompleted = true;
	}

	/**
	 * Pruefen, ob sich alle Netzwerkspieler angemeldet haben.
	 * 
	 * @return boolean
	 */
	private boolean isAllPlayersActive() {
		boolean retVal = true;
		for (final Player player : GameSession.gameOptions.getPlayers()) {
			if (!player.isActive()) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public synchronized void run() {
		if (GameSession.gameOptions.getNetwork() > 0 && !isAllPlayersActive()) {
			// Netzwerkspiel aber nicht alle Spieler aktiv
			playerTurn = false;
			status.setText("Nicht alle Spieler aktiv");
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			try {
				// Auf Spieler warten
				while (!isAllPlayersActive()) {
					Thread.sleep(100L);
				}
			} catch (final InterruptedException exception) {
				return;
			}
		}
		while (winner == null) {
			turnCompleted = false;
			if (getCurrPlayer().isComputer()) {
				playerTurn = false;
				status.setText("Der Computer ist am Zug");
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				try {
					// "Think-Time"
					Thread.sleep(500L);
				} catch (final InterruptedException exception) {
					return;
				}
				computerClick();
			} else if (GameSession.gameOptions.getNetwork() < 1) {
				// Wenn es kein Netzwerkspiel ist, darf der naechste Spieler
				// klicken
				playerTurn = true;
				status.setText(getCurrPlayer().getPlayerName() + " ist am Zug");
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			} else if (releasePlayerId > -1) {
				// Netzwerkspieler hat sich abgemeldet. Felder freigeben.
				getBoardPanel().releasePlayer(releasePlayerId);
				releasePlayerId = -1;
				turnCompleted = true;
			} else if (getCurrPlayer().getPlayerId() == getParentFrame()
					.getEigeneId()) {
				// Netzwerkspiel und man selber ist dran
				playerTurn = true;
				status.setText("Du bist am Zug");
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			} else {
				// Netzwerkspiel und ein anderer Spieler ist dran
				playerTurn = false;
				status.setText("Spieler " + getCurrPlayer().getPlayerName()
						+ " ist am Zug");
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
			}
			// Info-Panel aktualisieren, um ggf. den Repair-Button zu aktivieren
			infoPanel.refresh();
			try {
				// Auf Spielzug warten
				while (!turnCompleted) {
					Thread.sleep(50L);
				}
			} catch (final InterruptedException exception) {
				return;
			}
			spMain.repaint();
			infoPanel.refresh();
		}
		// Wenn niemand mehr aktiv ist und es keinen Gewinner gibt, kommt es zu
		// einem Untentschieden
		getBoardPanel().setEnabled(false);
		status.setText(winner.getPlayerName() + " WINS!!!");
		getParent().repaint();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see swing.gui.AbstractMainPanel#checkClick(int, int, boolean)
	 */
	@Override
	protected boolean checkClick(final int idX, final int idY,
			final boolean override) {
		if (!playerTurn && !override) {
			// Das playerTurn-Flag verhindert, dass menschliche Spieler einen
			// Zug machen, wenn der Computergegner oder ein Netzwerkspieler an
			// der Reihe ist. Damit der Computer oder ein Netzwerkspieler
			// trotzdem ihren Zug durchfuehren koennen, wird bei der Pruefung
			// der Spielzuege dieser beiden das Override-Flag als true
			// uebergeben. Somit kann die Pruefung durchgefuehrt werden.
			return false;
		}

		if (isRepairMode()) {
			// Im Reparatur-Modus duerfen alle Felder angeklickt werden,
			// die mindestens einen Strukturpunkt verloren haben.
			return getFieldComponent(idX, idY).getOverloads() > 0;
		}

		if (!getFieldComponent(idX, idY).isEnabled()) {
			// Nicht aktivierte Felder duerfen nie angeklickt werden
			return false;
		}

		if (getFieldComponent(idX, idY).getOwnerId() == getCurrPlayerNumber()) {
			// Eigene Felder duerfen immer angeklickt werden
			return true;
		} else if (getFieldComponent(idX, idY).getOwnerId() != -1) {
			// Von Mitspielern belegte Felder duerfen nie angeklickt werden
			return false;
		}

		if (getCurrRound() <= 1) {
			// In der ersten Runde darf frei gesetzt werden
			return true;
		}

		// Prinzipielle Erlaubnis, wenn das Feld frei ist
		boolean allowed = getFieldComponent(idX, idY).getOwnerId() == -1;

		// "sticky" bedeutet, dass nur angrenzend an eigene Felder angelegt
		// werden darf. Allerdings, wenn die Option "set while free" aktiv
		// ist und der Spieler keine Felder mehr hat, darf er wieder setzen
		// wohin er will. (Vorausgesetzt, es ist noch mind. ein Feld frei.)
		if (GameSession.gameOptions.isSticky()
				&& !(GameSession.gameOptions.isSetWhileFree() && getCurrPlayer()
						.getNumFields() == 0)) {
			boolean neighbour = false;
			for (final FieldComponent relation : getBoardPanel()
					.getActiveRelations(idX, idY)) {
				if (relation.getOwnerId() == getCurrPlayerNumber()) {
					neighbour = true;
					break;
				}
			}
			// Erlaubnis, wenn prinzipiell gesetzt werden darf und ein eigenes
			// Feld angrenzt
			allowed = allowed && neighbour;
		}
		return allowed;
	}

	private void takeOver(final int idX, final int idY) {
		final HashMap<String, FieldComponent> currChecklist = new HashMap<>();
		final HashMap<String, FieldComponent> newChecklist = new HashMap<>();
		// Zaehler fuer Kettenreaktionen zuruecksetzen
		final List<String> overloadCounter = new ArrayList<>();
		int overloads = 0;

		FieldComponent currField = getFieldComponent(idX, idY);
		// Neuen Besitzer eintragen
		currField.setOwner(getCurrPlayerNumber());
		// Wert um eins erhoehen
		currField.addValue();
		// Feld in Pruefliste eintragen
		currChecklist.put(currField.getIdX() + "-" + currField.getIdY(),
				currField);

		Set<String> testSet = new HashSet<String>();
		do {
			overloads = 0;
			for (final Entry<String, FieldComponent> entry : currChecklist
					.entrySet()) {
				currField = entry.getValue();
				// Wenn die Maximalkapazitaet des Feldes ueberschritten
				// (erreicht) ist, kommt es zu einer Ueberladung und der Inhalt
				// des Feldes verteilt sich auf die benachbarten Felder.
				if (currField.getValue() > currField.getMaxValue()
						|| GameSession.gameOptions.isOverloadOnEqual()
						&& currField.getValue() == currField.getMaxValue()) {
					// Kettenreaktionen zaehlen.
					// Dabei wird ermittelt, wieviele Felder durch die
					// Ueberladung ebenfalls zum ueberladen gebracht werden.
					// Dies kann als Basis fuer eine Punktewertung dienen,
					// bei der lange Ketten von Ueberladungen Punkte geben.
					// Die Speicherung erfolgt rundenbasiert. D.h.: ID 0 = die
					// von Hand ausgeloeste Ueberladung ID 1 ist/sind die
					// Ueberladung(en), die durch die erste Ueberladung
					// ausgeloest wurden usw.
					overloads++;
					// Am Feld vermerken, dass es ueberladen wurde.
					currField.addOverload();

					// Wenn die entsprechende Option eingestellt ist, wird
					// das Feld komplett ausgeleert. Andernfalls wird nur
					// die Menge subtrahiert, die sich auch tatsaechlich auf
					// die umliegenden Felder verteilt hat.
					if (GameSession.gameOptions.isEmptyOverloaded()) {
						currField.setValue(0);
					} else {
						currField.setValue(currField.getValue()
								- currField.getMaxValue());
					}

					// Wenn die entsprechende Option eingestellt ist, wird
					// ein Feld das ueberladen wurde freigegeben. Ansonsten
					// wuerde der urspruengliche Besitzer es behalten.
					if (GameSession.gameOptions.isLooseOverloaded()) {
						currField.setOwner(-1);
						// Da das Feld niemandem mehr gehoert,
						// wird der Rundencounter zurueckgesetzt
						currField.setRounds(0);
					}

					// Bei Ueberladung werden die Nachbarfelder uebernommen.
					// Anschliessend werden auch diese geprueft, ob sie
					// ihrerseits ueberladen und dadurch wiederum andere
					// Felder uebernehmen.
					for (final FieldComponent relation : getBoardPanel()
							.getActiveRelations(currField.getIdX(),
									currField.getIdY())) {
						// Auf feindliche Uebernahme pruefen
						if (currField.getOwnerId() != getCurrPlayerNumber()) {
							// Wenn das Feld vom Gegner uebernommen wurde,
							// wird der Rundencounter zurueckgesetzt
							currField.setRounds(0);
						}
						// Waehrend die aktuelle Checkliste abgearbeitet
						// wird, wird schon die neue Liste befuellt.
						relation.setOwner(getCurrPlayerNumber());
						// Wert um eins erhoehen
						relation.addValue();
						// Feld in Pruefliste eintragen
						newChecklist.put(
								relation.getIdX() + "-" + relation.getIdY(),
								relation);
					}

					if (GameSession.gameOptions.getMaxOverload() > 0
							&& currField.getOverloads() >= GameSession.gameOptions
									.getMaxOverload()) {
						// Wenn Felder oefter explodieren, als es die
						// Stabilitaet zulaesst, brechen die Felder weg
						// und eine Verteilung auf umliegende Felder findet
						// nicht mehr statt.
						getBoardPanel().disableField(currField.getIdX(),
								currField.getIdY());
						// Ausserdem bekommen alle Mitspieler einen
						// Reparaturpunkt
						for (final Player player : GameSession.gameOptions
								.getPlayers()) {
							if (player.getPlayerId() != getCurrPlayerNumber()) {
								player.addRepairPoints();
							}
						}
					}
				}
			}
			// Wenn der Durchgang erfolgreich beendet ist, werden die
			// abhaengigen Felder geprueft. Anschliessend deren Abhaengigkeiten
			// usw.
			currChecklist.clear();
			currChecklist.putAll(newChecklist);
			newChecklist.clear();
			if (!currChecklist.isEmpty()) {
				overloadCounter.add(Integer.toString(overloads));
				// Wenn es weitere Ueberladungen gibt, Endlos-Schleife checken
				if (testSet.contains(getBoardPanel().toString())) {
					System.out.println("Endless!");
					break;
				}
				testSet.add(getBoardPanel().toString());
			}
		} while (!currChecklist.isEmpty());
	}

	private void checkWinner() {
		winner = null;

		// Neuberechnung der Feld-Counter
		getBoardPanel().recalculate();

		for (int i = 0; i < GameSession.gameOptions.getPlayers().size(); ++i) {
			getPlayer(i).setNumFields(getBoardPanel().getSumFieldsPlayer(i));
		}

		// Nach der ersten Runde wird gecheckt wer gewonnen hat
		if (getCurrRound() > 1 || getBoardPanel().getSumFieldsFree() == 0) {

			int activePlayers = 0;

			// Wenn ohne eigene Felder gesetzt werden darf und noch Felder
			// frei sind, duerfen alle mitspielen.
			if (GameSession.gameOptions.isSetWhileFree()
					&& getBoardPanel().getSumFieldsFree() > 0) {
				activePlayers = GameSession.gameOptions.getPlayers().size();
			} else {
				// Wenn ohne eigene Felder nicht gesetzt werden darf oder
				// keine Felder mehr frei sind, muss geprueft werden, wer
				// ausscheidet. Dabei kann es bereits einen Gewinner geben,
				// wenn nur ein Spieler noch aktiv ist.
				for (final Player player : GameSession.gameOptions.getPlayers()) {
					if (player.getNumFields() == 0) {
						player.setActive(false);
					} else {
						activePlayers++;
						winner = player;
					}
				}

				// Wenn ueberladene Felder zerstoert werden und beim letzten Zug
				// kein Spielfeld mehr uebrig bleibt, wird der Spieler, der den
				// letzten Zug gemacht hat, zum Gewinner erklaert.
				if (getBoardPanel().getSumFields() == 0) {
					winner = getCurrPlayer();
				}

				// Wenn mehr als ein Spieler aktiv ist, gibt es keinen Gewinner.
				if (activePlayers > 1) {
					winner = null;
				}
			}

			if (GameSession.gameOptions.getGameGoal() == GameGoal.DIVIDE_ET_IMPERA) {
				// Ein Spieler muss die prozentuale Uebermacht errungen haben
				final int siegAnteil = getBoardPanel().getSumFields()
						/ activePlayers;
				for (final Player player : GameSession.gameOptions.getPlayers()) {
					if (player.getNumFields() > siegAnteil) {
						winner = player;
						break;
					}
				}
			}
		}

		if (winner != null) {
			playerTurn = false;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * @param playerId
	 *            int
	 * @return Player
	 */
	protected Player getPlayer(final int playerId) {
		return GameSession.gameOptions.getPlayer(playerId);
	}

	/**
	 * @return boolean
	 */
	protected boolean isMyTurn() {
		return playerTurn;
	}
}