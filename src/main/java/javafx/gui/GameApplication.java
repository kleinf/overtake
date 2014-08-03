package javafx.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.Constants;
import util.ModeEnum;

/**
 * @author Administrator
 * 
 */
public class GameApplication extends Application {

	private static final long serialVersionUID = 1L;
	private Stage primaryStage;

	/**
	 * Constructor.
	 */
	public GameApplication() {
		super();
	}

	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle(Constants.NAME);
		Pane startPanel = new StartPane();
		MenuBar menuBar = new GameMenue(this);
		BorderPane bp = new BorderPane();
		bp.setTop(menuBar);
		bp.setCenter(startPanel);
		Scene scene = new Scene(bp, startPanel.getWidth(),
				startPanel.getHeight());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * @param args
	 *            String[]
	 */
	public static void main(final String[] args) {
		launch(args);
	}

	/**
	 * 
	 */
	public void quitGame() {
	}

	/**
	 * @param path
	 * @param modeEdit
	 */
	public void load(String path, ModeEnum modeEdit) {
	}

	/**
	 * @param xmlData
	 * @param b
	 * @param modePlay
	 */
	public void start(String xmlData, boolean b, ModeEnum modePlay) {
	}

	/**
	 * @return
	 */
	public GamePanel getGamePanel() {
		return null;
	}

	/**
	 * @return
	 */
	public EditorPanel getEditorPanel() {
		return null;
	}

	/**
	 * @return Stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * 
	 */
	public void exitGame() {
	}

	/**
	 * 
	 */
	public void start() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param path
	 */
	public void loadOptions(String path) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param filename
	 */
	public void saveOptions(String filename) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return
	 */
	public Object getMode() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param mode
	 */
	public void init(ModeEnum mode) {
		// TODO Auto-generated method stub

	}
}