import game.AbstractComputer;
import game.GameSession;
import game.Player;
import gui.swing.FieldComponent;

/**
 * @author Administrator
 * 
 */
public class SprengmeisterCPU extends AbstractComputer {

	/**
	 * {@inheritDoc}
	 * 
	 * @see game.AbstractComputer#computerClick(game.Player)
	 */
	@Override
	public FieldComponent computerClick(final Player currPlayer) {
		FieldComponent checkField = null;
		FieldComponent bestField = null;
		int maxG = 999;
		for (int idY = 0; idY < GameSession.gameOptions.getNumFieldsHeight(); idY++) {
			for (int idX = 0; idX < GameSession.gameOptions.getNumFieldsWidth(); idX++) {
				checkField = getBoardPanel().getFieldComponent(idX, idY);
				if (checkField.isAllowedClick(true)) {
					if (checkField.getOwnerId() == currPlayer.getPlayerId()) {
						if (checkField.getValue() + 1 == checkField
								.getMaxValue()) {
							int value = 0;
							for (final FieldComponent relation : getBoardPanel()
									.getActiveRelations(checkField.getIdX(),
											checkField.getIdY())) {
								value += relation.getMaxValue()
										- relation.getValue()
										* (1 - 2 * relation.getOwnerId() != -1
												&& relation.getOwnerId() != currPlayer
														.getPlayerId() ? -1 : 0);
							}
							if (value < maxG || value == maxG
									&& Math.floor(Math.random() * 2.0D) == 1.0D) {
								maxG = value;
								bestField = checkField;
							}
						}
					}
					if (checkField.getOwnerId() == -1
							|| checkField.getOwnerId() == currPlayer
									.getPlayerId()) {
						if (maxG == 999) {
							boolean abbruch = false;
							for (int k = 0; k < getBoardPanel()
									.getActiveRelations(checkField.getIdX(),
											checkField.getIdY()).size(); k++) {
								if (getBoardPanel()
										.getActiveRelations(
												checkField.getIdX(),
												checkField.getIdY()).get(k)
										.getOwnerId() == currPlayer
										.getPlayerId()) {
									if (Math.floor(Math.random() * 2) == 1) {
										bestField = checkField;
										abbruch = true;
									}
								}
							}
							if (!abbruch
									&& bestField == null
									|| Math.floor(Math.random() * 10.0D) == 0.0D) {
								bestField = checkField;
							}
						}
					}
				}
			}
		}
		return bestField;
	}
}