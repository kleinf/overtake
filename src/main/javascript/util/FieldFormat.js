
const FACTOR = "factor";
const OFFSET_X = "offsetX";
const OFFSET_Y = "offsetY";
const COL = "col";
const ROW = "row";

/**
 * Constructor.
 * 
 * @param id1
 *            Id des Row- oder Col-Elements (je nachdem, was zuerst kommt)
 * @param id2
 *            Id des Row- oder Col-Elements (je nachdem, was danach kommt)
 * @param key1
 *            "row" oder "col" (je nachdem, was zuerst kommt)
 * @param key2
 *            "col" oder "row" (je nachdem, was danach kommt)
 * @param object1
 *            Row- oder Col-Element (je nachdem, was zuerst kommt)
 * @param object2
 *            Row- oder Col-Element (je nachdem, was danach kommt)
 */
function FieldFormat(id1, id2, key1, key2, object1, object2) {

	this.borderFormats = [];
	this.minPosX = Number.MAX_VALUE;
	this.minPosY = Number.MAX_VALUE;
	this.maxPosX = Number.MIN_VALUE;
	this.maxPosY = Number.MIN_VALUE;

	this.name1 = key1.toLowerCase();
	this.name2 = key2.toLowerCase();
	this.map = [];
	this.map[this.name1] = id1;
	this.map[this.name2] = id2;
	this.map[this.name1 + FACTOR] = FACTOR in object1 ? object1[FACTOR].toString() : 1;
	this.map[this.name2 + FACTOR] = FACTOR in object2 ? object2[FACTOR].toString() : 1;
	this.map[this.name1 + OFFSET_X] = OFFSET_X in object1 ? object1[OFFSET_X].toString() : 0;
	this.map[this.name2 + OFFSET_X] = OFFSET_X in object2 ? object2[OFFSET_X].toString() : 0;
	this.map[this.name1 + OFFSET_Y] = OFFSET_Y in object1 ? object1[OFFSET_Y].toString() : 0;
	this.map[this.name2 + OFFSET_Y] = OFFSET_Y in object2 ? object2[OFFSET_Y].toString() : 0;

	/**
	 * Aufbereitung der JSON-Datei mit den Feldbeschreibungen.
	 * 
	 * @param fieldObject
	 *            JSONObject
	 */
	FieldFormat.prototype.parseBorders = function(fieldObject) {
		this.borderFormats = [];
		if ("border" in fieldObject) {
			var borders = fieldObject.border;
			for (var i = 0; i < borders.length; i++) {
				var elementBorder = borders[i];
				var borderFormat = new BorderFormat(elementBorder.id,
						elementBorder.refBorderId, elementBorder.refFieldX,
						elementBorder.refFieldY);
				var pointFormats = [];
				var walls = elementBorder.wall;
				for (var j = 0; j < walls.length; j++) {
					var elementPoint = walls[j];
					var pt = elementPoint.type.toUpperCase();
					var posX1 = elementPoint.x1;
					var posY1 = elementPoint.y1;
					this.setMinMax(posX1, posY1);
					if (pt == "MOVE") {
						pointFormats.push(new PointFormat(pt, posX1, posY1));
					}
					if (pt == "LINE") {
						pointFormats.push(new PointFormat(pt, posX1, posY1));
					}
					if (pt == "QUAD") {
						var posX2 = elementPoint.x2;
						var posY2 = elementPoint.y2;
						this.setMinMax(posX2, posY2);
						pointFormats.push(new PointFormat(pt, posX1, posY1,
								posX2, posY2));
					}
					if (pt == "CURVE") {
						var posX2 = elementPoint.x2;
						var posY2 = elementPoint.y2;
						this.setMinMax(posX2, posY2);
						var posX3 = elementPoint.x3;
						var posY3 = elementPoint.y3;
						this.setMinMax(posX3, posY3);
						pointFormats.push(new PointFormat(pt, posX1, posY1,
								posX2, posY2, posX3, posY3));
					}
				}
				borderFormat.setPointFormats(pointFormats);
				this.borderFormats[borderFormat.getId()] = borderFormat;
			}
		}
	}

	FieldFormat.prototype.setMinMax = function(posX, posY) {
		this.minPosX = Math.min(this.minPosX, posX);
		this.minPosY = Math.min(this.minPosY, posY);
		this.maxPosX = Math.max(this.maxPosX, posX);
		this.maxPosY = Math.max(this.maxPosY, posY);
	}

	/**
	 * @return double
	 */
	FieldFormat.prototype.getMinMaxPosX = function() {
		return this.minPosX + this.maxPosX;
	}

	/**
	 * @return double
	 */
	FieldFormat.prototype.getMinMaxPosY = function() {
		return this.minPosY + this.maxPosY;
	}

	/**
	 * @param key
	 *            String
	 * @return double
	 */
	FieldFormat.prototype.getValue = function(key) {
		var retVal = 0.0;
		if (key in this.map) {
			retVal = parseFloat(this.map[key]);
		}
		return retVal;
	}

	/**
	 * @return int
	 */
	FieldFormat.prototype.getColId = function() {
		var retVal = 0;
		if (COL in this.map) {
			retVal = parseInt(this.map[COL], 10);
		}
		return retVal;
	}

	/**
	 * @return int
	 */
	FieldFormat.prototype.getRowId = function() {
		var retVal = 0;
		if (ROW in this.map) {
			retVal = parseInt(this.map[ROW], 10);
		}
		return retVal;
	}

	/**
	 * Spalten-Abstand als Faktor 0 - 1 (komplette Feldbreite).
	 * 
	 * @return double
	 */
	FieldFormat.prototype.getColFactor = function() {
		return this.getValue(COL + FACTOR);
	}

	/**
	 * Zeilen-Abstand als Faktor 0 - 1 (komplette Feldhoehe).
	 * 
	 * @return double
	 */
	FieldFormat.prototype.getRowFactor = function() {
		return this.getValue(ROW + FACTOR);
	}

	/**
	 * Versetzung X-Achse als Faktor 0 - 1 (komplette Feldbreite).
	 * 
	 * @return double
	 */
	FieldFormat.prototype.getOffsetX = function() {
		return this.getValue(COL + OFFSET_X) + this.getValue(ROW + OFFSET_X);
	}

	/**
	 * Versetzung Y-Achse als Faktor 0 - 1 (komplette Feldhoehe).
	 * 
	 * @return double
	 */
	FieldFormat.prototype.getOffsetY = function() {
		return this.getValue(COL + OFFSET_Y) + this.getValue(ROW + OFFSET_Y);
	}

	/**
	 * @return boolean
	 */
	FieldFormat.prototype.isEmpty = function() {
		return this.borderFormats == void (0) || this.borderFormats.length == 0;
	}

	/**
	 * @return List<BorderFormat>
	 */
	FieldFormat.prototype.getBorderFormats = function() {
		return this.borderFormats;
	}

	/**
	 * Gibt nur die BorderFormat-Objekte zurueck, die eine gueltige
	 * Nachbarschaftsbeziehungen darstellen. Handelt es sich um ein randloses
	 * Spielbrett, muessen die Nachbarschaftsbeziehungen nicht gefiltert werden.
	 * 
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
	 * @return Collection<BorderFormat>
	 */
	FieldFormat.prototype.getBorderFormatsFiltered = function(idX, idY, maxX,
			maxY, borderless) {
		if (borderless) {
			return this.borderFormats;
		}

		borderFormatsFiltered = [];
		// Alle Nachbarschaftsbeziehungen dieses Feldes durchlaufen
		for ( var i in this.borderFormats) {
			if (idX + this.borderFormats[i].getRefFieldX() >= 0
					&& idY + this.borderFormats[i].getRefFieldY() >= 0
					&& idX + this.borderFormats[i].getRefFieldX() < maxX
					&& idY + this.borderFormats[i].getRefFieldY() < maxY) {
				borderFormatsFiltered[this.borderFormats[i].getKey()] = this.borderFormats[i];
			}
		}
		return borderFormatsFiltered;
	}

	/**
	 * Gibt ein Array von Point-Objekten zurueck, das die IDs der moeglichen
	 * Nachbarfelder enthaelt.
	 * 
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
	 * @return List<FieldRelation>
	 */
	FieldFormat.prototype.getRelations = function(idX, idY, maxX, maxY,
			borderless) {
		var borderFormats = this.getBorderFormatsFiltered(idX, idY, maxX, maxY,
				borderless);
		var x;
		var y;
		var relations = [];
		for ( var i in borderFormats) {
			x = idX + borderFormats[i].getRefFieldX();
			y = idY + borderFormats[i].getRefFieldY();
			// Bei Randueberlauf muessen die Nachbarfelder, die ueber den
			// Rand hinausgehen, auf die gegenueberliegende Seite umgeleitet
			// werden.
			if (borderless) {
				if (x < 0) {
					x = maxX - 1;
				}
				if (x >= maxX) {
					x = 0;
				}
				if (y < 0) {
					y = maxY - 1;
				}
				if (y >= maxY) {
					y = 0;
				}
			}
			relations.push(new FieldRelation(borderFormats[i].getId(),
					borderFormats[i].getRefId(), x, y));
		}
		return relations;
	}

	/**
	 * Gibt die grafische Entsprechung des Feldes als Field-Objekt zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param sumColFactor
	 *            double
	 * @param sumRowFactor
	 *            double
	 * @param translate
	 *            boolean
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
	 * @return Field
	 */
	FieldFormat.prototype.getField = function(width, height, sumColFactor,
			sumRowFactor, translate, idX, idY, maxX, maxY, borderless) {
		return new Field(idX, idY, this.getFieldParts(width, height,
				sumColFactor, sumRowFactor, translate, idX, idY, maxX, maxY,
				borderless));
	}

	/**
	 * Gibt die Position (linke obere Ecke) des Feldes als double[] zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param sumColFactor
	 *            double
	 * @param sumRowFactor
	 *            double
	 * @param translate
	 *            boolean
	 * @return double[]
	 */
	FieldFormat.prototype.getPosition = function(width, height, sumColFactor,
			sumRowFactor, translate) {
		// X - Koordinaten des Spaltenanfangs
		var pathPosBoardX = 0;
		if (translate) {
			pathPosBoardX = width * sumColFactor;
		}
		// X - Abweichung der Zeile/Spalte
		var pathPosOffsetX = 0;
		if (translate) {
			pathPosOffsetX = width * this.getOffsetX();
		}
		// Y - Koordinaten des Zeilenanfangs
		var pathPosBoardY = 0;
		if (translate) {
			pathPosBoardY = height * sumRowFactor;
		}
		// Y - Abweichung innerhalb der Zeile/Spalte
		var pathPosOffsetY = 0;
		if (translate) {
			pathPosOffsetY = height * this.getOffsetY();
		}
		var pathPosSumX = pathPosBoardX + pathPosOffsetX;
		var pathPosSumY = pathPosBoardY + pathPosOffsetY;
		return [ pathPosSumX, pathPosSumY ];
	}

	/**
	 * Gibt ein Array von Shape-Objekten zurueck, die jeweils den Linien
	 * entsprechen, die an ein Nachbarfeld angrenzen. Eine Grenze kann dabei aus
	 * mehr als einer Linie bestehen.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param sumColFactor
	 *            double
	 * @param sumRowFactor
	 *            double
	 * @param translate
	 *            boolean
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
	 * @return Map<String, Shape>
	 */
	FieldFormat.prototype.getWalls = function(width, height, sumColFactor,
			sumRowFactor, translate, idX, idY, maxX, maxY, borderless) {
		var pos = this.getPosition(width, height, sumColFactor, sumRowFactor,
				translate);
		var walls = [];
		var borderFormats = this.getBorderFormatsFiltered(idX, idY, maxX, maxY,
				borderless);
		for ( var i in borderFormats) {
			walls[borderFormats[i].getKey()] = borderFormats[i].getWall(width,
					height, pos[0], pos[1]);
		}
		return walls;
	}

	/**
	 * Ermittelt die Groesse einer FieldComponent relativ zur Groesse des
	 * Containers, so dass die Flaeche des Containers ausgefuellt wird.
	 * 
	 * @param containerWidth
	 *            int
	 * @param containerHeight
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @param maxOffsetX
	 *            double
	 * @param maxOffsetY
	 *            double
	 * @return int[]
	 */
	FieldFormat.prototype.getSize = function(containerWidth, containerHeight,
			maxX, maxY, maxOffsetX, maxOffsetY) {
		var width = Math.round(containerWidth
				/ ((maxX - 1.0) * this.getColFactor() + 1.0 + maxOffsetX));
		var height = Math.round(containerHeight
				/ ((maxY - 1.0) * this.getRowFactor() + 1.0 + maxOffsetY));
		return [ width, height ];
	}

	/**
	 * Gibt die Segmente mit der exakten Begrenzung zu den korrekten
	 * Nachbarfeldern zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param sumColFactor
	 *            double
	 * @param sumRowFactor
	 *            double
	 * @param translate
	 *            boolean
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
	 * @return FieldPart[]
	 */
	FieldFormat.prototype.getFieldParts = function(width, height, sumColFactor,
			sumRowFactor, translate, idX, idY, maxX, maxY, borderless) {
		var pos = this.getPosition(width, height, sumColFactor, sumRowFactor,
				translate);
		var centerX = pos[0] + width * this.getMinMaxPosX() * 0.5;
		var centerY = pos[1] + height * this.getMinMaxPosY() * 0.5;

		var fieldRelation;
		var segment;
		var wall;
		var possibleBorder;
		// Alle Nachbarschaftsbeziehungen dieses Feldes durchlaufen
		var x;
		var y;
		var fieldParts = [];
		for ( var i in this.borderFormats) {
			x = idX + this.borderFormats[i].getRefFieldX();
			y = idY + this.borderFormats[i].getRefFieldY();
			// Nachbarschaftsbeziehungen dieses Feldes pruefen
			possibleBorder = borderless
					|| (x >= 0 && y >= 0 && x < maxX && y < maxY);
			// Bei Randueberlauf muessen die Nachbarfelder, die ueber den Rand
			// hinausgehen, auf die gegenueberliegende Seite umgeleitet werden.
			if (borderless) {
				if (x < 0) {
					x = maxX - 1;
				}
				if (x >= maxX) {
					x = 0;
				}
				if (y < 0) {
					y = maxY - 1;
				}
				if (y >= maxY) {
					y = 0;
				}
			}
			fieldRelation = void (0);
			segment = void (0);
			wall = this.borderFormats[i].getWall(width, height, pos[0], pos[1]);
			if (possibleBorder) {
				fieldRelation = new FieldRelation(
						this.borderFormats[i].getId(), this.borderFormats[i]
								.getRefId(), x, y);
				segment = new Shape();
				segment.moveTo(centerX, centerY);
				segment.append(wall, true);
				segment.closePath();
			}
			fieldParts.push(new FieldPart(fieldRelation, segment, wall));
		}
		return fieldParts;
	}
	this.parseBorders(object2);
}
