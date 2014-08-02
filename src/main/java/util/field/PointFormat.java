package util.field;

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
	public PointType getPointType() {
		return this.pointType;
	}

	/**
	 * @return double
	 */
	public double getPosX1() {
		return this.posX1;
	}

	/**
	 * @return double
	 */
	public double getPosY1() {
		return this.posY1;
	}

	/**
	 * @return double
	 */
	public double getPosX2() {
		return this.posX2;
	}

	/**
	 * @return double
	 */
	public double getPosY2() {
		return this.posY2;
	}

	/**
	 * @return double
	 */
	public double getPosX3() {
		return this.posX3;
	}

	/**
	 * @return double
	 */
	public double getPosY3() {
		return this.posY3;
	}

	/**
	 * @param pf
	 *            PointFormat
	 * @return boolean
	 */
	protected boolean isPoint(PointFormat pf) {
		double lastEndX;
		double lastEndY;
		switch (pf.getPointType()) {
		case LINE:
			lastEndX = pf.getPosX1();
			lastEndY = pf.getPosY1();
			break;
		case QUAD:
			lastEndX = pf.getPosX2();
			lastEndY = pf.getPosY2();
			break;
		case CURVE:
			lastEndX = pf.getPosX3();
			lastEndY = pf.getPosY3();
			break;
		default:
			lastEndX = pf.getPosX1();
			lastEndY = pf.getPosY1();
		}
		return lastEndX == posX1 && lastEndY == posY1;
	}
}
