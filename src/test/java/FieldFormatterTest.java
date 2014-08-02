import java.util.Map;
import java.util.Map.Entry;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import swing.util.FieldFormatterSwing;

/**
 * 
 */
public class FieldFormatterTest {

	private int numFieldsWidth = 10;
	private int numFieldsHeight = 10;
	private int fieldWidth = 40;
	private int fieldHeight = 40;
	private int boardWidth = 400;
	private int boardHeight = 400;
	private boolean borderless = false;

	private String fieldName = "A0.xml";
	private FieldFormatterSwing ff = FieldFormatterSwing.getInstance();

	/**
	 * 
	 */
	@Before
	public void init() {
		ff.init("src/test/resources/" + fieldName);
	}

	/**
	 * 
	 */
	@Test
	public void testFieldsizeToBoardsize() {
		int[] boardSize = ff.getBoardsize(fieldWidth, fieldHeight,
				numFieldsWidth, numFieldsHeight);

		int[] fieldSize = ff.getFieldsize(boardSize[0], boardSize[1], 0, 0,
				numFieldsWidth, numFieldsHeight);

		// Assert.assertEquals(fieldWidth, fieldSize[0]);
		// Assert.assertEquals(fieldHeight, fieldSize[1]);
	}

	/**
	 * 
	 */
	@Test
	public void testBoardsizeToFieldsize() {
		int[] fieldSize = ff.getFieldsize(boardWidth, boardHeight, 0, 0,
				numFieldsWidth, numFieldsHeight);

		int[] boardSize = ff.getBoardsize(fieldSize[0], fieldSize[1],
				numFieldsWidth, numFieldsHeight);

		// Assert.assertEquals(boardWidth, boardSize[0]);
		// Assert.assertEquals(boardHeight, boardSize[1]);
	}

	/**
	 * 
	 */
	@Test
	public void testCoords() {
		int[] fieldSize = ff.getFieldsize(boardWidth, boardHeight, 0, 0,
				numFieldsWidth, numFieldsHeight);
		for (int idY = 0; idY < numFieldsHeight; idY++) {
			for (int idX = 0; idX < numFieldsWidth; idX++) {
				ff.getPolygon(fieldSize[0], fieldSize[1], true, idX, idY,
						numFieldsWidth, numFieldsHeight);
				if (!FieldFormatterSwing.getInstance().isEmpty(idX, idY)) {
					outputSegments(idX, idY);
				}
			}
		}
	}

	private void outputSegments(final int idX, final int idY) {
		final GeneralPath poly = FieldFormatterSwing.getInstance().getPolygon(
				fieldWidth, fieldHeight, true, idX, idY, numFieldsWidth,
				numFieldsHeight);
		final Map<String, GeneralPath> segments = FieldFormatterSwing
				.getInstance().getSegments(fieldWidth, fieldHeight, true, idX,
						idY, numFieldsWidth, numFieldsHeight, borderless);
		System.out.println(idX + "/" + idY + " " + poly.getBounds().width + "/"
				+ poly.getBounds().height + " " + poly.getBounds().x + "/"
				+ poly.getBounds().y);
		for (final Entry<String, GeneralPath> entry : segments.entrySet()) {
			final String[] borders = entry.getKey().split("\\|");
			final GeneralPath path = entry.getValue();
			System.out.println("\t" + borders[0] + "->" + borders[1] + " ("
					+ (idX + Integer.parseInt(borders[2])) + "/"
					+ (idY + Integer.parseInt(borders[3])) + ")");
			outputSegment(path);
		}
	}

	private void outputSegment(GeneralPath path) {
		StringBuilder sb = new StringBuilder();
		for (PathIterator it = path.getPathIterator(null); !it.isDone(); it
				.next()) {
			double[] coords = new double[6];
			int type = it.currentSegment(coords);

			switch (type) {
			case PathIterator.SEG_MOVETO:
				sb.append("x:").append((int) coords[0]).append("/")
						.append("y:").append((int) coords[1]);
				break;
			case PathIterator.SEG_LINETO:
				sb.append(" --> ").append("x1:").append((int) coords[0])
						.append("/").append("y1:").append((int) coords[1]);
				break;
			case PathIterator.SEG_QUADTO:
				sb.append(" --> ").append("x1:").append((int) coords[0])
						.append("/").append("y1:").append((int) coords[1])
						.append("/").append("x2:").append((int) coords[2])
						.append("/").append("y2:").append((int) coords[3]);
				break;
			case PathIterator.SEG_CUBICTO:
				sb.append(" --> ").append("x1:").append((int) coords[0])
						.append("/").append("y1:").append((int) coords[1])
						.append("/").append("x2:").append((int) coords[2])
						.append("/").append("y2:").append((int) coords[3])
						.append("/").append("x3:").append((int) coords[4])
						.append("/").append("y3:").append((int) coords[5]);
				break;
			}
		}
		System.out.println("\t\t\t" + sb.toString());
	}
}
