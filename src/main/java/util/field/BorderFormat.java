package util.field;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 
 */
public class BorderFormat {

	private final int id;
	private final int refId;
	private final int refFieldX;
	private final int refFieldY;
	private List<PointFormat> pointFormats;

	/**
	 * Border-Objekt instanziieren und mit default-Werten initialisieren.
	 * 
	 * @param id
	 *            ID dieses Feldrandes.
	 * @param refId
	 *            ID des angrenzenden Feldrandes.
	 * @param refFieldX
	 *            Spalten-ID des referenzierten Nachbarfeldes.
	 * @param refFieldY
	 *            Zeilen-ID des referenzierten Nachbarfeldes.
	 */
	protected BorderFormat(final int id, final int refId, final int refFieldX, final int refFieldY) {
		this.id = id;
		this.refId = refId;
		this.refFieldX = refFieldX;
		this.refFieldY = refFieldY;
		this.pointFormats = new ArrayList<PointFormat>();
	}

	/**
	 * @return int
	 */
	protected int getId() {
		return this.id;
	}

	/**
	 * @return int
	 */
	protected int getRefId() {
		return this.refId;
	}

	/**
	 * @return int
	 */
	protected int getRefFieldX() {
		return this.refFieldX;
	}

	/**
	 * @return int
	 */
	protected int getRefFieldY() {
		return this.refFieldY;
	}

	/**
	 * @return String
	 */
	protected String getKey() {
		return this.id + "|" + this.refId + "|" + this.refFieldX + "|" + this.refFieldY;
	}

	/**
	 * @return List<PointFormat>
	 */
	private List<PointFormat> getPointFormats() {
		return this.pointFormats;
	}

	/**
	 * @param pointFormats
	 *            List<PointFormat>
	 */
	protected void setPointFormats(final List<PointFormat> pointFormats) {
		this.pointFormats = pointFormats;
	}

	/**
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param pathPosSumX
	 *            int
	 * @param pathPosSumY
	 *            int
	 * @return Shape
	 */
	protected Shape getWall(final int width, final int height, final double pathPosSumX, final double pathPosSumY) {
		double pathPosX1 = 0.0D; // X-Coordinate inside of the form
		double pathPosY1 = 0.0D; // Y-Coordinate inside of the form
		double pathPosX2 = 0.0D; // X-Coordinate inside of the form
		double pathPosY2 = 0.0D; // Y-Coordinate inside of the form
		double pathPosX3 = 0.0D; // X-Coordinate inside of the form
		double pathPosY3 = 0.0D; // Y-Coordinate inside of the form
		final Shape wall = new Shape();
		for (final PointFormat pf : getPointFormats()) {
			pathPosX1 = width * pf.getPosX1() + pathPosSumX;
			pathPosY1 = height * pf.getPosY1() + pathPosSumY;
			switch (pf.getPointType()) {
			case MOVE:
				wall.moveTo(pathPosX1, pathPosY1);
				break;
			case LINE:
				wall.lineTo(pathPosX1, pathPosY1);
				break;
			case QUAD:
				pathPosX2 = width * pf.getPosX2() + pathPosSumX;
				pathPosY2 = height * pf.getPosY2() + pathPosSumY;
				wall.quadTo(pathPosX1, pathPosY1, pathPosX2, pathPosY2);
				break;
			case CURVE:
				pathPosX2 = width * pf.getPosX2() + pathPosSumX;
				pathPosY2 = height * pf.getPosY2() + pathPosSumY;
				pathPosX3 = width * pf.getPosX3() + pathPosSumX;
				pathPosY3 = height * pf.getPosY3() + pathPosSumY;
				wall.curveTo(pathPosX1, pathPosY1, pathPosX2, pathPosY2, pathPosX3, pathPosY3);
				break;
			default:
				// not implemented
			}
		}
		return wall;
	}

	/**
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param pathPosSumX
	 *            int
	 * @param pathPosSumY
	 *            int
	 * @return double[]
	 */
	protected double[] getWallCenter(final int width, final int height, final double pathPosSumX,
			final double pathPosSumY) {
		double x = 0.0D; // X-Coordinate inside of the form
		double y = 0.0D; // Y-Coordinate inside of the form
		final List<PointFormat> pfs = getPointFormats();
		final int id = (int) Math.floor(pfs.size() * 0.5D);
		PointFormat pf = pfs.get(id);
		switch (pf.getPointType()) {
		case LINE:
			x = width * pf.getPosX1() + pathPosSumX;
			y = height * pf.getPosY1() + pathPosSumY;
			break;
		case QUAD:
			x = width * pf.getPosX2() + pathPosSumX;
			y = height * pf.getPosY2() + pathPosSumY;
			break;
		case CURVE:
			x = width * pf.getPosX3() + pathPosSumX;
			y = height * pf.getPosY3() + pathPosSumY;
			break;
		default:
			// not implemented
		}
		// Bei gerader Anzahl von Referenzpunkten Mittelwert bilden
		if (pfs.size() % 2 == 0) {
			pf = pfs.get(id - 1);
			switch (pf.getPointType()) {
			case MOVE:
				x += width * pf.getPosX1() + pathPosSumX;
				y += height * pf.getPosY1() + pathPosSumY;
				break;
			case LINE:
				x += width * pf.getPosX1() + pathPosSumX;
				y += height * pf.getPosY1() + pathPosSumY;
				break;
			case QUAD:
				x += width * pf.getPosX2() + pathPosSumX;
				y += height * pf.getPosY2() + pathPosSumY;
				break;
			case CURVE:
				x += width * pf.getPosX3() + pathPosSumX;
				y += height * pf.getPosY3() + pathPosSumY;
				break;
			default:
				// not implemented
			}
			x *= 0.5D;
			y *= 0.5D;
		}
		return new double[] { x, y };
	}
}