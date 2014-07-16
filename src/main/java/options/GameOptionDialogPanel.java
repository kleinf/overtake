package options;

import game.GameGoal;
import game.GameSession;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

/**
 * @author Administrator
 * 
 */
public class GameOptionDialogPanel extends AbstractOptDlgPnl {

	private static final long serialVersionUID = 1L;
	private JLabel jLblStart = null;
	private JRadioButton jRbEmpty = null;
	private JRadioButton jRbRandom = null;
	private JLabel jLblGoal = null;
	private JRadioButton jRbLastmanstanding = null;
	private JRadioButton jRbDivideetimpera = null;
	private JRadioButton jRbDomination = null;
	private JLabel jLblGamemode = null;
	private JRadioButton jRbFree = null;
	private JRadioButton jRbSticky = null;
	private JLabel jLblSpecials = null;
	private JCheckBox jCbSetwhilefree = null;
	private JCheckBox jCbOverloadonequal = null;
	private JCheckBox jCbEmptyoverloaded = null;
	private JCheckBox jCbLooseoverloaded = null;

	/**
	 * @param parentDialog
	 *            JDialog
	 * @param name
	 *            String
	 */
	protected GameOptionDialogPanel(final JDialog parentDialog,
			final String name) {
		super(parentDialog, name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see options.AbstractOptDlgPnl#initialize()
	 */
	@Override
	protected void initialize() {

		final GridBagConstraints gbc = getInitGridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(new JLabel(" "), gbc); // Leere Kopfzeile

		gbc.gridwidth = 1;
		add(getJLblStart(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJLblGamemode(), gbc);

		final ButtonGroup starts = new ButtonGroup();
		starts.add(getJRbEmpty());
		starts.add(getJRbRandom());

		final ButtonGroup gameModes = new ButtonGroup();
		gameModes.add(getJRbFree());
		gameModes.add(getJRbSticky());

		gbc.gridwidth = 1;
		add(getJRbEmpty(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJRbFree(), gbc);

		gbc.gridwidth = 1;
		add(getJRbRandom(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJRbSticky(), gbc);

		gbc.gridwidth = 1;
		add(getJLblGoal(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJLblSpecials(), gbc);

		final ButtonGroup goals = new ButtonGroup();
		goals.add(getJRbLastmanstanding());
		goals.add(getJRbDivideetimpera());
		goals.add(getJRbDomination());

		gbc.gridwidth = 1;
		add(getJRbLastmanstanding(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJCbSetWhileFree(), gbc);

		gbc.gridwidth = 1;
		add(getJRbDivideetimpera(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJCbOverloadonequal(), gbc);

		gbc.gridwidth = 1;
		add(getJRbDomination(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJCbEmptyoverloaded(), gbc);

		gbc.gridx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJCbLooseoverloaded(), gbc);

		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.weighty = 1.0D;
		add(new JLabel(), gbc); // Leere Fusszeile
	}

	/**
	 * This method initializes jLblStart.
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLblStart() {
		if (jLblStart == null) {
			jLblStart = new JLabel();
			jLblStart.setText("Start");
		}
		return jLblStart;
	}

	/**
	 * This method initializes jLblGoal.
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLblGoal() {
		if (jLblGoal == null) {
			jLblGoal = new JLabel();
			jLblGoal.setText("Goal");
		}
		return jLblGoal;
	}

	/**
	 * This method initializes jLblSpecials.
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLblSpecials() {
		if (jLblSpecials == null) {
			jLblSpecials = new JLabel();
			jLblSpecials.setText("Specials");
		}
		return jLblSpecials;
	}

	/**
	 * This method initializes jRbEmpty.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRbEmpty() {
		if (jRbEmpty == null) {
			jRbEmpty = new JRadioButton();
			jRbEmpty.setText("Empty");
			jRbEmpty.setToolTipText("Start with a empty board");
			jRbEmpty.setSelected(true);
		}
		return jRbEmpty;
	}

	/**
	 * This method initializes jRbRandom.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRbRandom() {
		if (jRbRandom == null) {
			jRbRandom = new JRadioButton();
			jRbRandom.setText("Random");
			jRbRandom.setToolTipText("Start with a random filled board");
			jRbRandom.setEnabled(false);
		}
		return jRbRandom;
	}

	/**
	 * This method initializes jRbLastmanstanding.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	JRadioButton getJRbLastmanstanding() {
		if (jRbLastmanstanding == null) {
			jRbLastmanstanding = new JRadioButton();
			jRbLastmanstanding.setText("Last man standing");
			jRbLastmanstanding
					.setToolTipText("The game ends as soon as one player remains - even if there are free fields to be overtaken");
			jRbLastmanstanding.setSelected(GameSession.gameOptions
					.getGameGoal() == GameGoal.LAST_MAN_STANDING);
			jRbLastmanstanding.addActionListener(new ActionListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				@Override
				public void actionPerformed(final ActionEvent event) {
					if (getJRbLastmanstanding().isSelected()) {
						getJCbSetWhileFree().setSelected(false);
						getJCbSetWhileFree().setEnabled(false);
						getJCbLooseoverloaded().setEnabled(true);
					}
				}
			});
		}
		return jRbLastmanstanding;
	}

	/**
	 * This method initializes jRbDivideetimpera.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	JRadioButton getJRbDivideetimpera() {
		if (jRbDivideetimpera == null) {
			jRbDivideetimpera = new JRadioButton();
			jRbDivideetimpera.setText("Divide et impera");
			jRbDivideetimpera
					.setToolTipText("The game ends as soon as one player conquers the biggest proportion e.g. more than 50% with two players, more than 25% with four players and so on");
			jRbDivideetimpera
					.setSelected(GameSession.gameOptions.getGameGoal() == GameGoal.DIVIDE_ET_IMPERA);
			jRbDivideetimpera.addActionListener(new ActionListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				@Override
				public void actionPerformed(final ActionEvent event) {
					if (getJRbDivideetimpera().isSelected()) {
						getJCbSetWhileFree().setEnabled(true);
						getJCbLooseoverloaded().setEnabled(true);
					}
				}
			});
		}
		return jRbDivideetimpera;
	}

	/**
	 * This method initializes jRbDomination.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	JRadioButton getJRbDomination() {
		if (jRbDomination == null) {
			jRbDomination = new JRadioButton();
			jRbDomination.setText("Domination");
			jRbDomination
					.setToolTipText("The game ends if one player conquers all fields - players can rejoin as long as there are free fields");
			jRbDomination
					.setSelected(GameSession.gameOptions.getGameGoal() == GameGoal.DOMINATION);
			jRbDomination.addActionListener(new ActionListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				@Override
				public void actionPerformed(final ActionEvent event) {
					if (getJRbDomination().isSelected()) {
						getJCbSetWhileFree().setSelected(true);
						getJCbSetWhileFree().setEnabled(false);
						getJCbLooseoverloaded().setSelected(false);
						getJCbLooseoverloaded().setEnabled(false);
					}
				}
			});
		}
		return jRbDomination;
	}

	/**
	 * This method initializes jLblGamemode.
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLblGamemode() {
		if (jLblGamemode == null) {
			jLblGamemode = new JLabel();
			jLblGamemode.setText("Gamemode");
		}
		return jLblGamemode;
	}

	/**
	 * This method initializes jRbFree.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRbFree() {
		if (jRbFree == null) {
			jRbFree = new JRadioButton();
			jRbFree.setText("Free");
			jRbFree.setToolTipText("If activated you can choose any field on the board except the ones of your opponents");
			jRbFree.setSelected(!GameSession.gameOptions.isSticky());
		}
		return jRbFree;
	}

	/**
	 * This method initializes jRbSticky.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRbSticky() {
		if (jRbSticky == null) {
			jRbSticky = new JRadioButton();
			jRbSticky.setText("Sticky");
			jRbSticky
					.setToolTipText("If activated you can choose only new fields next to another of your fields");
			jRbSticky.setSelected(GameSession.gameOptions.isSticky());
		}
		return jRbSticky;
	}

	/**
	 * This method initializes jCbSetwhilefree.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	JCheckBox getJCbSetWhileFree() {
		if (jCbSetwhilefree == null) {
			jCbSetwhilefree = new JCheckBox();
			jCbSetwhilefree.setText("Set while free");
			jCbSetwhilefree
					.setToolTipText("If activated players can rejoin as long as there are free fields");
			jCbSetwhilefree.setSelected(GameSession.gameOptions
					.isSetWhileFree());
			if (getJRbLastmanstanding().isSelected()
					|| getJRbDomination().isSelected()) {
				jCbSetwhilefree.setEnabled(false);
			}
		}
		return jCbSetwhilefree;
	}

	/**
	 * This method initializes jCbOverloadonequal.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCbOverloadonequal() {
		if (jCbOverloadonequal == null) {
			jCbOverloadonequal = new JCheckBox();
			jCbOverloadonequal.setText("Overload on equal");
			jCbOverloadonequal
					.setToolTipText("If activated fields will overload if the amount is equal to the number of neighbours, otherwise the amount of the fields needs to be greater");
			jCbOverloadonequal.setSelected(GameSession.gameOptions
					.isOverloadOnEqual());
		}
		return jCbOverloadonequal;
	}

	/**
	 * This method initializes jCbEmptyoverloaded.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCbEmptyoverloaded() {
		if (jCbEmptyoverloaded == null) {
			jCbEmptyoverloaded = new JCheckBox();
			jCbEmptyoverloaded.setText("Empty overloaded");
			jCbEmptyoverloaded
					.setToolTipText("If activated overloaded fields will be set to zero on overload, otherwise only the number of neighbours will be subtracted");
			jCbEmptyoverloaded.setSelected(GameSession.gameOptions
					.isEmptyOverloaded());
		}
		return jCbEmptyoverloaded;
	}

	/**
	 * This method initializes jCbLooseoverloaded.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	JCheckBox getJCbLooseoverloaded() {
		if (jCbLooseoverloaded == null) {
			jCbLooseoverloaded = new JCheckBox();
			jCbLooseoverloaded.setText("Loose overloaded");
			jCbLooseoverloaded
					.setToolTipText("If activated overloaded fields will loose their owners - don't use with \"Set while free\"");
			jCbLooseoverloaded.setSelected(GameSession.gameOptions
					.isLooseOverloaded());
		}
		return jCbLooseoverloaded;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see options.AbstractOptDlgPnl#updateOptions()
	 */
	@Override
	protected void updateOptions() {
		if (getJRbLastmanstanding().isSelected()) {
			GameSession.gameOptions.setGameGoal(GameGoal.LAST_MAN_STANDING);
		} else if (getJRbDivideetimpera().isSelected()) {
			GameSession.gameOptions.setGameGoal(GameGoal.DIVIDE_ET_IMPERA);
		} else if (getJRbDomination().isSelected()) {
			GameSession.gameOptions.setGameGoal(GameGoal.DOMINATION);
		}

		GameSession.gameOptions.setSticky(getJRbSticky().isSelected());
		GameSession.gameOptions.setSetWhileFree(getJCbSetWhileFree()
				.isSelected());
		GameSession.gameOptions.setOverloadOnEqual(getJCbOverloadonequal()
				.isSelected());
		GameSession.gameOptions.setEmptyOverloaded(getJCbEmptyoverloaded()
				.isSelected());
		GameSession.gameOptions.setLooseOverloaded(getJCbLooseoverloaded()
				.isSelected());
	}
}
