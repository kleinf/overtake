package swing.gui;

import java.util.ArrayList;
import java.util.List;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.jdom2.Element;

import game.GameSession;
import game.Player;
import swing.util.FieldFormatterSwing;
import swing.util.ImageLoader;
import util.field.FieldRelation;

/**
 * Dies ist die grafische Repraesentation des Spielbretts. Es besteht aus einer
 * Liste von FieldComponents die ueber Reflection geladen werden. Zudem ist es
 * moeglich, ein Hintergrundbild einzufuegen, welches einfach gezeichnet oder
 * gekachelt dargestellt wird.
 */
public class BoardPanel extends JPanel implements MouseListener, ComponentListener {

	private static final long serialVersionUID = 1L;
	private static final boolean BG_IMAGE_TILED = true;
	private final AbstractMainPanel parentPanel;
	private int sumFields = 0;
	private int sumFieldsFree = 0;
	private final int[] sumFieldsPlayer;
	private final Wall[][][] relations;
	private BufferedImage offscreenImage;
	private final FieldComponent[][] components;
	private final int numFieldsWidth;
	private final int numFieldsHeight;
	private boolean mousePressed = false;
	private Component dragComp = null;

	/**
	 * @param parentPanel
	 *            AbstractMainPanel
	 * @param data
	 *            Element
	 */
	protected BoardPanel(final AbstractMainPanel parentPanel, final Element data) {
		super();
		this.parentPanel = parentPanel;
		sumFieldsPlayer = new int[GameSession.gameOptions.getMaxPlayers()];

		setOpaque(true);

		// Felder erzeugen
		numFieldsWidth = GameSession.gameOptions.getNumFieldsWidth();
		numFieldsHeight = GameSession.gameOptions.getNumFieldsHeight();
		final int fieldWidth = GameSession.gameOptions.getFieldWidth();
		final int fieldHeight = GameSession.gameOptions.getFieldHeight();
		components = new FieldComponent[numFieldsWidth][numFieldsHeight];
		FieldFormatterSwing.getInstance().init(GameSession.gameOptions.getFieldName());
		setLayout(null);

		// Felder initialisieren
		if (data == null) {
			for (int idY = 0; idY < numFieldsHeight; idY++) {
				for (int idX = 0; idX < numFieldsWidth; idX++) {
					addField(idX, idY, fieldWidth, fieldHeight, null);
				}
			}
		} else {
			for (final Object elField : data.getChildren("field")) {
				final int idX = Integer.parseInt(((Element) elField).getAttributeValue("idX"));
				final int idY = Integer.parseInt(((Element) elField).getAttributeValue("idY"));
				addField(idX, idY, fieldWidth, fieldHeight, (Element) elField);
			}
		}

		// Nachbarschaftsbeziehungen aufbereiten
		relations = new Wall[numFieldsWidth][numFieldsHeight][FieldFormatterSwing.getInstance().getMaxRelations()];
		for (int idY = 0; idY < numFieldsHeight; idY++) {
			for (int idX = 0; idX < numFieldsWidth; idX++) {
				if (getFieldComponent(idX, idY) != null) {
					final List<FieldRelation> rel = FieldFormatterSwing.getInstance().getRelations(idX, idY,
							numFieldsWidth, numFieldsHeight, GameSession.gameOptions.isBorderless());
					for (int i = 0; i < rel.size(); i++) {
						relations[idX][idY][i] = convertRelation(idX, idY, rel.get(i));
					}
				}
			}
		}

		// Groesse des BoardPanels an Inhalt anpassen
		final double[] pos = FieldFormatterSwing.getInstance().getPosition(fieldWidth, fieldHeight, numFieldsWidth - 1,
				numFieldsHeight - 1, true);
		setSize((int) Math.rint(pos[0]) + fieldWidth + 1, (int) Math.rint(pos[1]) + fieldHeight + 1);

		// Groesse des BoardPanels an Inhalt anpassen
		int[] size = FieldFormatterSwing.getInstance().getBoardsize(fieldWidth, fieldHeight, numFieldsWidth,
				numFieldsHeight);
		setSize(size[0], size[1]);
		setPreferredSize(getSize());
		createBackground();
		addComponentListener(this);
	}

	private void addField(final int idX, final int idY, final int fieldWidth, final int fieldHeight,
			final Element xmlData) {
		final FieldComponent field = new FieldComponent(this, idX, idY, true, xmlData);

		// final Dimension relativeSize =
		// FieldFormatterSwing.getInstance().getSize(
		// getWidth(), getHeight(), idX, idY, cols, rows);

		final double[] pos = FieldFormatterSwing.getInstance().getPosition(fieldWidth, fieldHeight, idX, idY, true);
		field.setBounds((int) Math.rint(pos[0]), (int) Math.rint(pos[1]), fieldWidth, fieldHeight);

		components[idX][idY] = field;
		add(field);
	}

