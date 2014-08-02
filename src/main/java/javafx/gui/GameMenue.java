package javafx.gui;

import game.GameSession;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import util.Constants;
import util.ModeEnum;

/**
 * @author Administrator
 * 
 */
public class GameMenue extends MenuBar {

	private final GameApplication parentApplication;
	private static final String OVER_FILE_SUFFIX = ".over";
	private static final String OVER_FILE_FILTER = "*" + OVER_FILE_SUFFIX;

	/**
	 * @param parentApplication
	 *            GameApplication
	 */
	protected GameMenue(final GameApplication parentApplication) {
		this.parentApplication = parentApplication;
		getMenus().add(getMenuFile());
	}

	private Menu getMenuFile() {
		final Menu menuFile = new Menu("_File");
		if (getGamePanel() == null && getEditorPanel() == null) {
			menuFile.getItems().add(getMenuItemNewGame());
			menuFile.getItems().add(getMenuItemJoinNetgame());
			menuFile.getItems().add(getMenuItemEditBoard());
		}
		if (getEditorPanel() != null) {
			menuFile.getItems().add(getMenuItemStartGame());
		}
		if (getGamePanel() == null) {
			menuFile.getItems().add(getMenuItemLoadGame());
		}
		if (getGamePanel() != null || getEditorPanel() != null) {
			menuFile.getItems().add(getMenuItemSaveGame());
		}
		if (getGamePanel() == null) {
			menuFile.getItems().add(new SeparatorMenuItem());
			menuFile.getItems().add(getMenuFiles());
		}
		menuFile.getItems().add(new SeparatorMenuItem());
		if (getGamePanel() != null || getEditorPanel() != null) {
			menuFile.getItems().add(getMenuItemQuitGame());
		}
		menuFile.getItems().add(getMenuItemExitGame());
		return menuFile;
	}

