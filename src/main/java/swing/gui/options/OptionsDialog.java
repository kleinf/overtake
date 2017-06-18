package swing.gui.options;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import game.GameOptions;
import game.GameSession;
import swing.gui.GameFrame;
import util.ModeEnum;
import util.PseudoLogger;

/**
 * @author Administrator
 * 
 */
public class OptionsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JButton jButtonStart = null;
	private JTabbedPane jTabbedPane;
	private final ModeEnum mode;

	/**
	 * @param parentFrame
	 *            Frame
	 * @param mode
	 *            Mode
	 */
	public OptionsDialog(final Frame parentFrame, final ModeEnum mode) {
		super(parentFrame, true);
		setSize(430, 300);
		setPreferredSize(getSize());
		setLocationRelativeTo(getParent());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// Esc closes dialog
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				"Close");
		getRootPane().getActionMap().put("Close", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});

		setTitle("Game Options");
		setResizable(false);
		this.mode = mode;
		final OptionsMenue optionsMenue = new OptionsMenue(this);
		setJMenuBar(optionsMenue.getOptionsMenuBar());

		initialize();
	}

	private final void initialize() {
		GameSession.gameOptions = new GameOptions();

		final JPanel jPanel = new JPanel(new BorderLayout());
		jPanel.add(getJTabbedPane(), BorderLayout.CENTER);
		jPanel.add(getJButtonStart(), BorderLayout.SOUTH);
		setContentPane(jPanel);
	}

	/**
	 * This method initializes jTabbedPane.
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private final JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			AbstractOptDlgPnl optionDialogPanel = null;

			optionDialogPanel = new BoardOptionDialogPanel(this, "Board");
			optionDialogPanel.initialize();
			jTabbedPane.addTab(optionDialogPanel.getName(), optionDialogPanel);
			optionDialogPanel = new PlayerOptionDialogPanel(this, "Player");
			optionDialogPanel.initialize();
			jTabbedPane.addTab(optionDialogPanel.getName(), optionDialogPanel);
			optionDialogPanel = new GameOptionDialogPanel(this, "Game");
			optionDialogPanel.initialize();
			jTabbedPane.addTab(optionDialogPanel.getName(), optionDialogPanel);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes getJButtonStart.
	 * 
	 * @return javax.swing.JButton
	 */
	private final JButton getJButtonStart() {
		if (jButtonStart == null) {
			jButtonStart = new JButton();
			if (ModeEnum.MODE_EDIT.equals(getMode())) {
				jButtonStart.setText("Start editor");
			} else {
				jButtonStart.setText("Start game");
			}
		}
		jButtonStart.addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				start();
			}
		});
		return jButtonStart;
	}

	/**
	 * @param filename
	 *            String
	 */
	protected void loadOptions(final String filename) {
		try {
			final File file = new File(filename);
			final SAXBuilder builder = new SAXBuilder();
			final Document doc = builder.build(file);

			GameSession.gameOptions.setData(doc.getRootElement());
			for (int i = 0; i < jTabbedPane.getTabCount(); i++) {
				((AbstractOptDlgPnl) jTabbedPane.getComponentAt(i)).removeAll();
			}
			jTabbedPane.removeAll();
			jTabbedPane = null;
			remove(getContentPane());
			initialize();
			SwingUtilities.updateComponentTreeUI(this);
		} catch (final IOException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
		} catch (final JDOMException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
		}
	}

	/**
	 * @param filename
	 *            String
	 */
	protected void saveOptions(final String filename) {
		FileWriter writer = null;
		try {
			// Optionen aus den Dialogen auslesen
			updateOptions();

			// XML speichern
			final XMLOutputter serializer = new XMLOutputter();
			writer = new FileWriter(filename);
			serializer.output(new Document(GameSession.gameOptions.getData()), writer);
			writer.flush();
		} catch (final IOException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (final IOException exception) {
				PseudoLogger.getInstance().log(exception.getMessage());
			}
		}
	}

	/**
	 * 
	 */
	protected void start() {
		updateOptions();
		dispose();
		((GameFrame) getParent()).init(mode);
	}

	private void updateOptions() {
		for (int i = 0; i < jTabbedPane.getTabCount(); i++) {
			((AbstractOptDlgPnl) jTabbedPane.getComponentAt(i)).updateOptions();
		}
	}

	/**
	 * @return Mode
	 */
	protected ModeEnum getMode() {
		return mode;
	}
}
