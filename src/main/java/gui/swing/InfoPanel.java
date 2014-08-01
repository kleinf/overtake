package gui.swing;

import game.GameSession;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.ModeEnum;

/**
 * @author Administrator
 * 
 */
public class InfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private final GamePanel gamePanel;
	private JLabel jLblCurrPlayer;
	private JLabel jLblCurrPlayerValue;
	private JLabel jLblRound;
	private JLabel jLblRoundValue;
	private JLabel jLblFree;
	private JLabel jLblFreeValue;
	private JLabel[] jLblCounter;
	private JLabel[] jLblCounterValue;
	private JButton jButtonRepair;
	private JButton jButtonRepairStop;

	/**
	 * @param gamePanel
	 *            GamePanel
	 * @param width
	 *            int
	 * @param height
	 *            int
	 */
	protected InfoPanel(final GamePanel gamePanel, final int width,
			final int height) {
		super();
		setSize(width, height);
		setPreferredSize(getSize());

		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		this.gamePanel = gamePanel;

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0D;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridwidth = 1;
		add(getJLblCurrPlayer(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJLblCurrPlayerValue(), gbc);

		gbc.gridwidth = 1;
		add(getJLblRound(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJLblRoundValue(), gbc);

		gbc.gridwidth = 1;
		add(getJLblFree(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJLblFreeValue(), gbc);

		for (int i = 0; i < GameSession.gameOptions.getMaxPlayers(); i++) {
			gbc.gridwidth = 1;
			add(getJLblCounter()[i], gbc);
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			add(getJLblCounterValue()[i], gbc);
		}

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJButtonRepair(), gbc);
		getJButtonRepair().setVisible(false);
		getJButtonRepair().addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				repair();
			}
		});

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJButtonRepairStop(), gbc);
		getJButtonRepairStop().setVisible(false);
		getJButtonRepairStop().addActionListener(new ActionListener() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				repairStop();
			}
		});

		gbc.weighty = 1.0D;
		add(new JLabel(), gbc); // Leere Fusszeile

		refresh();
	}

	private JLabel getJLblCurrPlayer() {
		if (jLblCurrPlayer == null) {
			jLblCurrPlayer = new JLabel();
			jLblCurrPlayer.setText("Aktiver Spieler:");
		}
		return jLblCurrPlayer;
	}

	private JLabel getJLblCurrPlayerValue() {
		if (jLblCurrPlayerValue == null) {
			jLblCurrPlayerValue = new JLabel();
			jLblCurrPlayerValue.setText("");
		}
		return jLblCurrPlayerValue;
	}

	private JLabel getJLblRound() {
		if (jLblRound == null) {
			jLblRound = new JLabel();
			jLblRound.setText("Runde:");
		}
		return jLblRound;
	}

	private JLabel getJLblRoundValue() {
		if (jLblRoundValue == null) {
			jLblRoundValue = new JLabel();
			jLblRoundValue.setText("");
		}
		return jLblRoundValue;
	}

	private JLabel getJLblFree() {
		if (jLblFree == null) {
			jLblFree = new JLabel();
			jLblFree.setText("Anzahl Frei:");
		}
		return jLblFree;
	}

	private JLabel getJLblFreeValue() {
		if (jLblFreeValue == null) {
			jLblFreeValue = new JLabel();
			jLblFreeValue.setText("");
		}
		return jLblFreeValue;
	}

	private JLabel[] getJLblCounter() {
		if (jLblCounter == null) {
			jLblCounter = new JLabel[GameSession.gameOptions.getMaxPlayers()];
			for (int i = 0; i < GameSession.gameOptions.getMaxPlayers(); i++) {
				jLblCounter[i] = new JLabel();
				if (GameSession.gameOptions.getNetwork() > 0
						&& !gamePanel.getPlayer(i).isActive()) {
					jLblCounter[i] = new JLabel("<Nicht angemeldet>");
				} else {
					jLblCounter[i] = new JLabel("Anzahl "
							+ gamePanel.getPlayer(i).getPlayerName() + ":");
				}
			}
		}
		return jLblCounter;
	}

	private JLabel[] getJLblCounterValue() {
		if (jLblCounterValue == null) {
			jLblCounterValue = new JLabel[GameSession.gameOptions
					.getMaxPlayers()];
			for (int i = 0; i < GameSession.gameOptions.getMaxPlayers(); i++) {
				jLblCounterValue[i] = new JLabel();
				jLblCounterValue[i] = new JLabel("");
			}
		}
		return jLblCounterValue;
	}

	private JButton getJButtonRepair() {
		if (jButtonRepair == null) {
			jButtonRepair = new JButton();
			jButtonRepair.setText("");
		}
		return jButtonRepair;
	}

	private JButton getJButtonRepairStop() {
		if (jButtonRepairStop == null) {
			jButtonRepairStop = new JButton();
			jButtonRepairStop.setText("");
		}
		return jButtonRepairStop;
	}

	/**
	 * @param playerId
	 *            int
	 */
	protected void refreshNetPlayer(final int playerId) {
		if (gamePanel.getPlayer(playerId).isActive()) {
			jLblCounter[playerId].setText("Anzahl "
					+ gamePanel.getPlayer(playerId).getPlayerName() + ":");
		} else {
			jLblCounter[playerId].setText("<Nicht angemeldet>");
		}
	}

	/**
	 * 
	 */
	protected final void refresh() {
		getJLblCurrPlayerValue().setText(
				gamePanel.getCurrPlayer().getPlayerName());
		getJLblRoundValue().setText(Integer.toString(gamePanel.getCurrRound()));
		getJLblFreeValue().setText(
				gamePanel.getBoardPanel().getSumFieldsFree()
						+ " ("
						+ Math.rint(gamePanel.getBoardPanel()
								.getSumFieldsFree()
								* 100.0D
								/ gamePanel.getBoardPanel().getSumFields())
						+ "%)");
		for (int i = 0; i < GameSession.gameOptions.getMaxPlayers(); i++) {
			getJLblCounterValue()[i].setText(gamePanel.getPlayer(i)
					.getNumFields()
					+ " ("
					+ Math.rint(gamePanel.getPlayer(i).getNumFields() * 100.0D
							/ gamePanel.getBoardPanel().getSumFields()) + "%)");
		}
		if (gamePanel.getCurrPlayer().getRepairPoints() > 0
				&& !gamePanel.getCurrPlayer().isComputer()) {
			getJButtonRepair().setText(
					"Repair (" + gamePanel.getCurrPlayer().getRepairPoints()
							+ ")");
			getJButtonRepairStop()
					.setText(
							"Stop Repair ("
									+ gamePanel.getCurrPlayer()
											.getRepairPoints() + ")");
			if (gamePanel.isPlayMode()) {
				getJButtonRepair().setEnabled(gamePanel.isMyTurn());
				getJButtonRepair().setVisible(true);
			}
		} else {
			getJButtonRepair().setVisible(false);
			getJButtonRepairStop().setVisible(false);
		}
	}

	void repair() {
		getJButtonRepair().setVisible(false);
		getJButtonRepairStop().setEnabled(gamePanel.isMyTurn());
		getJButtonRepairStop().setVisible(true);
		gamePanel.setMode(ModeEnum.MODE_REPAIR);
	}

	void repairStop() {
		getJButtonRepair().setVisible(true);
		getJButtonRepairStop().setVisible(false);
		gamePanel.setMode(ModeEnum.MODE_PLAY);
	}
}