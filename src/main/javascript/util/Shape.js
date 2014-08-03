/**
 * 
 */
function Shape() {
	this.drawCmds = [];

	/**
	 * @param pathPosX1
	 *            double
	 * @param pathPosY1
	 *            double
	 */
	Shape.prototype.moveTo = function(pathPosX1, pathPosY1) {
		this.drawCmds.push(new PointFormat("MOVE", pathPosX1, pathPosY1));
	}

	/**
	 * @param pathPosX1
	 *            double
	 * @param pathPosY1
	 *            double
	 */
	Shape.prototype.lineTo = function(pathPosX1, pathPosY1) {
		this.drawCmds.push(new PointFormat("LINE", pathPosX1, pathPosY1));
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
	Shape.prototype.quadTo = function(pathPosX1, pathPosY1, pathPosX2,
			pathPosY2) {
		this.drawCmds.push(new PointFormat("QUAD", pathPosX1, pathPosY1,
				pathPosX2, pathPosY2));
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
	Shape.prototype.curveTo = function(pathPosX1, pathPosY1, pathPosX2,
			pathPosY2, pathPosX3, pathPosY3) {
		this.drawCmds.push(new PointFormat("CURVE", pathPosX1, pathPosY1,
				pathPosX2, pathPosY2, pathPosX3, pathPosY3));
	}

	/**
	 * 
	 */
	Shape.prototype.closePath = function() {
		var lastMove = void (0);
		for ( var i in this.drawCmds) {
			if (this.drawCmds[i].getPointType() == "MOVE") {
				lastMove = this.drawCmds[i];
			}
		}
		if (lastMove != void (0)) {
			this.drawCmds.push(new PointFormat("LINE", lastMove.getPosX1(),
					lastMove.getPosY1()));
		}
	}

	/**
	 * @param shape
	 *            Shape
	 * @param connect
	 *            boolean
	 */
	Shape.prototype.append = function(shape, connect) {
		// Bei connect = true und in Verbindung mit einer bestehenden Form, wird
		// der erste Move zu einer Line, wenn es die Verbindung noch nicht gibt.
		var startIndex = 0;
		var startPoint = shape.drawCmds[0];
		if (connect && this.drawCmds.length > 0 && shape.drawCmds.length > 0
				&& startPoint.getPointType() == "MOVE") {
			this.drawCmds.push(new PointFormat("LINE", startPoint.getPosX1(),
					startPoint.getPosY1()));
			startIndex = 1;
		}
		for (var i = startIndex; i < shape.drawCmds.length; i++) {
			// Zeichenbefehle zusammenfassen
			if (connect
					|| this.drawCmds.length == 0
					|| !shape.drawCmds[i]
							.isPoint(this.drawCmds[this.drawCmds.length - 1])) {
				this.drawCmds.push(shape.drawCmds[i]);
			}
		}
	}

	/**
	 * @return PointFormat[]
	 */
	Shape.prototype.getDrawCmds = function() {
		return this.drawCmds;
	}
}
