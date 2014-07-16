package util;

/**
 * @author Administrator
 * 
 */
public class PointFormat {

	private final PointType pointType;
	private final double posX1;
	private final double posY1;
	private final double posX2;
	private final double posY2;
	private final double posX3;
	private final double posY3;

	/**
	 * @param pointType
	 *            int
	 * @param posX1
	 *            double
	 * @param posY1
	 *            double
	 */
	protected PointFormat(final PointType pointType, final double posX1,
			final double posY1) {
		this.pointType = pointType;
		this.posX1 = posX1;
		this.posY1 = posY1;
		posX2 = 0;
		posY2 = 0;
		posX3 = 0;
		posY3 = 0;
	}

	/**
	 * @param pointType
	 *            PointType
	 * @param posX1
	 *            double
	 * @param posY1
	 *            double
	 * @param posX2
	 *            double
	 * @param posY2
	 *            double
	 */
	protected PointFormat(final PointType pointType, final double posX1,
			final double posY1, final double posX2, final double posY2) {
		this.pointType = pointType;
		this.posX1 = posX1;
		this.posY1 = posY1;
		this.posX2 = posX2;
		this.posY2 = posY2;
		posX3 = 0;
		posY3 = 0;
	}

	/**
	 * @param pointType
	 *            PointType
	 * @param posX1
	 *            double
	 * @param posY1
	 *            double
	 * @param posX2
	 *            double
	 * @param posY2
	 *            double
	 * @param posX3
	 *            double
	 * @param posY3
	 *            double
	 */
	protected PointFormat(final PointType pointType, final double posX1,
			final double posY1, final double posX2, final double posY2,
			final double posX3, final double posY3) {
		this.pointType = pointType;
		this.posX1 = posX1;
		this.posY1 = posY1;
		this.posX2 = posX2;
		this.posY2 = posY2;
		this.posX3 = posX3;
		this.posY3 = posY3;
	}

	/**
	 * @return PointType
	 */
	protected PointType getPointType() {
		return pointType;
	}

	/**
	 * @return double
	 */
	protected double getPosX1() {
		return posX1;
	}

	/**
	 * @return double
	 */
	protected double getPosY1() {
		return posY1;
	}

	/**
	 * @return double
	 */
	protected double getPosX2() {
		return posX2;
	}

	/**
	 * @return double
	 */
	protected double getPosY2() {
		return posY2;
	}

	/**
	 * @return double
	 */
	protected double getPosX3() {
		return posX3;
	}

	/**
	 * @return double
	 */
	protected double getPosY3() {
		return posY3;
	}
}
