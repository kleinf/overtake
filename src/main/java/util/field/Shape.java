package util.field;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 
 */
public class Shape {
	private List<PointFormat> drawCmds = new ArrayList<PointFormat>();

	/**
	 * @param pathPosX1
	 *            double
	 * @param pathPosY1
	 *            double
	 */
	protected void moveTo(double pathPosX1, double pathPosY1) {
		this.drawCmds.add(new PointFormat(PointType.MOVE, pathPosX1, pathPosY1));
	}

	/**
	 * @param pathPosX1
	 *            double
	 * @param pathPosY1
	 *            double
	 */
	protected void lineTo(double pathPosX1, double pathPosY1) {
		this.drawCmds.add(new PointFormat(PointType.LINE, pathPosX1, pathPosY1));
	}

	/**
	 * @param pathPosX1
	 *            double
	 * @param pathPosY1
	 *            double
	 * @param pathPosX2
	 *            double
	 * @param pathPosY2
	 *            double
	 */
	protected void quadTo(double pathPosX1, double pathPosY1, double pathPosX2, double pathPosY2) {
		this.drawCmds.add(new PointFormat(PointType.QUAD, pathPosX1, pathPosY1, pathPosX2, pathPosY2));
	}

	/**
	 * @param pathPosX1
	 *            double
	 * @param pathPosY1
	 *            double
	 * @param pathPosX2
	 *            double
	 * @param pathPosY2
	 *            double
	 * @param pathPosX3
	 *            double
	 * @param pathPosY3
	 *            double
	 */
	protected void curveTo(double pathPosX1, double pathPosY1, double pathPosX2, double pathPosY2, double pathPosX3,
			double pathPosY3) {
		this.drawCmds.add(
				new PointFormat(PointType.CURVE, pathPosX1, pathPosY1, pathPosX2, pathPosY2, pathPosX3, pathPosY3));
	}

	/**
	 * 
	 */
	protected void closePath() {
		PointFormat lastMove = null;
		for (PointFormat pf : this.drawCmds) {
			if (PointType.MOVE.equals(pf.getPointType())) {
				lastMove = pf;
			}
		}
		if (lastMove != null) {
			this.drawCmds.add(new PointFormat(PointType.LINE, lastMove.getPosX1(), lastMove.getPosY1()));
		}
	}

	/**
	 * @param shape
	 *            Shape
	 * @param connect
	 *            boolean
	 */
	protected void append(Shape shape, boolean connect) {
		// Bei connect = true und in Verbindung mit einer bestehenden Form, wird
		// der erste Move zu einer Line, wenn es die Verbindung noch nicht gibt.
		int startIndex = 0;
		PointFormat startPoint = shape.getDrawCmds().get(startIndex);
		if (connect && this.drawCmds.size() > 0 && shape.drawCmds.size() > 0
				&& PointType.MOVE.equals(startPoint.getPointType())) {
			this.drawCmds.add(new PointFormat(PointType.LINE, startPoint.getPosX1(), startPoint.getPosY1()));
			startIndex = 1;
		}
		for (int i = startIndex; i < shape.drawCmds.size(); i++) {
			// Zeichenbefehle zusammenfassen
			if (this.drawCmds.size() == 0
					|| !shape.getDrawCmds().get(i).isPoint(this.drawCmds.get(this.drawCmds.size() - 1))) {
				this.drawCmds.add(shape.getDrawCmds().get(i));
			}
		}
	}

	/**
	 * @return List<PointFormat>
	 */
	public List<PointFormat> getDrawCmds() {
		return this.drawCmds;
	}
}
