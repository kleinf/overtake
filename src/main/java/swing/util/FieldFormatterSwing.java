package swing.util;

import java.util.HashMap;
import java.util.Map;

import java.awt.geom.GeneralPath;

import util.field.FieldFormat;
import util.field.FieldFormatter;
import util.field.FieldPart;
import util.field.PointFormat;
import util.field.PointType;
import util.field.Shape;

/**
 * @author Administrator
 * 
 */
public final class FieldFormatterSwing extends FieldFormatter {

	private static FieldFormatterSwing formatter;

	/**
	 * Constructor.
	 */
	private FieldFormatterSwing() {
		// Singleton --> FieldFormatter.getInstance()
	}

	/**
	 * @return FieldFormatter
	 */
	public static synchronized FieldFormatterSwing getInstance() {

		if (formatter == null) {
			formatter = new FieldFormatterSwing();
		}
		return formatter;
	}

	/**
	 * Gibt die grafische Entsprechung des Feldes als Shape zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param translate
	 *            boolean true, wenn das Feld positioniert werden soll.
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @param borderless
	 *            boolean
	 * @return Shape
	 * 
	 * @see FieldFormat#getField(int, int, double, double, boolean, int, int,
	 *      int, int, boolean)
	 */
	public GeneralPath getPolygon(final int width, final int height, final boolean translate, final int idX,
			final int idY, final int maxX, final int maxY, boolean borderless) {
		final double[] sumFactors = getSumFactors(idX, idY);
		return getGeneralPath(getFieldFormat(idX, idY)
				.getField(width, height, sumFactors[0], sumFactors[1], translate, idX, idY, maxX, maxY, borderless)
				.getPolygon());
	}

	/**
	 * Gibt den Platzbedarf aller Felder als Dimension zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @return int[]
	 * 
	 * @see FieldFormat#getField(int, int, double, double, boolean, int, int,
	 *      int, int, boolean)
	 */
	public int[] getBoardsize(final int width, final int height, final int maxX, final int maxY) {

		double maxWidth = 0.0D;
		double maxHeight = 0.0D;
		GeneralPath poly;
		for (int j = 0; j < maxY; j++) {
			for (int i = 0; i < maxX; i++) {
				poly = getPolygon(width, height, true, i, j, maxX, maxY, true);
				maxWidth = Math.max(maxWidth, poly.getBounds().getMaxX());
				maxHeight = Math.max(maxHeight, poly.getBounds().getMaxY());
			}
		}

		return new int[] { (int) Math.rint(maxWidth) + 1, (int) Math.rint(maxHeight) + 1 };
	}

	/**
	 * Gibt eine Liste von GeneralPath-Objekten zurueck, die jeweils den Linien
	 * entsprechen, die an ein Nachbarfeld angrenzen. Eine Grenze kann dabei aus
	 * mehr als einer Linie bestehen.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param translate
	 *            boolean true, wenn das Feld positioniert werden soll.
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @param borderless
	 *            boolean
	 * @return Map<String, GeneralPath>
	 * 
	 * @see FieldFormat#getWalls(int, int, double, double, boolean, int, int,
	 *      int, int, boolean)
	 */
	public Map<String, GeneralPath> getWalls(final int width, final int height, final boolean translate, final int idX,
			final int idY, final int maxX, final int maxY, final boolean borderless) {

		final double[] sumFactors = getSumFactors(idX, idY);
		Map<String, GeneralPath> walls = new HashMap<String, GeneralPath>();
		for (Map.Entry<String, Shape> entry : getFieldFormat(idX, idY)
				.getWalls(width, height, sumFactors[0], sumFactors[1], translate, idX, idY, maxX, maxY, borderless)
				.entrySet()) {
			walls.put(entry.getKey(), getGeneralPath(entry.getValue()));
		}
		return walls;
	}

	private GeneralPath getGeneralPath(Shape shape) {
		GeneralPath gp = new GeneralPath();
		for (PointFormat pf : shape.getDrawCmds()) {
			if (PointType.MOVE.equals(pf.getPointType())) {
				gp.moveTo(pf.getPosX1(), pf.getPosY1());
			} else if (PointType.LINE.equals(pf.getPointType())) {
				gp.lineTo(pf.getPosX1(), pf.getPosY1());
			} else if (PointType.QUAD.equals(pf.getPointType())) {
				gp.quadTo(pf.getPosX1(), pf.getPosY1(), pf.getPosX2(), pf.getPosY2());
			} else if (PointType.CURVE.equals(pf.getPointType())) {
				gp.curveTo(pf.getPosX1(), pf.getPosY1(), pf.getPosX2(), pf.getPosY2(), pf.getPosX3(), pf.getPosY3());
			}
		}
		return gp;
	}

	/**
	 * Gibt die Segmente mit der exakten Begrenzung zu den korrekten
	 * Nachbarfeldern zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param translate
	 *            boolean true, wenn das Feld positioniert werden soll.
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @param borderless
	 *            boolean
	 * @return Map<String, GeneralPath>
	 * 
	 * @see FieldFormat#getFieldParts(int, int, double, double, boolean, int,
	 *      int, int, int, boolean)
	 */
	public Map<String, GeneralPath> getSegments(final int width, final int height, final boolean translate,
			final int idX, final int idY, final int maxX, final int maxY, final boolean borderless) {

		final double[] sumFactors = getSumFactors(idX, idY);
		Map<String, GeneralPath> segments = new HashMap<String, GeneralPath>();

		for (FieldPart fp : getFieldFormat(idX, idY)
				.getField(width, height, sumFactors[0], sumFactors[1], translate, idX, idY, maxX, maxY, borderless)
				.getFieldParts()) {
			if (fp.getSegment() != null) {
				segments.put(fp.getFieldRelation().getKey(), getGeneralPath(fp.getSegment()));
			}
		}
		return segments;
	}

	// ********* Folgende Methoden sind nur zur Visualisierung gedacht *********

	/**
	 * Gibt die Pfeile in der exakten Laenge zu den korrekten Nachbarfeldern
	 * zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param translate
	 *            boolean true, wenn das Feld positioniert werden soll.
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @param borderless
	 *            boolean
	 * @return Map<String, GeneralPath>
	 */
	public Map<String, GeneralPath> getArrows(final int width, final int height, final boolean translate, final int idX,
			final int idY, final int maxX, final int maxY, final boolean borderless) {

		Map<String, GeneralPath> arrows = new HashMap<String, GeneralPath>();
		for (Map.Entry<String, Shape> entry : getArrowShapes(width, height, translate, idX, idY, maxX, maxY, borderless)
				.entrySet()) {
			arrows.put(entry.getKey(), getGeneralPath(entry.getValue()));
		}
		return arrows;
	}
}