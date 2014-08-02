package game;

import swing.gui.BoardPanel;
import swing.gui.FieldComponent;

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