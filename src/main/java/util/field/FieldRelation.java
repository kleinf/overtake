package util.field;

/**
 * @author Administrator
 * 
 */
public class FieldRelation {

	private final int borderId;
	private final int refBorderId;
	private final int refFieldX;
	private final int refFieldY;

	/**
	 * FieldRelation-Objekt instanziieren und mit default-Werten initialisieren.
	 * 
	 * @param borderId
	 *            ID dieses Feldrandes.
	 * @param refBorderId
	 *            ID des angrenzenden Feldrandes.
	 * @param refFieldX
	 *            Spalten-ID des referenzierten Nachbarfeldes.
	 * @param refFieldY
	 *            Zeilen-ID des referenzierten Nachbarfeldes.
	 */
	protected FieldRelation(final int borderId, final int refBorderId, final int refFieldX, final int refFieldY) {
		this.borderId = borderId;
		this.refBorderId = refBorderId;
		this.refFieldX = refFieldX;
		this.refFieldY = refFieldY;
	}

	/**
	 * @return int
	 */
	protected int getBorderId() {
		return this.borderId;
	}

	/**
	 * @return int
	 */
	protected int getRefBorderId() {
		return this.refBorderId;
	}

	/**
	 * @return int
	 */
	public int getRefFieldX() {
		return this.refFieldX;
	}

	/**
	 * @return int
	 */
	public int getRefFieldY() {
		return this.refFieldY;
	}

	/**
	 * @return String
	 */
	public String getKey() {
		return this.borderId + "|" + this.refBorderId + "|" + this.refFieldX + "|" + this.refFieldY;
	}
}