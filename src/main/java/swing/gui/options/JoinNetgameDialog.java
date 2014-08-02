package swing.gui.options;

import game.NetOptions;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import swing.gui.GameFrame;

/**
 * @author Administrator
 * 
 */
public class JoinNetgameDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JLabel jLblPlayerName = null;
	private JTextField jTxtHost = null;
	private JTextField jTxtPort = null;
	private JTextField jTxtName = null;
	private JButton jButtonColor = null;
	private JButton jButtonStart = null;
	private JButton jButtonCheck = null;
	private NetOptions netOptions = null;

	/**
	 * @param parentFrame
	 *            Frame
	 * @param isModal
	 *            boolean
	 * @param netOptions
	 *            NetOptions
	 */
	public JoinNetgameDialog(final Frame parentFrame, final boolean isModal,
			final NetOptions netOptions) {
		super(parentFrame, isModal);

		setSize(220, 150);
		setPreferredSize(getSize());
		setLocationRelativeTo(getParent());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// Esc closes dialog
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Close");
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

		setTitle("Join Netgame");
		if (netOptions == null) {
			this.netOptions = new NetOptions();
		} else {
			this.netOptions = netOptions;
		}

		final GridBagLayout layout = new GridBagLayout();
		layout.preferredLayoutSize(this);
		setLayout(layout);

		initialize();
	}

	private final void initialize() {
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0D;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridwidth = 1;
		add(getJTxtHost(), gbc);
		gbc.gridwidth = 1;
		add(new JLabel(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJTxtPort(), gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJLblPlayerName(), gbc);

		gbc.gridwidth = 1;
		add(getJTxtName(), gbc);
		gbc.gridwidth = 1;
		add(new JLabel(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		add(getJButtonColor(), gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridwidth = 1;
		add(getJButtonCheck(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJButtonStart(), gbc);
	}

	/**
	 * This method initializes jLblPlayerName.
	 * 
	 * @return javax.swing.JLabel
	 */
	private final JLabel getJLblPlayerName() {
		if (jLblPlayerName == null) {
			jLblPlayerName = new JLabel();
			jLblPlayerName.setText("Spielername");
		}
		return jLblPlayerName;
	}

	/**
	 * This method initializes jTxtHost.
	 * 
	 * @return javax.swing.JTextField
	 */
	final JTextField getJTxtHost() {
		if (jTxtHost == null) {
			jTxtHost = new JTextField();
			jTxtHost.setText(getNetOptions().getHost());
		}
		return jTxtHost;
	}

	/**
	 * This method initializes jTxtPort.
	 * 
	 * @return javax.swing.JTextPort
	 */
	final JTextField getJTxtPort() {
		if (jTxtPort == null) {
			jTxtPort = new JTextField();
			jTxtPort.setText(Integer.toString(getNetOptions().getPort()));
		}
		return jTxtPort;
	}

	/**
	 * This method initializes jTxtName.
	 * 
	 * @return javax.swing.JTextField
	 */
	final JTextField getJTxtName() {
		if (jTxtName == null) {
			jTxtName = new JTextField();
			jTxtName.setText(netOptions.getPlayerName());
		}
		return jTxtName;
	}

	/**
	 * This method initializes jButtonColor.
	 * 
	 * @return javax.swing.JButton
	 */
	final JButton getJButtonColor() {
		if (jButtonColor == null) {
			jButtonColor = new JButton();
			jButtonColor.addActionListener(new ActionListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				@Override
				public void actionPerformed(final ActionEvent event) {
					final JComponent comp = (JComponent) event.getSource();
					final Color newColor = JColorChooser.showDialog(comp,
							"Choose your color", comp.getBackground());
					comp.setBackground(newColor);
				}
			});
			jButtonColor.setBackground(new Color(netOptions.getPlayerColor()));
		}
		return jButtonColor;
	}

	/**
	 * This method initializes jButtonCheck.
	 * 
	 * @return javax.swing.JButton
	 */
	private final JButton getJButtonCheck() {
		if (jButtonCheck == null) {
			jButtonCheck = new JButton();
			jButtonCheck.setEnabled(false);
			jButtonCheck.setText("Check options");
			jButtonCheck.addActionListener(new ActionListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				@Override
				public void actionPerformed(final ActionEvent event) {
					// TODO Aufruf des Optionen-Dialogs implementieren. Die
					// Einstellungen werden dabei vom Host gelesen und im Dialog
					// mit deaktivierten Feldern angezeigt.
				}
			});
		}
		return jButtonCheck;
	}

	/**
	 * This method initializes jButtonStart.
	 * 
	 * @return javax.swing.JButton
	 */
	private final JButton getJButtonStart() {
		if (jButtonStart == null) {
			jButtonStart = new JButton();
			jButtonStart.setText("Join game");
			jButtonStart.addActionListener(new ActionListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				@Override
				public void actionPerformed(final ActionEvent event) {
					getNetOptions().setHost(getJTxtHost().getText());
					if (getJTxtPort().getText() != null
							&& getJTxtPort().getText().trim().length() > 0) {
						getNetOptions().setPort(
								Integer.parseInt(getJTxtPort().getText()));
					}
					getNetOptions().setPlayerName(getJTxtName().getText());
					getNetOptions().setPlayerColor(
							getJButtonColor().getBackground().getRGB());
					dispose();
					((GameFrame) getParent()).joinNetGame(getNetOptions());
				}
			});
		}
		return jButtonStart;
	}

	/**
	 * @return NetOptions
	 */
	NetOptions getNetOptions() {
		return netOptions;
	}
}
