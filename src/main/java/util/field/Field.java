package util.field;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 
 */
public class Field {

	private int idX = 0;
	private int idY = 0;
	private List<FieldPart> fieldParts = new ArrayList<FieldPart>();
	private Shape polygon = new Shape();

	/**
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param fieldParts
	 *            List<FieldPart>
	 */
	protected Field(int idX, int idY, List<FieldPart> fieldParts) {
		super();
		this.idX = idX;
		this.idY = idY;
		this.fieldParts = fieldParts;
		for (FieldPart fp : fieldParts) {
			polygon.append(fp.getWall(), false);
		}
	}

	/**
	 * @param fp
	 *            FieldPart
	 */
	public void appendPart(FieldPart fp) {
		this.fieldParts.add(fp);
		polygon.append(fp.getWall(), false);
	}

	/**
	 * @return int
	 */
	public int getIdX() {
		return idX;
	}

	/**
	 * @return int
	 */
	public int getIdY() {
		return idY;
	}

	/**
	 * @return List<FieldPart>
	 */
	public List<FieldPart> getFieldParts() {
		return fieldParts;
	}

	/**
	 * @return Shape
	 */
	public Shape getPolygon() {
		return polygon;
	}
}
