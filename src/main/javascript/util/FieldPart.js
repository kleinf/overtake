/**
 * @param fieldRelation
 *            FieldRelation
 * @param segment
 *            Shape
 * @param wall
 *            Shape
 */
function FieldPart(fieldRelation, segment, wall) {

	this.fieldRelation = fieldRelation;
	this.segment = segment;
	this.wall = wall;

	/**
	 * @return the fieldRelation
	 */
	FieldPart.prototype.getFieldRelation = function() {
		return this.fieldRelation;
	}

	/**
	 * @return the segment
	 */
	FieldPart.prototype.getSegment = function() {
		return this.segment;
	}

	/**
	 * @return the wall
	 */
	FieldPart.prototype.getWall = function() {
		return this.wall;
	}
}
