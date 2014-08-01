package game;

import gui.swing.BoardPanel;
import gui.swing.FieldComponent;

/**
 * @author Administrator
 * 
 *         This is the base to build an AI.
 */
public abstract class AbstractComputer {
	private BoardPanel boardPanel;

	/**
	 * @param boardPanel
	 *            BoardPanel
	 */
	public final void init(final BoardPanel boardPanel) {
		this.boardPanel = boardPanel;
	}

	/**
	 * @param currPlayer
	 *            Player
	 * @return FieldComponent
	 */
	public abstract FieldComponent computerClick(Player currPlayer);

	/**
	 * @return BoardPanel
	 */
	protected final BoardPanel getBoardPanel() {
		return boardPanel;
	}
}