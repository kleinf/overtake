package javafx.gui;

import java.util.List;

import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

import org.jdom2.Element;

import game.Player;
import swing.gui.AbstractMainPanel;
import swing.gui.FieldComponent;

/**
 * Dies ist die grafische Repraesentation des Spielbretts. Es besteht aus einer
 * Liste von FieldComponents die ueber Reflection geladen werden. Zudem ist es
 * moeglich, ein Hintergrundbild einzufuegen, welches einfach gezeichnet oder
 * gekachelt dargestellt wird.
 */
public class BoardPanel extends swing.gui.BoardPanel {

	/**
	 * @param parentPanel
	 * @param data
	 */
	protected BoardPanel(AbstractMainPanel parentPanel, Element data) {
		super(parentPanel, data);
	}

	/**
	 * @see gui.BoardPanel#getSumFields()
	 */
	@Override
	protected int getSumFields() {
		return super.getSumFields();
	}

	/**
	 * @see gui.BoardPanel#getSumFieldsFree()
	 */
	@Override
	protected int getSumFieldsFree() {
		return super.getSumFieldsFree();
	}

	/**
	 * @see gui.BoardPanel#getSumFieldsPlayer(int)
	 */
	@Override
	protected int getSumFieldsPlayer(int playerId) {
		return super.getSumFieldsPlayer(playerId);
	}

	/**
	 * @see gui.BoardPanel#isAllowed(int, int, boolean)
	 */
	@Override
	protected boolean isAllowed(int idX, int idY, boolean override) {
		return super.isAllowed(idX, idY, override);
	}

	/**
	 * @see gui.BoardPanel#getPlayer(int)
	 */
	@Override
	protected Player getPlayer(int playerId) {
		return super.getPlayer(playerId);
	}

	/**
	 * @see gui.BoardPanel#isEditMode()
	 */
	@Override
	protected boolean isEditMode() {
		return super.isEditMode();
	}

	/**
	 * @see gui.BoardPanel#isRepairMode()
	 */
	@Override
	protected boolean isRepairMode() {
		return super.isRepairMode();
	}

	/**
	 * @see gui.BoardPanel#getActiveRelations(int, int)
	 */
	@Override
	public List<FieldComponent> getActiveRelations(int idX, int idY) {
		return super.getActiveRelations(idX, idY);
	}

	/**
	 * @see gui.BoardPanel#disableField(int, int)
	 */
	@Override
	protected void disableField(int idX, int idY) {
		super.disableField(idX, idY);
	}

	/**
	 * @see gui.BoardPanel#recalculate()
	 */
	@Override
	protected void recalculate() {
		super.recalculate();
	}

	/**
	 * @see gui.BoardPanel#releasePlayer(int)
	 */
	@Override
	protected void releasePlayer(int playerId) {
		super.releasePlayer(playerId);
	}

	/**
	 * @see gui.BoardPanel#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
	}

	/**
	 * @see gui.BoardPanel#getData()
	 */
	@Override
	protected Element getData() {
		return super.getData();
	}

	/**
	 * @see gui.BoardPanel#dispose()
	 */
	@Override
	protected void dispose() {
		super.dispose();
	}

	/**
	 * @see gui.BoardPanel#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent event) {
		super.mouseClicked(event);
	}

	/**
	 * @see gui.BoardPanel#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent event) {
		super.mouseEntered(event);
	}

	/**
	 * @see gui.BoardPanel#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent event) {
		super.mouseExited(event);
	}

	/**
	 * @see gui.BoardPanel#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent event) {
		super.mousePressed(event);
	}

	/**
	 * @see gui.BoardPanel#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent event) {
		super.mouseReleased(event);
	}

	/**
	 * @see gui.BoardPanel#componentHidden(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentHidden(ComponentEvent arg0) {
		super.componentHidden(arg0);
	}

	/**
	 * @see gui.BoardPanel#componentMoved(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentMoved(ComponentEvent arg0) {
		super.componentMoved(arg0);
	}

	/**
	 * @see gui.BoardPanel#componentResized(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentResized(ComponentEvent arg0) {
		super.componentResized(arg0);
	}

	/**
	 * @see gui.BoardPanel#componentShown(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentShown(ComponentEvent arg0) {
		super.componentShown(arg0);
	}
}