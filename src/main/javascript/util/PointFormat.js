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
function PointFormat(pointType, posX1, posY1, posX2, posY2, posX3, posY3) {
	/**
	 * @return PointType
	 */
	PointFormat.prototype.getPointType = function() {
		return this.pointType;
	}

	/**
	 * @return double
	 */
	PointFormat.prototype.getPosX1 = function() {
		return this.posX1;
	}

	/**
	 * @return double
	 */
	PointFormat.prototype.getPosY1 = function() {
		return this.posY1;
	}

	/**
	 * @return double
	 */
	PointFormat.prototype.getPosX2 = function() {
		return this.posX2;
	}

	/**
	 * @return double
	 */
	PointFormat.prototype.getPosY2 = function() {
		return this.posY2;
	}

	/**
	 * @return double
	 */
	PointFormat.prototype.getPosX3 = function() {
		return this.posX3;
	}

	/**
	 * @return double
	 */
	PointFormat.prototype.getPosY3 = function() {
		return this.posY3;
	}

	this.pointType = pointType;
	this.posX1 = posX1 == void (0) ? 0 : posX1;
	this.posY1 = posY1 == void (0) ? 0 : posY1;
	this.posX2 = posX2 == void (0) ? 0 : posX2;
	this.posY2 = posY2 == void (0) ? 0 : posY2;
	this.posX3 = posX3 == void (0) ? 0 : posX3;
	this.posY3 = posY3 == void (0) ? 0 : posY3;
}
