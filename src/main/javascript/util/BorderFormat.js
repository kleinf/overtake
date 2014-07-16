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
function BorderFormat(id, refId, refFieldX, refFieldY) {
	/**
	 * @return int
	 */
	BorderFormat.prototype.getId = function() {
		return this.id;
	}

	/**
	 * @return int
	 */
	BorderFormat.prototype.getRefId = function() {
		return this.refId;
	}

	/**
	 * @return int
	 */
	BorderFormat.prototype.getRefFieldX = function() {
		return this.refFieldX;
	}

	/**
	 * @return int
	 */
	BorderFormat.prototype.getRefFieldY = function() {
		return this.refFieldY;
	}

	/**
	 * @return String
	 */
	BorderFormat.prototype.getKey = function() {
		return this.id + "|" + this.refId + "|" + this.refFieldX + "|"
				+ this.refFieldY;
	}

	/**
	 * @return List<PointFormat>
	 */
	BorderFormat.prototype.getPointFormats = function() {
		return this.pointFormats;
	}

	/**
	 * @param pointFormats
	 *            List<PointFormat>
	 */
	BorderFormat.prototype.setPointFormats = function(pointFormats) {
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
	BorderFormat.prototype.getWall = function(width, height, pathPosSumX,
			pathPosSumY) {
		var pathPosX1; // X-Coordinate inside of the form
		var pathPosX2; // X-Coordinate inside of the form
		var pathPosX3; // X-Coordinate inside of the form
		var pathPosY1; // Y-Coordinate inside of the form
		var pathPosY2; // Y-Coordinate inside of the form
		var pathPosY3; // Y-Coordinate inside of the form
		var wall = new Shape();
		var pointFormats = this.getPointFormats();
		for ( var i in pointFormats) {
			pathPosX1 = width * pointFormats[i].getPosX1() + pathPosSumX;
			pathPosY1 = height * pointFormats[i].getPosY1() + pathPosSumY;
			switch (pointFormats[i].getPointType()) {
			case "MOVE":
				wall.moveTo(pathPosX1, pathPosY1);
				break;
			case "LINE":
				wall.lineTo(pathPosX1, pathPosY1);
				break;
			case "QUAD":
				pathPosX2 = width * pointFormats[i].getPosX2() + pathPosSumX;
				pathPosY2 = height * pointFormats[i].getPosY2() + pathPosSumY;
				wall.quadTo(pathPosX1, pathPosY1, pathPosX2, pathPosY2);
				break;
			case "CURVE":
				pathPosX2 = width * pointFormats[i].getPosX2() + pathPosSumX;
				pathPosY2 = height * pointFormats[i].getPosY2() + pathPosSumY;
				pathPosX3 = width * pointFormats[i].getPosX3() + pathPosSumX;
				pathPosY3 = height * pointFormats[i].getPosY3() + pathPosSumY;
				wall.curveTo(pathPosX1, pathPosY1, pathPosX2, pathPosY2,
						pathPosX3, pathPosY3);
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
	BorderFormat.prototype.getWallCenter = function(width, height, pathPosSumX,
			pathPosSumY) {
		var x = 0; // X-Coordinate inside of the form
		var y = 0; // Y-Coordinate inside of the form
		var pfs = this.getPointFormats();
		var id = (int)
		Math.floor(pfs.size() * 0.5);
		var pf = pfs.get(id);
		switch (pf.getPointType()) {
		case "LINE":
			x = width * pf.getPosX1() + pathPosSumX;
			y = height * pf.getPosY1() + pathPosSumY;
			break;
		case "QUAD":
			x = width * pf.getPosX2() + pathPosSumX;
			y = height * pf.getPosY2() + pathPosSumY;
			break;
		case "CURVE":
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
			case "MOVE":
				x += width * pf.getPosX1() + pathPosSumX;
				y += height * pf.getPosY1() + pathPosSumY;
				break;
			case "LINE":
				x += width * pf.getPosX1() + pathPosSumX;
				y += height * pf.getPosY1() + pathPosSumY;
				break;
			case "QUAD":
				x += width * pf.getPosX2() + pathPosSumX;
				y += height * pf.getPosY2() + pathPosSumY;
				break;
			case "CURVE":
				x += width * pf.getPosX3() + pathPosSumX;
				y += height * pf.getPosY3() + pathPosSumY;
				break;
			default:
				// not implemented
			}
			x *= 0.5;
			y *= 0.5;
		}
		return [ x, y ];
	}

	this.id = id;
	this.refId = refId;
	this.refFieldX = refFieldX;
	this.refFieldY = refFieldY;
	this.pointFormats = [];
}
