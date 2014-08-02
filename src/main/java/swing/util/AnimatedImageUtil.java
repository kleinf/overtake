package swing.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.Constants;
import util.PseudoLogger;

/**
 * @author Administrator
 * 
 */
public class AnimatedImageUtil {

	private static final int META_DELAY = 0;
	private static final int META_WIDTH = 1;
	private static final int META_HEIGHT = 2;
	private static final int META_LEFT_POS = 3;
	private static final int META_TOP_POS = 4;
	private static final int META_TRANSPARENT = 5;
	private static final int META_INTERLACE = 6;
	private static final int META_DISPOSAL = 7;
	private static final int DISPOSAL_NO_ACTION = 0;
	private static final int DISPOSAL_NO_DISPOSE = 1;
	private static final int DISPOSAL_TO_BACKGROUND = 2;
	private static final int DISPOSAL_TO_PREVIOUS = 3;

	private AnimatedImageUtil() {
		// private constructor
	}

	/**
	 * @param color
	 *            Color
	 * @return MyImage
	 */
	public static AnimatedImage createMyImage(final Color color) {
		final int width = 256;
		final int height = 256;

		final Image[] frames = new BufferedImage[1];
		final BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		final Graphics g = image.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, width, height);
		frames[0] = image;

		final int[][] metadata = new int[1][8];
		metadata[0][META_WIDTH] = width;
		metadata[0][META_HEIGHT] = height;
		metadata[0][META_LEFT_POS] = 0;
		metadata[0][META_TOP_POS] = 0;
		metadata[0][META_DISPOSAL] = DISPOSAL_TO_BACKGROUND;

