package gui.swing;

import game.Player;

/**
 * @author Administrator
 * 
 */
public class Wall {

	private int field1x;
	private int field1y;
	private int field2x;
	private int field2y;
	private BoardPanel parent;
	private boolean destructable;
	private int value;
	private int ownerId;

	/**
	 * Wall-Objekt instanziieren und mit default-Werten initialisieren.
	 * 
	 * @param field1x
	 *            Field1x.
	 * @param field1y
	 *            Field1y.
	 * @param field2x
	 *            Field2x.
	 * @param field2y
	 *            Field2y.
	 * @param parent
	 *            Parent-Objekt (Board).
	 * @param destructable
	 *            Aktiv-Flag.
	 */
	protected void init(final int field1x, final int field1y,
			final int field2x, final int field2y, final BoardPanel parent,
			final boolean destructable) {
		this.field1x = field1x;
		this.field1y = field1y;
		this.field2x = field2x;
		this.field2y = field2y;
		this.parent = parent;
		this.destructable = destructable;
		value = 0;
		ownerId = -1;
	}

	/**
	 * @return int
	 */
	protected int getField1x() {
		return field1x;
	}

	/**
	 * @return int
	 */
	protected int getField1y() {
		return field1y;
	}

	/**
	 * @return int
	 */
	protected int getField2x() {
		return field2x;
	}

	/**
	 * @return int
	 */
	protected int getField2y() {
		return field2y;
	}

	/**
	 * @param value
	 *            int
	 */
	public void setValue(final int value) {
		this.value = value;
	}

	/**
	 * @return int
	 */
	public int getValue() {
		return value;
	}

	/**
	 * 
	 */
	public void addValue() {
		value++;
	}

	/**
	 * 
	 */
	public void subValue() {
		value--;
	}

	/**
	 * @param ownerId
	 *            int
	 */
	public void setOwnerId(final int ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return int
	 */
	public int getOwnerId() {
		return ownerId;
	}

	/**
	 * @return Player
	 */
	public Player getOwner() {
		return parent.getPlayer(ownerId);
	}

	/**
	 * @return boolean
	 */
	protected boolean isDestructable() {
		return destructable;
	}

	/**
	 * @param destructable
	 *            boolean
	 */
	public void setDestructable(final boolean destructable) {
		this.destructable = destructable;
	}

	/**
	 * @return boolean
	 */
	public boolean isAllowed() {
		return parent.isAllowed(field1x, field1y, false)
				|| parent.isAllowed(field2x, field2y, false);
	}
}