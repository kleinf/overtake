package swing.gui;

import java.awt.event.MouseEvent;

import game.GameSession;

/**
 * @author Administrator
 * 
 */
public class EditorPanel extends AbstractMainPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param parentFrame
	 *            GameFrame
	 * @param xmlData
	 *            String
	 */
	protected EditorPanel(final GameFrame parentFrame, final String xmlData) {

		super(parentFrame, xmlData);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see swing.gui.AbstractMainPanel#initialize()
	 */
	@Override
	protected void initialize() {
		setName("EditorPanel");

		add(getBoardPanel(), "Center");
		setSize(getBoardPanel().getWidth(), getBoardPanel().getHeight());
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
		if (mouseEvent.getButton() == MouseEvent.BUTTON1 && mouseEvent.isShiftDown()) {
			// Linke Maustaste + SHIFT = Wert aendern
			if (getFieldComponent(idX, idY).getValue() < getFieldComponent(idX, idY).getMaxValue()) {
				getFieldComponent(idX, idY).setValue(getFieldComponent(idX, idY).getValue() + 1);
			} else {
				getFieldComponent(idX, idY).setValue(0);
			}
		} else if (mouseEvent.getButton() == MouseEvent.BUTTON1 && mouseEvent.isControlDown()) {
			// Linke Maustaste + STRG = Besitzer aendern
			if (getFieldComponent(idX, idY).getOwnerId() < GameSession.gameOptions.getMaxPlayers() - 1) {
				getFieldComponent(idX, idY).setOwner(getFieldComponent(idX, idY).getOwnerId() + 1);
			} else {
				getFieldComponent(idX, idY).setOwner(-1);
			}
		} else if (mouseEvent.getButton() == MouseEvent.BUTTON1 && mouseEvent.isAltDown()) {
			// Linke Maustaste + ALT = Reset
			getFieldComponent(idX, idY).setValue(0);
			getFieldComponent(idX, idY).setOwner(-1);
			getFieldComponent(idX, idY).setEnabled(true);
		} else if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
			// Linke Maustaste = Aktivieren/Deaktivieren
			if (getFieldComponent(idX, idY).isEnabled()) {
				getFieldComponent(idX, idY).setEnabled(false);
			} else {
				getFieldComponent(idX, idY).setEnabled(true);
			}
		}
		repaint();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see swing.gui.AbstractMainPanel#checkClick(int, int, boolean)
	 */
	@Override
	protected boolean checkClick(final int idX, final int idY, final boolean override) {
		return true;
	}
}