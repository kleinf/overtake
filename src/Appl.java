import gui.GameFrame;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import util.Constants;
import util.Logger;

/**
 * @author Administrator
 * 
 */
public final class Appl {

	private static final boolean PACK_FRAME = false;

	// Constructor
	private Appl() {
	}

	/**
	 * 
	 */
	private void start() {
		final GameFrame frame = new GameFrame();
		frame.setName(Constants.NAME);
		frame.setTitle(Constants.NAME);

		// Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Check frames with default size
		// Pack frames with usable preferred size
		if (PACK_FRAME) {
			frame.pack();
		} else {
			frame.validate();
		}

		// Center window
		final Dimension screenSize = Toolkit.getDefaultToolkit()
				.getScreenSize();
		final Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
	}

	/**
	 * @param args
	 *            String[]
	 */
	public static void main(final String[] args) {
		try {
			// UIManager.setLookAndFeel(
			// "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			final Appl app = new Appl();
			app.start();
		} catch (final ClassNotFoundException exception) {
			Logger.getInstance().log(exception.getMessage());
		} catch (final InstantiationException exception) {
			Logger.getInstance().log(exception.getMessage());
		} catch (final IllegalAccessException exception) {
			Logger.getInstance().log(exception.getMessage());
		} catch (final UnsupportedLookAndFeelException exception) {
			Logger.getInstance().log(exception.getMessage());
		}
	}
}