		return new AnimatedImage(frames, metadata, width, height);
	}

	/**
	 * @param imageName
	 *            String
	 * @param color
	 *            Color
	 * @return MyImage
	 */
	public static AnimatedImage createMyImage(final String imageName,
			final Color color) {
		if (imageName != null && imageName.trim().length() > 0) {
			try {
				return createMyImage(ImageIO.createImageInputStream(new File(
						Constants.USERPATH + "/" + imageName)));
			} catch (final IOException e) {
				PseudoLogger.getInstance().log(e.getMessage());
			}
		}
		return createMyImage(color);
	}

	/**
	 * @param imageStream
	 *            ImageInputStream
	 * @return MyImage
	 * @throws IOException
	 *             im Fehlerfall
	 */
	private static AnimatedImage createMyImage(
			final ImageInputStream imageStream) throws IOException {
		ImageReader reader = null;
		try {
			final Iterator<ImageReader> readers = ImageIO
					.getImageReaders(imageStream);
			if (!readers.hasNext()) {
				throw new RuntimeException("Imageformat nicht lesbar.");
			}
			reader = readers.next();
			reader.setInput(imageStream);
			final int numFrames = reader.getNumImages(true);
			final int[][] metadata = new int[numFrames][];
			final Image[] frames = new BufferedImage[numFrames];
			int maxWidth = 0;
			int maxHeight = 0;
			for (int i = 0; i < numFrames; i++) {
				metadata[i] = getMetadata(reader.getImageMetadata(i));
				frames[i] = reader.read(i);
				if (frames[i].getWidth(null) > maxWidth) {
					maxWidth = frames[i].getWidth(null);
				}
				if (frames[i].getHeight(null) > maxHeight) {
					maxHeight = frames[i].getHeight(null);
				}
			}

			// Merge Frames
			Graphics2D bufGfx;
			Composite opaque;
			final AlphaComposite clear = AlphaComposite.getInstance(
					AlphaComposite.CLEAR, 0.0F);
			final Image[] framesMerged = new BufferedImage[numFrames];
			for (int i = 0; i < numFrames; i++) {
				framesMerged[i] = new BufferedImage(maxWidth, maxHeight,
						BufferedImage.TYPE_INT_ARGB);
				bufGfx = (Graphics2D) framesMerged[i].getGraphics();
				if (i > 0) {
					bufGfx.drawImage(framesMerged[i - 1], 0, 0, null);
				}
				if (i == 0
						|| metadata[i][META_DISPOSAL] == DISPOSAL_TO_BACKGROUND
						|| metadata[i][META_DISPOSAL] == DISPOSAL_TO_PREVIOUS) {
					opaque = bufGfx.getComposite();
					bufGfx.setComposite(clear);
					bufGfx.fillRect(0, 0, maxWidth, maxHeight);
					bufGfx.setComposite(opaque);
				}
				if (i > 0
						&& metadata[i - 1][META_DISPOSAL] == DISPOSAL_NO_DISPOSE) {
					bufGfx.drawImage(frames[i - 1],
							metadata[i - 1][META_LEFT_POS],
							metadata[i - 1][META_TOP_POS], null);
				}
				if (metadata[i][META_DISPOSAL] == DISPOSAL_TO_PREVIOUS) {
					for (int j = 0; j < i; j++) {
						if (metadata[j][META_DISPOSAL] == DISPOSAL_NO_DISPOSE) {
							bufGfx.drawImage(frames[j],
									metadata[j][META_LEFT_POS],
									metadata[j][META_TOP_POS], null);
						}
					}
				}
				bufGfx.drawImage(frames[i], metadata[i][META_LEFT_POS],
						metadata[i][META_TOP_POS], null);
				bufGfx.dispose();
				metadata[i][META_WIDTH] = maxWidth;
				metadata[i][META_HEIGHT] = maxHeight;
				metadata[i][META_LEFT_POS] = 0;
				metadata[i][META_TOP_POS] = 0;
				metadata[i][META_DISPOSAL] = DISPOSAL_TO_BACKGROUND;
			}

			return new AnimatedImage(framesMerged, metadata, maxWidth,
					maxHeight);
		} finally {
			try {
				if (imageStream != null) {
					imageStream.close();
				}
			} catch (final IOException exception) {
				// do nothing
			}
			if (reader != null) {
				reader.dispose();
			}
		}
	}

	private static int[] getMetadata(final IIOMetadata gim) {
		final int[] metadata = new int[8];
		if ("javax_imageio_gif_image_1.0".equals(gim
				.getNativeMetadataFormatName())) {

			final Node tree = gim.getAsTree("javax_imageio_gif_image_1.0");
			final NodeList children = tree.getChildNodes();
			for (int j = 0; j < children.getLength(); j++) {
				final Node nodeItem = children.item(j);
				if ("ImageDescriptor".equals(nodeItem.getNodeName())) {
					final NamedNodeMap attr = nodeItem.getAttributes();
					Node attnode = attr.getNamedItem("imageLeftPosition");
					metadata[META_LEFT_POS] = Integer.parseInt(attnode
							.getNodeValue());
					attnode = attr.getNamedItem("imageTopPosition");
					metadata[META_TOP_POS] = Integer.parseInt(attnode
							.getNodeValue());
					attnode = attr.getNamedItem("imageWidth");
					metadata[META_WIDTH] = Integer.parseInt(attnode
							.getNodeValue());
					attnode = attr.getNamedItem("imageHeight");
					metadata[META_HEIGHT] = Integer.parseInt(attnode
							.getNodeValue());
					attnode = attr.getNamedItem("interlaceFlag");
					metadata[META_INTERLACE] = Boolean.parseBoolean(attnode
							.getNodeValue()) ? 1 : 0;
				}
				if ("GraphicControlExtension".equals(nodeItem.getNodeName())) {
					final NamedNodeMap attr = nodeItem.getAttributes();
					Node attnode = attr.getNamedItem("disposalMethod");
					switch (attnode.getNodeValue()) {
					case "none":
						metadata[META_DISPOSAL] = DISPOSAL_NO_ACTION;
						break;
					case "doNotDispose":
						metadata[META_DISPOSAL] = DISPOSAL_NO_DISPOSE;
						break;
					case "restoreToBackgroundColor":
						metadata[META_DISPOSAL] = DISPOSAL_TO_BACKGROUND;
						break;
					case "restoreToPrevious":
						metadata[META_DISPOSAL] = DISPOSAL_TO_PREVIOUS;
						break;
					default:
						metadata[META_DISPOSAL] = DISPOSAL_NO_ACTION;
					}
					attnode = attr.getNamedItem("transparentColorFlag");
					metadata[META_TRANSPARENT] = Boolean.parseBoolean(attnode
							.getNodeValue()) ? 1 : 0;
					attnode = attr.getNamedItem("delayTime");
					metadata[META_DELAY] = Integer.parseInt(attnode
							.getNodeValue()) * 10;
				}
			}
		}
		return metadata;
	}
}