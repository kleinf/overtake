package swing.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import swing.util.ImageLoader;

/**
 * @author Administrator
 * 
 */
public class StartPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private final transient Image bgImage;
	private final boolean bgImageTiled;
	private BufferedImage offscreenImage;

	/**
	 * Constructor.
	 */
	protected StartPanel() {
		super();
		bgImage = ImageLoader.getBufferedImage("images/backgrounds/HP.GIF", this);
		bgImageTiled = false;
		setSize(bgImage.getWidth(null), bgImage.getHeight(null));
		setPreferredSize(getSize());
		setOpaque(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(final Graphics gfx) {
		super.paint(gfx);
		paintBackground(gfx);
	}

	private void paintBackground(final Graphics gfx) {
		if (offscreenImage == null) {
			offscreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			if (bgImage != null) {
				final Graphics2D offscreenGraphics = offscreenImage.createGraphics();
				if (bgImageTiled) {
					final Rectangle rect = new Rectangle(0, 0, bgImage.getWidth(null), bgImage.getHeight(null));
					offscreenGraphics.setPaint(new TexturePaint((BufferedImage) bgImage, rect));
					offscreenGraphics.fill(new Rectangle(0, 0, getWidth(), getHeight()));
				} else {
					offscreenGraphics.drawImage(bgImage, 0, 0, null);
				}
			}
		}
		gfx.drawImage(offscreenImage, 0, 0, null);
	}
}