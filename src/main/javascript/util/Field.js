/**
 * @param idX
 *            int
 * @param idY
 *            int
 * @param fieldParts
 *            FieldPart[]
 */
function Field(idX, idY, fieldParts) {

	this.idX = idX;
	this.idY = idY;
	this.fieldParts = fieldParts;
	this.polygon = new Shape();

	for ( var i in fieldParts) {
		this.polygon.append(fieldParts[i].getWall(), false);
	}

	/**
	 * @param fieldPart
	 *            FieldPart
	 */
	Field.prototype.appendPart = function(fieldPart) {
		this.fieldParts.push(fieldPart);
		this.polygon.append(fieldPart.getWall(), false);
	}

	/**
	 * @return int
	 */
	Field.prototype.getIdX = function() {
		return this.idX;
	}

	/**
	 * @return int
	 */
	Field.prototype.getIdY = function() {
		return this.idY;
	}

	/**
	 * @return List<FieldPart>
	 */
	Field.prototype.getFieldParts = function() {
		return this.fieldParts;
	}

	/**
	 * @return Shape
	 */
	Field.prototype.getPolygon = function() {
		return this.polygon;
	}
}
