package util.field;

/**
 * @author Administrator
 * 
 */
public class FieldPart {
	private FieldRelation fieldRelation = null;
	private Shape segment = null;
	private Shape wall = null;

	/**
	 * @param fieldRelation
	 *            FieldRelation
	 * @param segment
	 *            Shape
	 * @param wall
	 *            Shape
	 */
	protected FieldPart(FieldRelation fieldRelation, Shape segment, Shape wall) {
		super();
		this.fieldRelation = fieldRelation;
		this.segment = segment;
		this.wall = wall;
	}

	/**
	 * @return the fieldRelation
	 */
	public FieldRelation getFieldRelation() {
		return fieldRelation;
	}

	/**
	 * @return the segment
	 */
	public Shape getSegment() {
		return segment;
	}

	/**
	 * @return the wall
	 */
	public Shape getWall() {
		return wall;
	}
}
