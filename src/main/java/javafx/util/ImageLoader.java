package javafx.util;

import java.net.URL;

import javafx.scene.image.Image;

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
	 * @return Image
	 */
	public static Image getImage(final String imageName) {

		if (imageName == null || imageName.trim().length() == 0) {
			return null;
		}

		final URL imageUrl = ClassLoader.getSystemResource(imageName);

		if (imageUrl == null) {
			return null;
		}
		return new Image(imageName.toString());
	}
}