	/**
	 *
	 */
	private void createBackground() {
		final Image bgImage = ImageLoader.getBufferedImage(GameSession.gameOptions.getBoardBgImageName(), this);
		offscreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		if (bgImage != null) {
			final Graphics2D offscreenGraphics = offscreenImage.createGraphics();
			if (BG_IMAGE_TILED) {
				final Rectangle rect = new Rectangle(0, 0, bgImage.getWidth(null), bgImage.getHeight(null));
				offscreenGraphics.setPaint(new TexturePaint((BufferedImage) bgImage, rect));
				offscreenGraphics.fill(new Rectangle(0, 0, getWidth(), getHeight()));
			} else {
				offscreenGraphics.drawImage(bgImage, 0, 0, null);
			}
		}
	}

	/**
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @return FieldComponent
	 */
	public final FieldComponent getFieldComponent(final int idX, final int idY) {
		return components[idX][idY];
	}

	/**
	 * @return int
	 */
	protected int getSumFields() {
		return sumFields;
	}

	/**
	 * @return int
	 */
	protected int getSumFieldsFree() {
		return sumFieldsFree;
	}

	/**
	 * @param playerId
	 *            int
	 * @return int
	 */
	protected int getSumFieldsPlayer(final int playerId) {
		return sumFieldsPlayer[playerId];
	}

	/**
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param override
	 *            boolean
	 * @return boolean
	 */
	protected boolean isAllowed(final int idX, final int idY, final boolean override) {
		return parentPanel.checkClick(idX, idY, override);
	}

	/**
	 * @param playerId
	 *            int
	 * @return Player
	 */
	protected Player getPlayer(final int playerId) {
		if (playerId == -1) {
			return null;
		}
		return GameSession.gameOptions.getPlayer(playerId);
	}

	/**
	 * @return boolean
	 */
	protected boolean isEditMode() {
		return parentPanel.isEditMode();
	}

	/**
	 * @return boolean
	 */
	protected boolean isRepairMode() {
		return parentPanel.isRepairMode();
	}

	/**
	 * Diese Methode prueft die Gueltigkeit der Nachbarschaftsbeziehungen von
	 * diesem Feld aus zu dem uebergebenen Nachbarfeld. Falls das gewuenschte
	 * Feld ein gueltiger Nachbar ist, wird die Nachbarschaftsbeziehung als
	 * Wall-Objekt gespeichert.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param rel
	 *            FieldRelation
	 * @return Wall
	 */
	private Wall convertRelation(final int idX, final int idY, final FieldRelation rel) {
		final boolean destructable = true;
		FieldComponent refField = getFieldComponent(rel.getRefFieldX(), rel.getRefFieldY());
		if (refField != null && refField.isEnabled()) {
			final Wall wall = new Wall();
			wall.init(idX, idY, refField.getIdX(), refField.getIdY(), this, destructable);
			return wall;
		}
		return null;
	}

	/**
	 * Die Liste moeglicher Nachbarschaftsbeziehungen wird auf deaktivierte oder
	 * durch Waende abgeschottete Felder geprueft. Diese werden aussortiert und
	 * nur die aktiven Nachbarn werden in die Liste aufgenommen.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @return ArrayList<Field>
	 */
	public List<FieldComponent> getActiveRelations(final int idX, final int idY) {
		final ArrayList<FieldComponent> activeRelations = new ArrayList<>();

		for (final Wall wall : relations[idX][idY]) {
			if (wall != null && wall.isDestructable()) {
				activeRelations.add(getFieldComponent(wall.getField2x(), wall.getField2y()));
			}
		}
		return activeRelations;
	}

	/**
	 * Dieses Feld wird deaktiviert und die angrenzenden Waende (und somit die
	 * Nachbarschaftsbeziehung zu angrenzenden Feldern) entfernt.
	 * 
	 * TODO Hier ist noch Nacharbeit noetig. Derzeit werden die Referenzen
	 * doppelt (in beide Richtungen) gespeichert. Das fuehrt (wie hier zu sehen
	 * ist) zu unnoetigem Aufwand, da die Relationen aus beiden Richtungen
	 * entfernt werden muessen.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 */
	protected void disableField(final int idX, final int idY) {
		getFieldComponent(idX, idY).setEnabled(false);
		Wall relation1 = null;
		Wall relation2 = null;
		for (int i = 0; i < relations[idX][idY].length; i++) {
			// Alle Beziehungen anderer Felder zu diesem Feld entfernen
			relation1 = relations[idX][idY][i];
			if (relation1 == null) {
				continue;
			}
			for (int j = 0; j < relations[relation1.getField2x()][relation1.getField2y()].length; j++) {
				relation2 = relations[relation1.getField2x()][relation1.getField2y()][j];
				if (relation2 != null && relation2.getField2x() == idX && relation2.getField2y() == idY) {
					relations[relation1.getField2x()][relation1.getField2y()][j] = null;
				}
			}
			// Alle Beziehungen dieses Feldes zu anderen Feldern entfernen
			relations[idX][idY][i] = null;
		}
	}

