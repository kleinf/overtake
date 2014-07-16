package util;

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
	 * Border-Objekt instanziieren und mit default-Werten initialisieren.
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
	protected FieldRelation(final int borderId, final int refBorderId,
			final int refFieldX, final int refFieldY) {
		this.borderId = borderId;
		this.refBorderId = refBorderId;
		this.refFieldX = refFieldX;
		this.refFieldY = refFieldY;
	}

	/**
	 * @return int
	 */
	protected int getBorderId() {
		return borderId;
	}

	/**
	 * @return int
	 */
	protected int getRefBorderId() {
		return refBorderId;
	}

	/**
	 * @return int
	 */
	public int getRefFieldX() {
		return refFieldX;
	}

	/**
	 * @return int
	 */
	public int getRefFieldY() {
		return refFieldY;
	}

	/**
	 * @return String
	 */
	protected String getKey() {
		return borderId + "|" + refBorderId + "|" + refFieldX + "|" + refFieldY;
	}
}