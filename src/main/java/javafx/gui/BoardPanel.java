package javafx.gui;

import game.Player;

import java.util.List;

import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

import org.jdom.Element;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#getSumFields()
	 */
	@Override
	protected int getSumFields() {
		// TODO Auto-generated method stub
		return super.getSumFields();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#getSumFieldsFree()
	 */
	@Override
	protected int getSumFieldsFree() {
		// TODO Auto-generated method stub
		return super.getSumFieldsFree();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#getSumFieldsPlayer(int)
	 */
	@Override
	protected int getSumFieldsPlayer(int playerId) {
		// TODO Auto-generated method stub
		return super.getSumFieldsPlayer(playerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#isAllowed(int, int, boolean)
	 */
	@Override
	protected boolean isAllowed(int idX, int idY, boolean override) {
		// TODO Auto-generated method stub
		return super.isAllowed(idX, idY, override);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#getPlayer(int)
	 */
	@Override
	protected Player getPlayer(int playerId) {
		// TODO Auto-generated method stub
		return super.getPlayer(playerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#isEditMode()
	 */
	@Override
	protected boolean isEditMode() {
		// TODO Auto-generated method stub
		return super.isEditMode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#isRepairMode()
	 */
	@Override
	protected boolean isRepairMode() {
		// TODO Auto-generated method stub
		return super.isRepairMode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#getActiveRelations(int, int)
	 */
	@Override
	public List<FieldComponent> getActiveRelations(int idX, int idY) {
		// TODO Auto-generated method stub
		return super.getActiveRelations(idX, idY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#disableField(int, int)
	 */
	@Override
	protected void disableField(int idX, int idY) {
		// TODO Auto-generated method stub
		super.disableField(idX, idY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#recalculate()
	 */
	@Override
	protected void recalculate() {
		// TODO Auto-generated method stub
		super.recalculate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#releasePlayer(int)
	 */
	@Override
	protected void releasePlayer(int playerId) {
		// TODO Auto-generated method stub
		super.releasePlayer(playerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics graphics) {
		// TODO Auto-generated method stub
		super.paintComponent(graphics);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#getData()
	 */
	@Override
	protected Element getData() {
		// TODO Auto-generated method stub
		return super.getData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#dispose()
	 */
	@Override
	protected void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub
		super.mouseClicked(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent event) {
		// TODO Auto-generated method stub
		super.mouseEntered(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub
		super.mouseExited(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		super.mousePressed(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub
		super.mouseReleased(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#componentHidden(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		super.componentHidden(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#componentMoved(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		super.componentMoved(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#componentResized(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		super.componentResized(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.BoardPanel#componentShown(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		super.componentShown(arg0);
	}
}