	/**
	 * Neuberechnung der verschiedenen Feld-Counter und reset gesetzter Flags.
	 */
	protected void recalculate() {
		sumFields = 0;
		sumFieldsFree = 0;
		for (int i = 0; i < sumFieldsPlayer.length; i++) {
			sumFieldsPlayer[i] = 0;
		}

		FieldComponent field;
		for (int idY = 0; idY < GameSession.gameOptions.getNumFieldsHeight(); idY++) {
			for (int idX = 0; idX < GameSession.gameOptions.getNumFieldsWidth(); idX++) {
				// Nur aktive Felder werden gezaehlt
				field = getFieldComponent(idX, idY);
				if (field.isEnabled()) {
					sumFields++;
					final int owner = field.getOwnerId();
					if (owner == -1) {
						sumFieldsFree++;
					} else {
						sumFieldsPlayer[owner]++;
					}
				}
				field.resetAllowedClick();
			}
		}
	}

	/**
	 * Freigeben der Felder des angegebenen Spielers.
	 * 
	 * @param playerId
	 *            int
	 */
	protected void releasePlayer(final int playerId) {
		sumFieldsPlayer[playerId]++;
		for (int idY = 0; idY < GameSession.gameOptions.getNumFieldsHeight(); idY++) {
			for (int idX = 0; idX < GameSession.gameOptions.getNumFieldsWidth(); idX++) {
				if (getFieldComponent(idX, idY).getOwnerId() == playerId) {
					getFieldComponent(idX, idY).setOwner(-1);
					getFieldComponent(idX, idY).setValue(0);
					if (getFieldComponent(idX, idY).isEnabled()) {
						sumFieldsFree++;
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(final Graphics graphics) {
		graphics.drawImage(offscreenImage, 0, 0, this);
	}

	/**
	 * Liefert die Spielfeld-Daten als XML-Element zurueck.
	 * 
	 * @return Element
	 */
	protected Element getData() {
		final Element board = new Element("board");
		board.setAttribute("numFieldsWidth", Integer.toString(GameSession.gameOptions.getNumFieldsWidth()));
		board.setAttribute("numFieldsHeight", Integer.toString(GameSession.gameOptions.getNumFieldsHeight()));

		final ArrayList<Element> elements = new ArrayList<>();
		for (int idY = 0; idY < GameSession.gameOptions.getNumFieldsHeight(); idY++) {
			for (int idX = 0; idX < GameSession.gameOptions.getNumFieldsWidth(); idX++) {
				elements.add(getFieldComponent(idX, idY).getXmlData());
			}
		}
		board.addContent(elements);

		return board;
	}

	/**
	 * 
	 */
	protected void dispose() {
		for (int idY = 0; idY < GameSession.gameOptions.getNumFieldsHeight(); idY++) {
			for (int idX = 0; idX < GameSession.gameOptions.getNumFieldsWidth(); idX++) {
				getFieldComponent(idX, idY).dispose();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(final MouseEvent event) {
		// not implemented
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(final MouseEvent event) {
		if (mousePressed) {
			dragComp = event.getComponent();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(final MouseEvent event) {
		// not implemented
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(final MouseEvent event) {
		dragComp = event.getComponent();
		mousePressed = true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(final MouseEvent event) {
		if (dragComp != null && dragComp.equals(event.getComponent())) {
			parentPanel.boardClick(event);
		}
		mousePressed = false;
	}

	/**
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentHidden(final ComponentEvent arg0) {
		// not implemented
	}

	/**
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentMoved(final ComponentEvent arg0) {
		// not implemented
	}

	/**
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentResized(final ComponentEvent arg0) {
		createBackground();
		int[] relativeSize = FieldFormatterSwing.getInstance().getFieldsize(getWidth(), getHeight(), 0, 0,
				numFieldsWidth, numFieldsHeight);
		for (int idY = 0; idY < GameSession.gameOptions.getNumFieldsHeight(); idY++) {
			for (int idX = 0; idX < GameSession.gameOptions.getNumFieldsWidth(); idX++) {
				final double[] pos = FieldFormatterSwing.getInstance().getPosition(relativeSize[0], relativeSize[1],
						idX, idY, true);
				getFieldComponent(idX, idY).setBounds((int) Math.rint(pos[0]), (int) Math.rint(pos[1]), relativeSize[0],
						relativeSize[1]);
			}
		}
	}

	/**
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentShown(final ComponentEvent arg0) {
		// not implemented
	}

	/**
	 * @see java.awt.Component#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		FieldComponent fc;
		for (int idY = 0; idY < GameSession.gameOptions.getNumFieldsHeight(); idY++) {
			for (int idX = 0; idX < GameSession.gameOptions.getNumFieldsWidth(); idX++) {
				fc = getFieldComponent(idX, idY);
				if (fc.isEnabled()) {
					sb.append(idX).append("-");
					sb.append(idY).append("-");
					sb.append(fc.getValue());
				}
			}
		}
		return sb.toString();
	}
}