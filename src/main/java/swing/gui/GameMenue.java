package swing.gui;

import game.GameSession;
import game.NetOptions;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import util.Constants;
import util.ModeEnum;
import swing.gui.options.JoinNetgameDialog;
import swing.gui.options.OptionsDialog;

/**
 * @author Administrator
 * 
 */
public class GameMenue {

	private final GameFrame parentFrame;
	private static final String OVER_FILE = ".over";

	/**
	 * @param parentFrame
	 *            GameFrame
	 */
	protected GameMenue(final GameFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

	/**
	 * @return JMenuBar
	 */
	protected JMenuBar getGameMenuBar() {
		final JMenuBar jMenuBar = new JMenuBar();
		jMenuBar.add(getMenuFile());
		return jMenuBar;
	}

	private JMenu getMenuFile() {
		final JMenu jMenuFile = new JMenu("File");
		jMenuFile.setMnemonic('f');
		if (getGamePanel() == null && getEditorPanel() == null) {
			jMenuFile.add(getMenuItemNewGame());
			jMenuFile.add(getMenuItemJoinNetgame());
			jMenuFile.add(getMenuItemEditBoard());
		}
		if (getEditorPanel() != null) {
			jMenuFile.add(getMenuItemStartGame());
		}
		if (getGamePanel() == null) {
			jMenuFile.add(getMenuItemLoadGame());
		}
		if (getGamePanel() != null || getEditorPanel() != null) {
			jMenuFile.add(getMenuItemSaveGame());
		}
		if (getGamePanel() == null) {
			jMenuFile.addSeparator();
			jMenuFile.add(getMenuFiles());
		}
		jMenuFile.addSeparator();
		if (getGamePanel() != null || getEditorPanel() != null) {
			jMenuFile.add(getMenuItemQuitGame());
		}
		jMenuFile.add(getMenuItemExitGame());
		return jMenuFile;
	}

	private JMenuItem getMenuItemNewGame() {
		final JMenuItem jMenuItemNewGame = new JMenuItem("New Game", 'n');
		// jMenuItemNewGame.setAccelerator(KeyStroke.getKeyStroke('n',
		// InputEvent.CTRL_MASK));
		jMenuItemNewGame.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				final OptionsDialog options = new OptionsDialog(
						getParentFrame(), ModeEnum.MODE_PLAY);
				options.setVisible(true);
			}
		});
		return jMenuItemNewGame;
	}

	private JMenuItem getMenuItemJoinNetgame() {
		final JMenuItem jMenuItemJoinNetgame = new JMenuItem("Join Netgame",
				'j');
		jMenuItemJoinNetgame.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				final JoinNetgameDialog joinNetGame = new JoinNetgameDialog(
						getParentFrame(), true, new NetOptions());
				joinNetGame.setVisible(true);
			}
		});
		return jMenuItemJoinNetgame;
	}

	private JMenuItem getMenuItemEditBoard() {
		final JMenuItem jMenuItemEditBoard = new JMenuItem("Edit Board", 'e');
		jMenuItemEditBoard.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				final OptionsDialog options = new OptionsDialog(
						getParentFrame(), ModeEnum.MODE_EDIT);
				options.setVisible(true);
			}
		});
		return jMenuItemEditBoard;
	}

	private JMenuItem getMenuItemStartGame() {
		final JMenuItem jMenuItemStartGame = new JMenuItem("Start Game", 'n');
		// jMenuItemNewGame.setAccelerator(KeyStroke.getKeyStroke('n',
		// InputEvent.CTRL_MASK));
		jMenuItemStartGame.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				getParentFrame().start(getEditorPanel().getXmlData(), true,
						ModeEnum.MODE_PLAY);
			}
		});
		return jMenuItemStartGame;
	}

	private JMenuItem getMenuItemLoadGame() {
		final JMenuItem jMenuItemLoadGame = new JMenuItem("Load game", 'l');
		jMenuItemLoadGame.addActionListener(new LoadGameActionListener());
		if (getGamePanel() != null && GameSession.gameOptions.getNetwork() > 0) {
			// Waehrend eines aktiven Netzwerkspiels ist das Laden eines alten
			// Spielstands nicht moeglich
			jMenuItemLoadGame.setEnabled(false);
		}
		return jMenuItemLoadGame;
	}

	private JMenuItem getMenuItemSaveGame() {
		final JMenuItem jMenuItemSaveGame = new JMenuItem("Save game", 's');
		jMenuItemSaveGame.addActionListener(new SaveGameActionListener());
		if (!isEditMode()
				&& (getGamePanel() == null || GameSession.gameOptions
						.getNetwork() > 0)) {
			// Solange das Spiel nicht gestartet wurde und waehrend eines
			// aktiven
			// Netzwerkspiels ist das Speichern eines Spielstands nicht moeglich
			jMenuItemSaveGame.setEnabled(false);
		}
		return jMenuItemSaveGame;
	}

	/**
	 * @author Administrator
	 * 
	 */
	class LoadGameActionListener implements ActionListener {

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(final ActionEvent actionevent) {
			final JFileChooser jFileChooser = new JFileChooser("./");
			jFileChooser.setFileFilter(new GameFileFilter());
			int returnVal;
			if (isEditMode()) {
				returnVal = jFileChooser.showOpenDialog(getEditorPanel());
			} else {
				returnVal = jFileChooser.showOpenDialog(getGamePanel());
			}
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (isEditMode()) {
					getParentFrame().load(
							jFileChooser.getSelectedFile().getPath(),
							ModeEnum.MODE_EDIT);
				} else {
					getParentFrame().load(
							jFileChooser.getSelectedFile().getPath(),
							ModeEnum.MODE_PLAY);
				}
			}
		}
	}

	/**
	 * @author Administrator
	 * 
	 */
	class SaveGameActionListener implements ActionListener {

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(final ActionEvent actionevent) {
			final JFileChooser jFileChooser = new JFileChooser("./");
			jFileChooser.setFileFilter(new GameFileFilter());
			int returnVal;
			if (isEditMode()) {
				returnVal = jFileChooser.showSaveDialog(getEditorPanel());
			} else {
				returnVal = jFileChooser.showSaveDialog(getGamePanel());
			}
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String filename = jFileChooser.getSelectedFile().getPath();
				final int pos = filename.lastIndexOf('.');
				if (pos > 0) {
					final String suffix = filename.substring(pos);
					if (!OVER_FILE.equals(suffix)) {
						filename = filename + OVER_FILE;
					}
				} else {
					filename = filename + OVER_FILE;
				}
				if (isEditMode()) {
					getEditorPanel().saveGame(filename);
				} else {
					getGamePanel().saveGame(filename);
				}
			}
		}
	}

	/**
	 * @author Administrator
	 * 
	 */
	protected static class GameFileFilter extends FileFilter {

		/**
		 * {@inheritDoc}
		 * 
		 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
		 */
		@Override
		public boolean accept(final File file) {
			return file.isDirectory()
					|| file.getName().toLowerCase().endsWith(OVER_FILE);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see javax.swing.filechooser.FileFilter#getDescription()
		 */
		@Override
		public String getDescription() {
			return "Game-Files";
		}
	}

	private JMenuItem getMenuFiles() {
		final JMenu jMenuFiles = new JMenu("File(s)");
		final String[] entries = Constants.USERDIR.list();
		Collections.sort(Arrays.asList(entries));
		int gamefiles = 0;
		for (final String entry : entries) {
			if (entry.endsWith(OVER_FILE)) {
				gamefiles++;

				final JMenuItem jMenuItemFile = new JMenuItem(entry);
				jMenuItemFile.addActionListener(new ActionListener() {

					/**
					 * {@inheritDoc}
					 * 
					 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
					 */
					@Override
					public void actionPerformed(final ActionEvent event) {
						if (event.getActionCommand() != null) {
							if (isEditMode()) {
								getParentFrame().load(event.getActionCommand(),
										ModeEnum.MODE_EDIT);
							} else {
								getParentFrame().load(event.getActionCommand(),
										ModeEnum.MODE_PLAY);
							}
						}
					}
				});
				jMenuFiles.add(jMenuItemFile);
			}
		}
		if (gamefiles == 0) {
			jMenuFiles.setEnabled(false);
		}
		if (getGamePanel() != null) {
			// Waehrend eines aktiven Netzwerkspiels ist das Laden eines alten
			// Spielstands nicht moeglich
			jMenuFiles.setEnabled(GameSession.gameOptions.getNetwork() == 0);
		}
		return jMenuFiles;
	}

	private JMenuItem getMenuItemQuitGame() {
		JMenuItem jMenuItemQuitGame;
		if (isEditMode()) {
			jMenuItemQuitGame = new JMenuItem("Quit editor", 'q');
		} else {
			jMenuItemQuitGame = new JMenuItem("Quit game", 'q');
		}
		jMenuItemQuitGame.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				getParentFrame().quitGame();
			}
		});
		return jMenuItemQuitGame;
	}

	private JMenuItem getMenuItemExitGame() {
		final JMenuItem jMenuItemExitGame = new JMenuItem("Exit", 'x');
		jMenuItemExitGame.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				if (getGamePanel() != null) {
					getGamePanel().stop();
				}
				getParentFrame().exitGame();
			}
		});
		return jMenuItemExitGame;
	}

	/**
	 * @return GamePanel
	 */
	protected GamePanel getGamePanel() {
		return parentFrame.getGamePanel();
	}

	/**
	 * @return EditorPanel
	 */
	protected EditorPanel getEditorPanel() {
		return parentFrame.getEditorPanel();
	}

	/**
	 * @return boolean
	 */
	protected boolean isEditMode() {
		return parentFrame.getEditorPanel() != null;
	}

	/**
	 * @return GameFrame
	 */
	protected GameFrame getParentFrame() {
		return parentFrame;
	}
}
