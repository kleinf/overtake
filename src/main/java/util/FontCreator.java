package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.awt.Font;
import java.awt.FontFormatException;

/**
 * @author Administrator
 * 
 */
public final class FontCreator {

	private FontCreator() {
		// Instanziierung unterbinden
	}

	/**
	 * @param fontName
	 *            String
	 * @return Font
	 */
	public static Font createFont(final String fontName) {
		Font font = null;
		InputStream is = null;
		try {
			is = ClassLoader.getSystemResourceAsStream(fontName);
			font = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (final FileNotFoundException exception) {
			Logger.getInstance().log(exception.getMessage());
		} catch (final FontFormatException exception) {
			Logger.getInstance().log(exception.getMessage());
		} catch (final IOException exception) {
			Logger.getInstance().log(exception.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
		return font;
	}

	/**
	 * @param fontName
	 *            String
	 * @param style
	 *            int
	 * @param size
	 *            float
	 * @return Font
	 */
	public static Font createFont(final String fontName, final int style,
			final float size) {
		Font font = createFont(fontName);
		if (font != null) {
			font = font.deriveFont(style, size);
		}
		return font;
	}
}
