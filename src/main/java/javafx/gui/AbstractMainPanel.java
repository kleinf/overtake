package javafx.gui;

import javafx.scene.layout.Pane;

import java.awt.event.MouseEvent;

import util.ModeEnum;

/**
 * @author Administrator
 * 
 */
public abstract class AbstractMainPanel extends Pane {

	protected AbstractMainPanel(GameApplication parentFrame, String xmlData) {
		super();
	}

	protected void initialize() {
	}

	protected void boardClick(MouseEvent mouseEvent) {
	}

	protected boolean checkClick(int idX, int idY, boolean override) {
		return false;
	}

	protected void setMode(ModeEnum mode) {
	}

	protected String getXmlData() {
		return null;
	}

	protected void saveGame(String filename) {
	}
}