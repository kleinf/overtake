/**
 * 
 */
function Shape() {
	/**
	 * @param pathPosX1
	 *            double
	 * @param pathPosY1
	 *            double
	 */
	Shape.prototype.moveTo = function(pathPosX1, pathPosY1) {
		this.drawCmds.push([ "move", pathPosX1, pathPosY1 ]);
	}

	/**
	 * @param pathPosX1
	 *            double
	 * @param pathPosY1
	 *            double
	 */
	Shape.prototype.lineTo = function(pathPosX1, pathPosY1) {
		this.drawCmds.push([ "line", pathPosX1, pathPosY1 ]);
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
		this.drawCmds
				.push([ "quad", pathPosX1, pathPosY1, pathPosX2, pathPosY2 ]);
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
		this.drawCmds.push([ "curve", pathPosX1, pathPosY1, pathPosX2,
				pathPosY2, pathPosX3, pathPosY3 ]);
	}

	/**
	 * 
	 */
	Shape.prototype.closePath = function() {
		var lastMove = void (0);
		for ( var i in this.drawCmds) {
			if (this.drawCmds[i][0] == "move") {
				lastMove = this.drawCmds[i];
			}
		}
		if (lastMove != void (0)) {
			this.drawCmds.push([ "line", lastMove[1], lastMove[2] ]);
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
		if (connect && this.drawCmds.length > 0 && shape.drawCmds.length > 0
				&& shape.drawCmds[0][0] == "move") {
			this.drawCmds.push([ "line", shape.drawCmds[0][1],
					shape.drawCmds[0][2] ]);
			startIndex = 1;
		}
		for (var i = startIndex; i < shape.drawCmds.length; i++) {
			this.drawCmds.push(shape.drawCmds[i]);
		}
	}

	/**
	 * @return drawCmds[]
	 */
	Shape.prototype.getDrawCmds = function() {
		return this.drawCmds;
	}

	this.drawCmds = [];
}
