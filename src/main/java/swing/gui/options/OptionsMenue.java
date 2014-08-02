package swing.gui.options;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import util.Constants;
import util.ModeEnum;
import util.PseudoLogger;

/**
 * @author Administrator
 * 
 */
public class OptionsMenue {

	private final OptionsDialog parentDialog;
	private static final String INI_FILE = ".ini";

	/**
	 * @param parentDialog
	 *            OptionsDialog
	 */
	protected OptionsMenue(final OptionsDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	/**
	 * @return JMenuBar
	 */
	protected JMenuBar getOptionsMenuBar() {
		final JMenuBar jMenuBar = new JMenuBar();
		jMenuBar.add(getMenuFile());
		jMenuBar.add(getMenuLookAndFeel());
		return jMenuBar;
	}

	private JMenu getMenuFile() {
		final JMenu jMenuFile = new JMenu("File");
		jMenuFile.setMnemonic('f');
		jMenuFile.add(getMenuItemStartGame());
		jMenuFile.add(getMenuItemLoadOptions());
		jMenuFile.add(getMenuItemSaveOptions());
		jMenuFile.addSeparator();
		jMenuFile.add(getMenuFiles());
		return jMenuFile;
	}

	/**
	 * @return JMenuItem
	 */
	private JMenuItem getMenuItemStartGame() {
		JMenuItem jMenuItemStartGame = null;
		if (ModeEnum.MODE_EDIT.equals(getParentDialog().getMode())) {
			jMenuItemStartGame = new JMenuItem("Start editor", 's');
		} else {
			jMenuItemStartGame = new JMenuItem("Start game", 's');
		}
		// jMenuItemStartGame.setAccelerator(KeyStroke.getKeyStroke('s',
		// InputEvent.CTRL_MASK));
		jMenuItemStartGame.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				getParentDialog().start();
			}
		});
		return jMenuItemStartGame;
	}

	private JMenuItem getMenuItemLoadOptions() {
		final JMenuItem jMenuItemLoadOptions = new JMenuItem("Load options",
				'l');
		jMenuItemLoadOptions.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				final JFileChooser jFileChooser = new JFileChooser("./");
				jFileChooser.setFileFilter(new FileFilter() {

					/**
					 * {@inheritDoc}
					 * 
					 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
					 */
					@Override
					public boolean accept(final File file) {
						return file.isDirectory()
								|| file.getName().toLowerCase()
										.endsWith(INI_FILE);
					}

					/**
					 * {@inheritDoc}
					 * 
					 * @see javax.swing.filechooser.FileFilter#getDescription()
					 */
					@Override
					public String getDescription() {
						return "Options";
					}
				});
				final int returnVal = jFileChooser
						.showOpenDialog(getParentDialog());
				if (returnVal == 0) {
					getParentDialog().loadOptions(
							jFileChooser.getSelectedFile().getPath());
				}
			}
		});
		return jMenuItemLoadOptions;
	}

	private JMenuItem getMenuItemSaveOptions() {
		final JMenuItem jMenuItemSaveOptions = new JMenuItem("Save options",
				's');
		jMenuItemSaveOptions.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				final JFileChooser jFileChooser = new JFileChooser("./");
				jFileChooser.setFileFilter(new FileFilter() {

					/**
					 * {@inheritDoc}
					 * 
					 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
					 */
					@Override
					public boolean accept(final File file) {
						return file.isDirectory()
								|| file.getName().toLowerCase()
										.endsWith(INI_FILE);
					}

					/**
					 * {@inheritDoc}
					 * 
					 * @see javax.swing.filechooser.FileFilter#getDescription()
					 */
					@Override
					public String getDescription() {
						return "Options";
					}
				});
				final int returnVal = jFileChooser
						.showSaveDialog(getParentDialog());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filename = jFileChooser.getSelectedFile().getPath();
					final int pos = filename.lastIndexOf('.');
					if (pos > 0) {
						final String suffix = filename.substring(pos);
						if (!INI_FILE.equals(suffix)) {
							filename = filename + INI_FILE;
						}
					} else {
						filename = filename + INI_FILE;
					}
					getParentDialog().saveOptions(filename);
					getParentDialog().setJMenuBar(getOptionsMenuBar());
					SwingUtilities.updateComponentTreeUI(getParentDialog());
				}
			}
		});
		return jMenuItemSaveOptions;
	}

	private JMenuItem getMenuFiles() {
		final JMenu jMenuFiles = new JMenu("File(s)");
		final String[] entries = Constants.USERDIR.list();
		Collections.sort(Arrays.asList(entries));
		int gamefiles = 0;
		for (final String entry : entries) {
			if (entry.endsWith(INI_FILE)) {
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
							getParentDialog().loadOptions(
									event.getActionCommand());
						}
					}
				});
				jMenuFiles.add(jMenuItemFile);
			}
		}
		if (gamefiles == 0) {
			jMenuFiles.setEnabled(false);
		}
		return jMenuFiles;
	}

	private JMenu getMenuLookAndFeel() {
		final JMenu jMenuLookAndFeel = new JMenu("LookAndFeel");
		jMenuLookAndFeel.setMnemonic('l');
		jMenuLookAndFeel.add(getMenuItemWindows());
		jMenuLookAndFeel.add(getMenuItemMotif());
		jMenuLookAndFeel.add(getMenuItemMetal());
		jMenuLookAndFeel.add(getMenuItemNimbus());
		return jMenuLookAndFeel;
	}

	private JMenuItem getMenuItemWindows() {
		final JMenuItem jMenuItemWindows = new JMenuItem("Windows", 'w');
		jMenuItemWindows.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				try {
					UIManager
							.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					SwingUtilities.updateComponentTreeUI(getParentDialog());
					SwingUtilities.updateComponentTreeUI(getParentDialog()
							.getParent());
				} catch (final ClassNotFoundException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				} catch (final InstantiationException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				} catch (final IllegalAccessException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				} catch (final UnsupportedLookAndFeelException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				}
			}
		});
		return jMenuItemWindows;
	}

	private JMenuItem getMenuItemMotif() {
		final JMenuItem jMenuItemMotif = new JMenuItem("Motif", 'o');
		jMenuItemMotif.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				try {
					UIManager
							.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
					SwingUtilities.updateComponentTreeUI(getParentDialog());
					SwingUtilities.updateComponentTreeUI(getParentDialog()
							.getParent());
				} catch (final ClassNotFoundException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				} catch (final InstantiationException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				} catch (final IllegalAccessException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				} catch (final UnsupportedLookAndFeelException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				}
			}
		});
		return jMenuItemMotif;
	}

	private JMenuItem getMenuItemMetal() {
		final JMenuItem jMenuItemMetal = new JMenuItem("Metal", 'e');
		jMenuItemMetal.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				try {
					UIManager
							.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
					SwingUtilities.updateComponentTreeUI(getParentDialog());
					SwingUtilities.updateComponentTreeUI(getParentDialog()
							.getParent());
				} catch (final ClassNotFoundException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				} catch (final InstantiationException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				} catch (final IllegalAccessException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				} catch (final UnsupportedLookAndFeelException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				}
			}
		});
		return jMenuItemMetal;
	}

	private JMenuItem getMenuItemNimbus() {
		final JMenuItem jMenuItemNimbus = new JMenuItem("Nimbus", 'n');
		jMenuItemNimbus.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				try {
					UIManager
							.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					SwingUtilities.updateComponentTreeUI(getParentDialog());
					SwingUtilities.updateComponentTreeUI(getParentDialog()
							.getParent());
				} catch (final ClassNotFoundException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				} catch (final InstantiationException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				} catch (final IllegalAccessException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				} catch (final UnsupportedLookAndFeelException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				}
			}
		});
		return jMenuItemNimbus;
	}

	OptionsDialog getParentDialog() {
		return parentDialog;
	}
}
