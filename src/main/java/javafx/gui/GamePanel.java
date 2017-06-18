package javafx.gui;

import game.Player;
import swing.gui.ChatPanel;

/**
 * @author Administrator
 * 
 */
public class GamePanel extends AbstractMainPanel {

	protected GamePanel(GameApplication parentFrame, String xmlData) {
		super(parentFrame, xmlData);
	}

	protected ChatPanel getChatPanel() {
		return null;
	}

	protected void activatePlayer(int playerId, String playerName, int playerColor) {
	}

	protected void deactivatePlayer(int playerId) {
	}

	protected void start() {
	}

	protected void stop() {
	}

	protected void netBoardClick(int idX, int idY) {
	}

	protected void setNetMode(String mode) {
	}

	public synchronized void run() {
	}

	protected Player getPlayer(int playerId) {
		return null;
	}

	protected boolean isMyTurn() {
		return false;
	}

}