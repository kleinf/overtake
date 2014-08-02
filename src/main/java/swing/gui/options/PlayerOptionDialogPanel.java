package swing.gui.options;

import game.GameOptions;
import game.GameSession;
import game.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Constants;
import util.PseudoLogger;
import swing.util.AnimatedImageUtil;

/**
 * @author Administrator
 * 
 */
public class PlayerOptionDialogPanel extends AbstractOptDlgPnl {

	private static final long serialVersionUID = 1L;
	private JLabel jLblMaxPlayers = null;
	private JSlider jSldMaxPlayers = null;
	private JLabel jLblMaxPlayersValue = null;
	private JLabel jLblPlayerName = null;
	private JLabel jLblCpu = null;
	private JLabel jLblNet = null;
	private JCheckBox jCbNetwork = null;
	private JComboBox<String> jCobHost = null;
	private JTextField jTxtPort = null;
	private List<JTextField> jTxtPlayername = null;
	private List<JComboBox<String>> jCbComputername = null;
	private List<JCheckBox> jCbCpu = null;
	private List<JButton> jButtonColor = null;
	private List<JComboBox<String>> jCbImage = null;

	/**
	 * @param parentDialog
	 *            JDialog
	 * @param name
	 *            String
	 */
	protected PlayerOptionDialogPanel(final JDialog parentDialog,
			final String name) {
		super(parentDialog, name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see swing.gui.options.AbstractOptDlgPnl#initialize()
	 */
	@Override
	protected void initialize() {

		final GridBagConstraints gbc = getInitGridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(new JLabel(" "), gbc); // Leere Kopfzeile

		gbc.gridwidth = 2;
		add(getJLblMaxPlayers(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJLblNet(), gbc);

		gbc.gridwidth = 1;
		add(getJSldMaxPlayers(), gbc);
		gbc.gridwidth = 1;
		add(getJLblMaxPlayersValue(), gbc);
		gbc.gridwidth = 1;
		add(getJCbNetwork(), gbc);
		gbc.gridwidth = 1;
		add(getJCobHost(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJTxtPort(), gbc);

		gbc.gridwidth = 2;
		add(getJLblPlayerName(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJLblCpu(), gbc);

		for (int i = 0; i < getJSldMaxPlayers().getMaximum(); i++) {
			gbc.gridwidth = 2;
			add(getJTxtPlayername().get(i), gbc);
			add(getJCbComputername().get(i), gbc);
			gbc.gridwidth = 1;
			add(getJCbCpu().get(i), gbc);
			gbc.gridwidth = 1;
			add(getJCbImage().get(i), gbc);
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.fill = GridBagConstraints.BOTH;
			add(getJButtonColor().get(i), gbc);
			gbc.fill = GridBagConstraints.HORIZONTAL;
		}

		gbc.weighty = 1.0D;
		add(new JLabel(), gbc); // Leere Fusszeile

		// Liste der IP-Adressen asynchron ermitteln, um Zeit zu sparen
		new DaemonThread(jCbNetwork, jCobHost, jTxtPort).start();
	}

	/**
	 * This method initializes jLblMaxPlayers.
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLblMaxPlayers() {
		if (jLblMaxPlayers == null) {
			jLblMaxPlayers = new JLabel();
			jLblMaxPlayers.setText("Number of players");
		}
		return jLblMaxPlayers;
	}

	/**
	 * This method initializes jLblMaxPlayersValue.
	 * 
	 * @return javax.swing.JLabel
	 */
	JLabel getJLblMaxPlayersValue() {
		if (jLblMaxPlayersValue == null) {
			jLblMaxPlayersValue = new JLabel();
			jLblMaxPlayersValue.setText(Integer
					.toString(GameSession.gameOptions.getMaxPlayers()));
		}
		return jLblMaxPlayersValue;
	}

	/**
	 * This method initializes jSldMaxPlayers.
	 * 
	 * @return javax.swing.JSlider
	 */
	JSlider getJSldMaxPlayers() {
		if (jSldMaxPlayers == null) {
			jSldMaxPlayers = new JSlider();
			jSldMaxPlayers.setMinimum(1);
			jSldMaxPlayers.setMaximum(4);
			jSldMaxPlayers.setValue(GameSession.gameOptions.getMaxPlayers());
			jSldMaxPlayers.setSnapToTicks(true);
			jSldMaxPlayers.addChangeListener(new ChangeListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
				 */
				@Override
				public void stateChanged(final ChangeEvent event) {
					getJLblMaxPlayersValue().setText(
							Integer.toString(getJSldMaxPlayers().getValue()));
					for (int i = 0; i < getJSldMaxPlayers().getMaximum(); i++) {
						if (i < getJSldMaxPlayers().getValue()) {
							getJButtonColor().get(i).setEnabled(true);
							getJCbImage().get(i).setEnabled(true);
							getJTxtPlayername().get(i).setEnabled(true);
							getJCbComputername().get(i).setEnabled(true);
							getJCbCpu().get(i).setEnabled(
									!getJCbNetwork().isSelected());
						} else {
							getJButtonColor().get(i).setEnabled(false);
							getJCbImage().get(i).setEnabled(false);
							getJTxtPlayername().get(i).setEnabled(false);
							getJCbComputername().get(i).setEnabled(false);
							getJCbCpu().get(i).setEnabled(false);
						}
						getJTxtPlayername().get(i).setVisible(
								!getJCbCpu().get(i).isSelected());
						getJCbComputername().get(i).setVisible(
								getJCbCpu().get(i).isSelected()
										&& !getJCbNetwork().isSelected());
					}
				}
			});
		}
		return jSldMaxPlayers;
	}

	/**
	 * This method initializes jLblPlayerName.
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLblPlayerName() {
		if (jLblPlayerName == null) {
			jLblPlayerName = new JLabel();
			jLblPlayerName.setText("Playername");
		}
		return jLblPlayerName;
	}

	/**
	 * This method initializes jLblCpu.
	 * 
	 * @return javax.swing.JLabel
	 */
	JLabel getJLblCpu() {
		if (jLblCpu == null) {
			jLblCpu = new JLabel();
			jLblCpu.setText("CPU");
		}
		return jLblCpu;
	}

	/**
	 * This method initializes jLblNet.
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLblNet() {
		if (jLblNet == null) {
			jLblNet = new JLabel();
			jLblNet.setText("NET");
		}
		return jLblNet;
	}

	/**
	 * This method initializes jCbNetwork.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	JCheckBox getJCbNetwork() {
		if (jCbNetwork == null) {
			jCbNetwork = new JCheckBox();
			jCbNetwork.setEnabled(false);
			jCbNetwork.setSelected(GameSession.gameOptions.getNetwork() > 0);
			jCbNetwork.addActionListener(new ActionListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				@Override
				public void actionPerformed(final ActionEvent event) {
					getJCobHost().setEnabled(getJCbNetwork().isSelected());
					getJTxtPort().setEnabled(getJCbNetwork().isSelected());
					getJLblCpu().setEnabled(!getJCbNetwork().isSelected());
					for (int i = 0; i < getJSldMaxPlayers().getMaximum(); i++) {
						if (i < getJSldMaxPlayers().getValue()) {
							getJCbCpu().get(i).setEnabled(
									!getJCbNetwork().isSelected());
							if (getJCbNetwork().isSelected()) {
								getJCbCpu().get(i).setSelected(false);
							}
							getJTxtPlayername().get(i).setVisible(
									!getJCbCpu().get(i).isSelected());
							getJCbComputername().get(i).setVisible(
									getJCbCpu().get(i).isSelected()
											&& !getJCbNetwork().isSelected());
						}
					}
				}
			});
		}
		return jCbNetwork;
	}

	/**
	 * This method initializes jCobHost.
	 * 
	 * @return javax.swing.JComboBox
	 */
	JComboBox<String> getJCobHost() {
		if (jCobHost == null) {
			jCobHost = new JComboBox<>();
			jCobHost.setEnabled(false);
			jCobHost.addItem("Wird geladen");
		}
		return jCobHost;
	}

	/**
	 * This method initializes jTxtPort.
	 * 
	 * @return javax.swing.JTextPort
	 */
	JTextField getJTxtPort() {
		if (jTxtPort == null) {
			jTxtPort = new JTextField();
			jTxtPort.setText(Integer.toString(GameSession.gameOptions.getPort()));
			jTxtPort.setEnabled(false);
		}
		return jTxtPort;
	}

	/**
	 * This method initializes jTxtPlayername.
	 * 
	 * @return javax.swing.JTextField[]
	 */
	List<JTextField> getJTxtPlayername() {
		if (jTxtPlayername == null) {
			jTxtPlayername = new ArrayList<>(getJSldMaxPlayers().getMaximum());
			for (int i = 0; i < getJSldMaxPlayers().getMaximum(); i++) {
				final JTextField jTextField = new JTextField();
				if (i < GameSession.gameOptions.getPlayers().size()) {
					jTextField.setText(GameSession.gameOptions.getPlayer(i)
							.getPlayerName());
					jTextField.setEnabled(true);
				} else {
					jTextField.setEnabled(false);
				}
				jTextField.setVisible(!getJCbCpu().get(i).isSelected());
				jTxtPlayername.add(jTextField);
			}
		}
		return jTxtPlayername;
	}

	/**
	 * This method initializes jCbComputername.
	 * 
	 * @return javax.swing.JComboBox[]
	 */
	List<JComboBox<String>> getJCbComputername() {
		if (jCbComputername == null) {
			jCbComputername = new ArrayList<>(getJSldMaxPlayers().getMaximum());
			for (int i = 0; i < getJSldMaxPlayers().getMaximum(); i++) {
				final JComboBox<String> jComboBox = new JComboBox<>();
				final String[] entries = Constants.USERDIR.list();
				Collections.sort(Arrays.asList(entries));
				int computerfiles = 0;
				for (final String entry : entries) {
					if (entry.matches(".*CPU\\.class$")) {
						computerfiles++;
						jComboBox
								.addItem(entry.substring(0, entry.length() - 6));
					}
				}
				if (computerfiles == 0) {
					jComboBox.setEnabled(false);
					getJCbCpu().get(i).setSelected(false);
					getJCbCpu().get(i).setEnabled(false);
				}

				if (i < GameSession.gameOptions.getPlayers().size()) {
					getJTxtPlayername().get(i).setVisible(
							!getJCbCpu().get(i).isSelected());
					jComboBox.setEnabled(true);
				} else {
					jComboBox.setEnabled(false);
				}
				jComboBox.setVisible(getJCbCpu().get(i).isSelected()
						&& !getJCbNetwork().isSelected());
				jCbComputername.add(jComboBox);
			}
		}
		return jCbComputername;
	}

	/**
	 * This method initializes jCbCpu.
	 * 
	 * @return javax.swing.JCheckBox[]
	 */
	List<JCheckBox> getJCbCpu() {
		if (jCbCpu == null) {
			jCbCpu = new ArrayList<>(getJSldMaxPlayers().getMaximum());
			for (int i = 0; i < getJSldMaxPlayers().getMaximum(); i++) {
				final JCheckBox jCheckBox = new JCheckBox();
				if (i < GameSession.gameOptions.getPlayers().size()) {
					jCheckBox.setSelected(GameSession.gameOptions.getPlayer(i)
							.isComputer());
					jCheckBox.setEnabled(!getJCbNetwork().isSelected());
				} else {
					jCheckBox.setEnabled(false);
				}
				jCheckBox.addActionListener(new ActionListener() {

					/**
					 * {@inheritDoc}
					 * 
					 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
					 */
					@Override
					public void actionPerformed(final ActionEvent event) {
						for (int j = 0; j < getJSldMaxPlayers().getMaximum(); j++) {
							if (j < getJSldMaxPlayers().getValue()) {
								getJTxtPlayername().get(j).setVisible(
										!getJCbCpu().get(j).isSelected());
								getJCbComputername().get(j).setVisible(
										getJCbCpu().get(j).isSelected()
												&& !getJCbNetwork()
														.isSelected());
							}
						}
					}
				});
				jCbCpu.add(jCheckBox);
			}
		}
		return jCbCpu;
	}

	/**
	 * This method initializes jButtonColor.
	 * 
	 * @return javax.swing.JButton[]
	 */
	List<JButton> getJButtonColor() {
		if (jButtonColor == null) {
			jButtonColor = new ArrayList<>(getJSldMaxPlayers().getMaximum());
			for (int i = 0; i < getJSldMaxPlayers().getMaximum(); i++) {
				final JButton jButton = new JButton();
				jButton.addActionListener(new ActionListener() {

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
						if (newColor != null) {
							comp.setBackground(newColor);
						}
					}
				});
				if (i < GameSession.gameOptions.getPlayers().size()) {
					jButton.setBackground(new Color(GameSession.gameOptions
							.getPlayer(i).getPlayerColor()));
					jButton.setEnabled(true);
				} else {
					jButton.setEnabled(false);
				}
				jButtonColor.add(jButton);
			}
		}
		return jButtonColor;
	}

	/**
	 * This method initializes jCbImage.
	 * 
	 * @return javax.swing.JComboBox[]
	 */
	List<JComboBox<String>> getJCbImage() {
		if (jCbImage == null) {
			jCbImage = new ArrayList<>(getJSldMaxPlayers().getMaximum());
			final GameOptions go = GameSession.gameOptions;
			for (int i = 0; i < getJSldMaxPlayers().getMaximum(); i++) {
				final JComboBox<String> jComboBox = new JComboBox<>();
				final String[] entries = Constants.USERDIR.list();
				Collections.sort(Arrays.asList(entries));
				jComboBox.addItem("");
				int imagefiles = 0;
				for (final String entry : entries) {
					if (entry.toLowerCase().endsWith(".gif")
							|| entry.toLowerCase().endsWith(".jpg")
							|| entry.toLowerCase().endsWith(".png")) {
						imagefiles++;
						jComboBox.addItem(entry);
						if (i < go.getPlayers().size()
								&& entry.equalsIgnoreCase(go.getPlayer(i)
										.getPlayerImageName())) {
							jComboBox.setSelectedIndex(imagefiles);
						}
					}
				}
				if (imagefiles == 0) {
					jComboBox.setEnabled(false);
				}
				if (i < go.getPlayers().size()) {
					jComboBox.setEnabled(true);
				} else {
					jComboBox.setEnabled(false);
				}
				jCbImage.add(jComboBox);
			}
		}
		return jCbImage;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see swing.gui.options.AbstractOptDlgPnl#updateOptions()
	 */
	@Override
	protected void updateOptions() {
		final GameOptions go = GameSession.gameOptions;
		go.setMaxPlayers(getJSldMaxPlayers().getValue());
		for (int i = 0; i < getJSldMaxPlayers().getValue(); i++) {
			String playername = getJTxtPlayername().get(i).getText();
			if (getJCbCpu().get(i).isSelected()) {
				playername = (String) getJCbComputername().get(i)
						.getSelectedItem();
			}
			go.getPlayers().add(
					i,
					new Player(i, playername, getJButtonColor().get(i)
							.getBackground().getRGB(), getJCbCpu().get(i)
							.isSelected()));
			go.getPlayer(i).setPlayerImageName(
					getJCbImage().get(i).getSelectedItem().toString());
			go.getPlayer(i).setPlayerImage(
					AnimatedImageUtil.createMyImage(getJCbImage().get(i)
							.getSelectedItem().toString(), getJButtonColor()
							.get(i).getBackground()));
			if (getJCbNetwork().isSelected()) {
				// Initialize network-player as inaktive, because
				// they will be activated by connection to the server
				go.getPlayer(i).setActive(false);
			}
		}

		go.setNetwork(getJCbNetwork().isSelected() ? 1 : 0);
		go.setHost((String) getJCobHost().getSelectedItem());
		if (getJTxtPort().getText() != null
				&& getJTxtPort().getText().trim().length() > 0) {
			go.setPort(Integer.parseInt(getJTxtPort().getText()));
		}
		go.setNetwork(getJCbNetwork().isSelected() ? 1 : 0);
	}
}

/**
 * @author Administrator
 * 
 */
class DaemonThread extends Thread {
	private final JComboBox<String> jCobHost;
	private final JCheckBox jCbNetwork;
	private final JTextField jTxtPort;

	/**
	 * @param jCbNetwork
	 *            JCheckBox
	 * @param jCobHost
	 *            JComboBox
	 * @param jTxtPort
	 *            JTextField
	 */
	protected DaemonThread(final JCheckBox jCbNetwork,
			final JComboBox<String> jCobHost, final JTextField jTxtPort) {
		super();
		this.jCbNetwork = jCbNetwork;
		this.jCobHost = jCobHost;
		this.jTxtPort = jTxtPort;
		setDaemon(true);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		InetAddress[] inetAdresses = null;
		try {
			inetAdresses = InetAddress.getAllByName(InetAddress.getLocalHost()
					.getHostName());
		} catch (final UnknownHostException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
		}
		if (inetAdresses != null) {
			jCobHost.removeAllItems();
			for (final InetAddress inetAdress : inetAdresses) {
				jCobHost.addItem(inetAdress.getHostAddress());
			}
			jCbNetwork.setEnabled(true);
			jCobHost.setEnabled(true);
			jTxtPort.setEnabled(true);
		}
	}
}