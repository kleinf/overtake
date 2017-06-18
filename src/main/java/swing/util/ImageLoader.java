package swing.util;

import java.net.URL;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;

/**
 * @author Administrator
 * 
 */
public final class ImageLoader {

	private ImageLoader() {
		// private constructor
	}

	/**
	 * @param imageName
	 *            String
	 * @param component
	 *            Component
	 * @return BufferedImage
	 */
	public static BufferedImage getBufferedImage(final String imageName, final Component component) {

		if (imageName == null || imageName.trim().length() == 0) {
			return null;
		}

		final URL imageUrl = ClassLoader.getSystemResource(imageName);

		if (imageUrl == null) {
			return null;
		}
		final Image image = component.getToolkit().createImage(imageUrl);

		final MediaTracker mediaTracker = new MediaTracker(component);
		mediaTracker.addImage(image, 0);
		try {
			mediaTracker.waitForID(0);
		} catch (final InterruptedException exception) {
			return null;
		}

		if (mediaTracker.isErrorAny()) {
			return null;
		}

		final BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		return bufferedImage;
	}
}