	private MenuItem getMenuItemNewGame() {
		final MenuItem menuItemNewGame = new MenuItem("_New Game");
		menuItemNewGame.setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see javafx.event.EventHandler#handle(javafx.event.Event)
			 */
			@Override
			public void handle(ActionEvent event) {
//				final OptionsDialogStage options = new OptionsDialogStage(
//						getParentApplication(), ModeEnum.MODE_PLAY);
//				options.show();
			}
		});
		return menuItemNewGame;
	}

	private MenuItem getMenuItemJoinNetgame() {
		final MenuItem menuItemJoinNetgame = new MenuItem("_Join Netgame");
		menuItemJoinNetgame.setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see javafx.event.EventHandler#handle(javafx.event.Event)
			 */
			@Override
			public void handle(ActionEvent event) {
				// final JoinNetgameDialog joinNetGame = new JoinNetgameDialog(
				// getParentApplication(), true, new NetOptions());
				// joinNetGame.setVisible(true);
			}
		});
		return menuItemJoinNetgame;
	}

	private MenuItem getMenuItemEditBoard() {
		final MenuItem menuItemEditBoard = new MenuItem("_Edit Board");
		menuItemEditBoard.setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see javafx.event.EventHandler#handle(javafx.event.Event)
			 */
			@Override
			public void handle(ActionEvent event) {
//				final OptionsDialogStage options = new OptionsDialogStage(
//						getParentApplication(), ModeEnum.MODE_EDIT);
//				options.show();
			}
		});
		return menuItemEditBoard;
	}

	private MenuItem getMenuItemStartGame() {
		final MenuItem menuItemStartGame = new MenuItem("Start _new Game");
		menuItemStartGame.setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see javafx.event.EventHandler#handle(javafx.event.Event)
			 */
			@Override
			public void handle(ActionEvent event) {
				getParentApplication().start(getEditorPanel().getXmlData(),
						true, ModeEnum.MODE_PLAY);
			}
		});
		return menuItemStartGame;
	}

	private MenuItem getMenuItemLoadGame() {
		final MenuItem menuItemLoadGame = new MenuItem("_Load game");
		menuItemLoadGame.setOnAction(new LoadGameActionListener());
		if (getGamePanel() != null && GameSession.gameOptions.getNetwork() > 0) {
			// Waehrend eines aktiven Netzwerkspiels ist das Laden eines alten
			// Spielstands nicht moeglich
			menuItemLoadGame.setDisable(true);
		}
		return menuItemLoadGame;
	}

	private MenuItem getMenuItemSaveGame() {
		final MenuItem menuItemSaveGame = new MenuItem("_Save game");
		menuItemSaveGame.setOnAction(new SaveGameActionListener());
		// Solange das Spiel nicht gestartet wurde und waehrend eines aktiven
		// Netzwerkspiels ist das Speichern eines Spielstands nicht moeglich
		if (!isEditMode()
				&& (getGamePanel() == null || GameSession.gameOptions
						.getNetwork() > 0)) {
			menuItemSaveGame.setDisable(true);
		}
		return menuItemSaveGame;
	}

	/**
	 * @author Administrator
	 * 
	 */
	class LoadGameActionListener implements EventHandler<ActionEvent> {

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void handle(ActionEvent event) {
			final FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File("./"));
			fileChooser.getExtensionFilters().add(
					new ExtensionFilter("Game-Files", OVER_FILE_FILTER));
			File selectedFile = fileChooser
					.showOpenDialog(getParentApplication().getPrimaryStage());
			if (selectedFile != null) {
				if (isEditMode()) {
					getParentApplication().load(selectedFile.getPath(),
							ModeEnum.MODE_EDIT);
				} else {
					getParentApplication().load(selectedFile.getPath(),
							ModeEnum.MODE_PLAY);
				}
			}
		}
	}

	/**
	 * @author Administrator
	 * 
	 */
	class SaveGameActionListener implements EventHandler<ActionEvent> {

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void handle(ActionEvent event) {
			final FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File("./"));
			fileChooser.getExtensionFilters().add(
					new ExtensionFilter("Game-Files", OVER_FILE_FILTER));
			File selectedFile = fileChooser
					.showSaveDialog(getParentApplication().getPrimaryStage());
			if (selectedFile != null) {
				String filename = selectedFile.getPath();
				final int pos = filename.lastIndexOf('.');
				if (pos > 0) {
					final String suffix = filename.substring(pos);
					if (!OVER_FILE_SUFFIX.equals(suffix)) {
						filename = filename + OVER_FILE_SUFFIX;
					}
				} else {
					filename = filename + OVER_FILE_SUFFIX;
				}
				if (isEditMode()) {
					getEditorPanel().saveGame(filename);
				} else {
					getGamePanel().saveGame(filename);
				}
			}
		}
	}

	private MenuItem getMenuFiles() {
		final Menu menuFiles = new Menu("File(s)");
		final String[] entries = Constants.USERDIR.list();
		Collections.sort(Arrays.asList(entries));
		int gamefiles = 0;
		for (final String entry : entries) {
			if (entry.endsWith(OVER_FILE_SUFFIX)) {
				gamefiles++;
				final MenuItem menuItemFile = new MenuItem(entry);
				menuItemFile.setOnAction(new EventHandler<ActionEvent>() {

					/**
					 * {@inheritDoc}
					 * 
					 * @see javafx.event.EventHandler#handle(javafx.event.Event)
					 */
					@Override
					public void handle(ActionEvent event) {
						if (event.getSource() != null) {
							if (isEditMode()) {
								getParentApplication().load(
										((MenuItem) event.getSource())
												.getText(), ModeEnum.MODE_EDIT);
							} else {
								getParentApplication().load(
										((MenuItem) event.getSource())
												.getText(), ModeEnum.MODE_PLAY);
							}
						}
					}
				});
				menuFiles.getItems().add(menuItemFile);
			}
		}
		if (gamefiles == 0) {
			menuFiles.setDisable(true);
		}
		if (getGamePanel() != null) {
			// Waehrend eines aktiven Netzwerkspiels ist das Laden eines alten
			// Spielstands nicht moeglich
			menuFiles.setDisable(GameSession.gameOptions.getNetwork() != 0);
		}
		return menuFiles;
	}

	private MenuItem getMenuItemQuitGame() {
		MenuItem menuItemQuitGame;
		if (isEditMode()) {
			menuItemQuitGame = new MenuItem("_Quit editor");
		} else {
			menuItemQuitGame = new MenuItem("_Quit game");
		}
		menuItemQuitGame.setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see javafx.event.EventHandler#handle(javafx.event.Event)
			 */
			@Override
			public void handle(ActionEvent event) {
				getParentApplication().quitGame();
			}
		});
		return menuItemQuitGame;
	}

	private MenuItem getMenuItemExitGame() {
		final MenuItem menuItemExitGame = new MenuItem("E_xit");
		menuItemExitGame.setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see javafx.event.EventHandler#handle(javafx.event.Event)
			 */
			@Override
			public void handle(ActionEvent event) {
				if (getGamePanel() != null) {
					getGamePanel().stop();
				}
				getParentApplication().exitGame();
			}
		});
		return menuItemExitGame;
	}

	/**
	 * @return GamePanel
	 */
	protected GamePanel getGamePanel() {
		return parentApplication.getGamePanel();
	}

	/**
	 * @return EditorPanel
	 */
	protected EditorPanel getEditorPanel() {
		return parentApplication.getEditorPanel();
	}

	/**
	 * @return boolean
	 */
	protected boolean isEditMode() {
		return parentApplication.getEditorPanel() != null;
	}

	/**
	 * @return GameFrame
	 */
	protected GameApplication getParentApplication() {
		return parentApplication;
	}
}
