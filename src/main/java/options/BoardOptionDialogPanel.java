package options;

import game.GameSession;

import java.util.Arrays;
import java.util.Collections;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Constants;

/**
 * @author Administrator
 * 
 */
public class BoardOptionDialogPanel extends AbstractOptDlgPnl {

	private static final long serialVersionUID = 1L;
	private JLabel jLblBoardLayout = null;
	private JButton jButtonBoardLayout = null;
	private JLabel jLblFieldForm = null;
	private JComboBox<String> jCbFieldName = null;
	private JCheckBox jCbBorderless = null;
	private JLabel jLblFieldAlpha = null;
	private JSlider jSldFieldAlpha = null;
	private JLabel jLblFieldAlphaValue = null;
	private JLabel jLblMaxOverload = null;
	private JSlider jSldMaxOverload = null;
	private JLabel jLblMaxOverloadValue = null;
	private BoardLayoutOptDlgPnl optDlgPnlBoardLayout;

	/**
	 * @param parentDialog
	 *            JDialog
	 * @param name
	 *            String
	 */
	protected BoardOptionDialogPanel(final JDialog parentDialog,
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

		gbc.gridwidth = 2;
		add(getJLblFieldForm(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJLblBoardLayout(), gbc);

		gbc.gridwidth = 1;
		add(getJCbFieldName(), gbc);
		gbc.gridwidth = 1;
		add(new JLabel(), gbc);
		gbc.gridwidth = 1;
		add(getJButtonBoardLayout(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJCbBorderless(), gbc);

		gbc.gridwidth = 2;
		add(getJLblMaxOverload(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJLblFieldAlpha(), gbc);

		gbc.gridwidth = 1;
		add(getJSldMaxOverload(), gbc);
		gbc.gridwidth = 1;
		add(getJLblMaxOverloadValue(), gbc);
		gbc.gridwidth = 1;
		add(getJSldFieldAlpha(), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(getJLblFieldAlphaValue(), gbc);

		gbc.weighty = 1.0D;
		add(new JLabel(), gbc); // Leere Fusszeile
	}

	/**
	 * This method initializes jLblBoardLayout.
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLblBoardLayout() {
		if (jLblBoardLayout == null) {
			jLblBoardLayout = new JLabel();
			jLblBoardLayout.setText("Board-Layout");
		}
		return jLblBoardLayout;
	}

	/**
	 * This method initializes jButtonBoardLayout.
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonBoardLayout() {
		if (jButtonBoardLayout == null) {
			jButtonBoardLayout = new JButton();
			jButtonBoardLayout.setText("Edit");
			jButtonBoardLayout.addActionListener(new ActionListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				@Override
				public void actionPerformed(final ActionEvent event) {
					getOptDlgPnlBoardLayout().reset();
					getOptDlgPnlBoardLayout().setVisible(true);
				}
			});
		}
		return jButtonBoardLayout;
	}

	/**
	 * This method initializes optDlgPnlBoardLayout.
	 * 
	 * @return OptDlgPnlBoardLayoutS
	 */
	BoardLayoutOptDlgPnl getOptDlgPnlBoardLayout() {
		if (optDlgPnlBoardLayout == null) {
			optDlgPnlBoardLayout = new BoardLayoutOptDlgPnl(this);
		}
		return optDlgPnlBoardLayout;
	}

	/**
	 * This method initializes jLblFieldForm.
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLblFieldForm() {
		if (jLblFieldForm == null) {
			jLblFieldForm = new JLabel();
			jLblFieldForm.setText("Fieldform");
		}
		return jLblFieldForm;
	}

	/**
	 * This method initializes jCbFieldName.
	 * 
	 * @return javax.swing.JComboBox
	 */
	JComboBox<String> getJCbFieldName() {
		if (jCbFieldName == null) {
			jCbFieldName = new JComboBox<>();
			final String[] entries = Constants.USERDIR.list();
			Collections.sort(Arrays.asList(entries));
			for (final String entry : entries) {
				if (entry.matches(".*\\.field$")) {
					jCbFieldName
							.addItem(entry.substring(0, entry.length() - 6));
				}
			}
			jCbFieldName.setEnabled(jCbFieldName.getItemCount() > 0);
		}
		return jCbFieldName;
	}

	/**
	 * This method initializes jCbBorderless.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	JCheckBox getJCbBorderless() {
		if (jCbBorderless == null) {
			jCbBorderless = new JCheckBox();
			jCbBorderless.setText("Borderless");
			jCbBorderless
					.setToolTipText("If activated overloads at borders will continue on the other side of the board");
			jCbBorderless.setSelected(GameSession.gameOptions.isBorderless());
		}
		return jCbBorderless;
	}

	/**
	 * This method initializes jLblFieldAlpha.
	 * 
	 * @return javax.swing.JLabel
	 */
	JLabel getJLblFieldAlpha() {
		if (jLblFieldAlpha == null) {
			jLblFieldAlpha = new JLabel();
			jLblFieldAlpha.setToolTipText("The transparency of the fields");
			jLblFieldAlpha.setText("Field-Transparency");
		}
		return jLblFieldAlpha;
	}

	/**
	 * This method initializes jSldFieldAlpha.
	 * 
	 * @return javax.swing.JSlider
	 */
	JSlider getJSldFieldAlpha() {
		if (jSldFieldAlpha == null) {
			jSldFieldAlpha = new JSlider();
			jSldFieldAlpha.setMinimum(0);
			jSldFieldAlpha.setMaximum(100);
			jSldFieldAlpha.setValue((int) (GameSession.gameOptions
					.getFieldAlpha() * 100.0F));
			jSldFieldAlpha.setSnapToTicks(true);
			jSldFieldAlpha.addChangeListener(new ChangeListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
				 */
				@Override
				public void stateChanged(final ChangeEvent event) {
					getJLblFieldAlphaValue().setText(
							getJSldFieldAlpha().getValue() + "%");
					if (getJSldFieldAlpha().getValue() < 100) {
						getJSldMaxOverload().setValue(0);
						getJSldMaxOverload().setEnabled(false);
						getJLblMaxOverload().setEnabled(false);
						getJLblMaxOverloadValue().setEnabled(false);
					} else {
						getJSldMaxOverload().setEnabled(true);
						getJLblMaxOverload().setEnabled(true);
						getJLblMaxOverloadValue().setEnabled(true);
					}
				}
			});
		}
		return jSldFieldAlpha;
	}

	/**
	 * This method initializes jLblFieldAlphaValue.
	 * 
	 * @return javax.swing.JLabel
	 */
	JLabel getJLblFieldAlphaValue() {
		if (jLblFieldAlphaValue == null) {
			jLblFieldAlphaValue = new JLabel();
			jLblFieldAlphaValue.setText((int) (GameSession.gameOptions
					.getFieldAlpha() * 100.0F) + "%");
		}
		return jLblFieldAlphaValue;
	}

	/**
	 * This method initializes jLblMaxOverload.
	 * 
	 * @return javax.swing.JLabel
	 */
	JLabel getJLblMaxOverload() {
		if (jLblMaxOverload == null) {
			jLblMaxOverload = new JLabel();
			jLblMaxOverload
					.setToolTipText("The amount of how many overloads a field can take until it is vanished");
			jLblMaxOverload.setText("Field-Integrity");
		}
		return jLblMaxOverload;
	}

	/**
	 * This method initializes jSldMaxOverload.
	 * 
	 * @return javax.swing.JSlider
	 */
	JSlider getJSldMaxOverload() {
		if (jSldMaxOverload == null) {
			jSldMaxOverload = new JSlider();
			jSldMaxOverload.setMinimum(0);
			jSldMaxOverload.setMaximum(10);
			jSldMaxOverload.setValue(GameSession.gameOptions.getMaxOverload());
			jSldMaxOverload.setSnapToTicks(true);
			jSldMaxOverload.addChangeListener(new ChangeListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
				 */
				@Override
				public void stateChanged(final ChangeEvent event) {
					getJLblMaxOverloadValue().setText(
							Integer.toString(getJSldMaxOverload().getValue()));
					if (getJSldMaxOverload().getValue() > 0) {
						getJSldFieldAlpha().setValue(100);
						getJSldFieldAlpha().setEnabled(false);
						getJLblFieldAlpha().setEnabled(false);
						getJLblFieldAlphaValue().setEnabled(false);
					} else {
						getJSldFieldAlpha().setEnabled(true);
						getJLblFieldAlpha().setEnabled(true);
						getJLblFieldAlphaValue().setEnabled(true);
					}
				}
			});
		}
		return jSldMaxOverload;
	}

	/**
	 * This method initializes jLblMaxOverloadValue.
	 * 
	 * @return javax.swing.JLabel
	 */
	JLabel getJLblMaxOverloadValue() {
		if (jLblMaxOverloadValue == null) {
			jLblMaxOverloadValue = new JLabel();
			jLblMaxOverloadValue.setText(String.valueOf(GameSession.gameOptions
					.getMaxOverload()));
		}
		return jLblMaxOverloadValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see options.AbstractOptDlgPnl#updateOptions()
	 */
	@Override
	protected void updateOptions() {
		if (optDlgPnlBoardLayout != null) {
			optDlgPnlBoardLayout.updateOptions();
		}

		GameSession.gameOptions.setFieldName((String) getJCbFieldName()
				.getSelectedItem());
		GameSession.gameOptions
				.setFieldAlpha(getJSldFieldAlpha().getValue() / 100.0F);
		GameSession.gameOptions.setMaxOverload(getJSldMaxOverload().getValue());
		GameSession.gameOptions.setBorderless(getJCbBorderless().isSelected());
	}
}
