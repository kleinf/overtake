/**
 * Constructor. FieldFormat-Objekt einlesen.
 * 
 * @param fileName
 *            String Dateiname.
 */
function FieldFormatter(fieldFileName) {
	this.maxBorders = 0;
	this.fieldFormats = void (0);
	this.colFirst = false;
	this.rowFirst = false;

	/**
	 * Initialisieren des FieldFormatter mit Feldbeschreibungen im JSON-Format.
	 * 
	 * @param object
	 *            JSONObject
	 */
	FieldFormatter.prototype.initJson = function(fieldData) {
		var el0 = fieldData.field;
		var key = Object.keys(el0)[0].toLowerCase();
		this.colFirst = key == "col";
		this.rowFirst = key == "row";
		var key1 = this.colFirst ? "col" : "row";
		var key2 = this.rowFirst ? "col" : "row";
		var list1 = el0[key1];
		this.fieldFormats = [ list1.length ];
		var el1;
		var list2;
		var el2;
		var fieldFormat;
		for (var i = 0; i < list1.length; i++) {
			el1 = list1[i];
			list2 = el1[key2];
			this.fieldFormats[i] = [ list2.length ];
			for (var j = 0; j < list2.length; j++) {
				el2 = list2[j];
				fieldFormat = new FieldFormat(i, j, key1, key2, el1, el2);
				this.maxBorders = Math.max(
						fieldFormat.getBorderFormats().length, this.maxBorders);
				this.fieldFormats[i][j] = fieldFormat;
			}
		}
	}

	/**
	 * Gibt das FieldFormat zurueck, das den uebergebenen Parametern entspricht.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @return boolean
	 * 
	 * @see FieldFormat#isEmpty()
	 */
	FieldFormatter.prototype.getFieldFormat = function(idX, idY) {
		var retVal = void (0);
		if (this.colFirst) {
			var c = idX % this.fieldFormats.length;
			var r = idY % this.fieldFormats[c].length;
			retVal = this.fieldFormats[c][r];
		}
		if (this.rowFirst) {
			var r = idY % this.fieldFormats.length;
			var c = idX % this.fieldFormats[r].length;
			retVal = this.fieldFormats[r][c];
		}
		return retVal;
	}
	this.getFieldFormat = this.getFieldFormat;

	/**
	 * Prueft, ob es sich bei dem angegebenen Feld um einen Platzhalter handelt,
	 * der nicht dargestellt werden soll.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @return boolean
	 * 
	 * @see FieldFormat#isEmpty()
	 */
	FieldFormatter.prototype.isEmpty = function(idX, idY) {
		return this.getFieldFormat(idX, idY).length == 0;
	}

	/**
	 * Gibt die maximale Anzahl von Nachbarschaftsbeziehungen zurueck. Bei
	 * Feldern mit abweichender Anzahl immer der maximal moegliche Wert.
	 * 
	 * @return int
	 */
	FieldFormatter.prototype.getMaxRelations = function() {
		return this.maxBorders;
	}

	/**
	 * Gibt ein Array von Point-Objekten zurueck, das die IDs der Nachbarfelder
	 * enthaelt.
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
	FieldFormatter.prototype.getRelations = function(idX, idY, maxX, maxY,
			borderless) {
		return this.getFieldFormat(idX, idY).getRelations(idX, idY, maxX, maxY,
				borderless);
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
	FieldFormatter.prototype.getPolygon = function(width, height, translate,
			idX, idY, maxX, maxY, borderless) {
		var sumFactors = this.getSumFactors(idX, idY);
		return this.getFieldFormat(idX, idY).getField(width, height,
				sumFactors[0], sumFactors[1], translate, idX, idY, maxX, maxY,
				borderless).getPolygon();
	}

	/**
	 * Gibt die Position des Feldes als double[] zurueck.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @return double[]
	 */
	FieldFormatter.prototype.getSumFactors = function(idX, idY) {
		var sumColFactor = 0;
		var sumRowFactor = 0;
		for (var i = 0; i < idX; i++) {
			sumColFactor += this.getFieldFormat(i, idY).getColFactor();
		}
		for (var j = 0; j < idY; j++) {
			sumRowFactor += this.getFieldFormat(idX, j).getRowFactor();
		}
		return [ sumColFactor, sumRowFactor ];
	}

	/**
	 * Gibt die Position des Feldes als double[] zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param translate
	 *            boolean
	 * @return double[]
	 * 
	 * @see FieldFormat#getPosition(int, int, double, double, boolean)
	 */
	FieldFormatter.prototype.getPosition = function(width, height, idX, idY,
			translate) {
		var sumFactors = this.getSumFactors(idX, idY);
		return this.getFieldFormat(idX, idY).getPosition(width, height,
				sumFactors[0], sumFactors[1], translate);
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
	FieldFormatter.prototype.getBoardsize = function(width, height, maxX, maxY) {
		var maxWidth = 0;
		var maxHeight = 0;
		var poly;
		for (var j = 0; j < maxY; j++) {
			for (var i = 0; i < maxX; i++) {
				poly = this.getPolygon(width, height, true, i, j, maxX, maxY,
						true);
				// TODO
				// maxWidth = Math.max(maxWidth, poly.getBounds().getMaxX());
				// maxHeight = Math.max(maxHeight, poly.getBounds().getMaxY());
			}
		}
		return [ Math.round(maxWidth) + 1, Math.round(maxHeight) + 1 ];
	}

	/**
	 * @param containerWidth
	 *            int
	 * @param containerHeight
	 *            int
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @return int[]
	 */
	FieldFormatter.prototype.getFieldsize = function(containerWidth,
			containerHeight, idX, idY, maxX, maxY) {

		var maxOffsetX = 0;
		var maxOffsetY = 0;
		for ( var i in this.fieldFormats) {
			for ( var j in this.fieldFormats[i]) {
				maxOffsetX = Math.max(this.fieldFormats[i][j].getOffsetX(),
						maxOffsetX);
				maxOffsetY = Math.max(this.fieldFormats[i][j].getOffsetY(),
						maxOffsetY);
			}
		}
		return this.getFieldFormat(idX, idY).getSize(containerWidth,
				containerHeight, maxX, maxY, maxOffsetX, maxOffsetY);
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
	 * @return Map<String, Shape>
	 * 
	 * @see FieldFormat#getWalls(int, int, double, double, boolean, int, int,
	 *      int, int, boolean)
	 */
	FieldFormatter.prototype.getWalls = function(width, height, translate, idX,
			idY, maxX, maxY, borderless) {
		var sumFactors = this.getSumFactors(idX, idY);
		return this.getFieldFormat(idX, idY).getWalls(width, height,
				sumFactors[0], sumFactors[1], translate, idX, idY, maxX, maxY,
				borderless);
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
	 * @return Shape[]
	 * 
	 * @see FieldFormat#getFieldParts(int, int, double, double, boolean, int,
	 *      int, int, int, boolean)
	 */
	FieldFormatter.prototype.getSegments = function(width, height, translate,
			idX, idY, maxX, maxY, borderless) {
		var sumFactors = this.getSumFactors(idX, idY);
		var segments = [];
		var fieldParts = this.getFieldFormat(idX, idY).getField(width, height,
				sumFactors[0], sumFactors[1], translate, idX, idY, maxX, maxY,
				borderless).getFieldParts();
		for ( var i in fieldParts) {
			segments.push(fieldParts[i].getSegment());
		}
		return segments;
	}
}
