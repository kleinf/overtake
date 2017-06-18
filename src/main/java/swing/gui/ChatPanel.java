package swing.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import game.Player;
import net.Net;
import util.Constants;

/**
 * @author Administrator
 * 
 */
public class ChatPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTextArea jTextArea = null;
	private JComboBox<Player> jComboBox = null;
	private JTextField jTextField = null;
	private final GameFrame parentFrame;

	/**
	 * @param parentFrame
	 *            GameFrame
	 */
	public ChatPanel(final GameFrame parentFrame) {
		super();
		this.parentFrame = parentFrame;
		setLayout(new BorderLayout());
		add(getJScrollPane(), BorderLayout.CENTER);
		add(getJTextField(), BorderLayout.SOUTH);
		add(getJComboBox(), BorderLayout.NORTH);
	}

	/**
	 * This method initializes jScrollPane.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private final JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTextArea());
			jScrollPane.setAutoscrolls(true);
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTextArea.
	 * 
	 * @return javax.swing.JTextArea
	 */
	private final JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setEnabled(false);
		}
		return jTextArea;
	}

	/**
	 * This method initializes jComboBox.
	 * 
	 * @return javax.swing.JComboBox
	 */
	public final JComboBox<Player> getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox<>();
		}
		return jComboBox;
	}

	/**
	 * This method initializes jTextField.
	 * 
	 * @return javax.swing.JTextField
	 */
	protected final JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.addKeyListener(new KeyAdapter() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
				 */
				@Override
				public void keyReleased(final KeyEvent event) {
					if (event.getKeyCode() == KeyEvent.VK_ENTER) {
						getGameFrame().send(((Player) getJComboBox().getSelectedItem()).getPlayerId(),
								Net.CHAT.name() + Constants.NET_DIVIDER + getGameFrame().getEigenerName() + ": "
										+ getJTextField().getText());
						getJTextField().setText("");
					}
				}
			});
		}
		return jTextField;
	}

	/**
	 * @param message
	 *            String Chat-Message to append
	 */
	public void appendChatMessage(final String message) {
		jTextArea.append(message);
	}

	/**
	 * @return GameFrame
	 */
	protected GameFrame getGameFrame() {
		return parentFrame;
	}
}
