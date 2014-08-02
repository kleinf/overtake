package swing.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @author Administrator
 * 
 */
public class AnimatedImage {
	private boolean anim = false;
	private final int numFrames;
	private final Image[] frames;
	private final Image[] framesResized;
	private final int[][] metadata;
	private final int[][] metadataResized;
	private int width = 0;
	private int height = 0;

	private static final int META_DELAY = 0;
	private static final int META_WIDTH = 1;
	private static final int META_HEIGHT = 2;

	/**
	 * @param frames
	 *            Image[]
	 * @param metadata
	 *            int[][]
	 * @param width
	 *            int
	 * @param height
	 *            int
	 */
	public AnimatedImage(final Image[] frames, final int[][] metadata,
			final int width, final int height) {
		this.frames = frames;
		framesResized = new BufferedImage[frames.length];
		this.metadata = metadata;
		metadataResized = metadata.clone();
		numFrames = frames.length;
		anim = numFrames > 1;
		this.width = width;
		this.height = height;
	}

	/**
	 * @param widthResized
	 *            int
	 * @param heightResized
	 *            int
	 * @return AnimatedImage
	 */
	public AnimatedImage resize(final int widthResized, final int heightResized) {
		// Resize
		double scaleX = 1.0D;
		double scaleY = 1.0D;
		if (widthResized > 0) {
			scaleX = widthResized / (double) width;
		}
		if (heightResized > 0) {
			scaleY = heightResized / (double) height;
		}
		final AffineTransform scale = AffineTransform.getScaleInstance(scaleX,
				scaleY);
		Graphics2D bufGfx;
		for (int i = 0; i < numFrames; i++) {
			metadataResized[i][META_WIDTH] = widthResized == 0 ? width
					: widthResized;
			metadataResized[i][META_HEIGHT] = heightResized == 0 ? height
					: heightResized;
			framesResized[i] = new BufferedImage(widthResized == 0 ? width
					: widthResized,
					heightResized == 0 ? height : heightResized,
					BufferedImage.TYPE_INT_ARGB);
			bufGfx = (Graphics2D) framesResized[i].getGraphics();
			// Better quality
			bufGfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			bufGfx.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			bufGfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			bufGfx.setTransform(scale);
			bufGfx.drawImage(frames[i], 0, 0, null);
		}
		return this;
	}

	/**
	 * @param widthResized
	 *            int
	 * @param heightResized
	 *            int
	 * @return AnimatedImage
	 */
	public AnimatedImage getResizedCopy(final int widthResized,
			final int heightResized) {
		return new AnimatedImage(frames.clone(), metadata.clone(), width,
				height).resize(widthResized, heightResized);
	}

	/**
	 * @param frameId
	 *            int
	 * @return Image
	 */
	public Image getFrame(final int frameId) {
		return framesResized[frameId];
	}

	/**
	 * @param frameId
	 *            int
	 * @return int
	 */
	public int getFrameDelay(final int frameId) {
		return metadataResized[frameId][META_DELAY];
	}

	/**
	 * @return boolean
	 */
	public boolean isAnim() {
		return anim;
	}

	/**
	 * @return the numFrames
	 */
	public int getNumFrames() {
		return numFrames;
	}
}