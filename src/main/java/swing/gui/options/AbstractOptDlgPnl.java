package swing.gui.options;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * @author Administrator
 * 
 */
public abstract class AbstractOptDlgPnl extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * @param parentDialog
	 *            JDialog
	 * @param name
	 *            String
	 */
	protected AbstractOptDlgPnl(final JDialog parentDialog, final String name) {
		super();

		setSize(parentDialog.getSize());
		setPreferredSize(getSize());
		final GridBagLayout layout = new GridBagLayout();
		layout.preferredLayoutSize(this);
		setLayout(layout);
		setName(name);
	}

	/**
	 * This initialize MUST be called from outside. Otherwise all instanciated
	 * Components are reseted to null when the constructor of sub-classes is
	 * called!!!
	 * 
	 * @TODO Check out why and implement better solution
	 */
	protected abstract void initialize();

	/**
	 * @return GridBagConstraints
	 */
	protected final GridBagConstraints getInitGridBagConstraints() {
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0D;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 5, 0, 5);

		return gbc;
	}

	/**
	 *
	 */
	protected abstract void updateOptions();
}
