package swing.gui;

import java.util.Map;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

import org.jdom2.Element;

import game.GameSession;
import swing.util.AnimatedImage;
import swing.util.AnimatedImageUtil;
import swing.util.FieldFormatterSwing;
import swing.util.FontCreator;

/**
 * @author Administrator
 * 
 */
public class FieldComponent extends JComponent implements MouseListener, Runnable {

	private static final long serialVersionUID = 1L;
	private final BoardPanel boardPanel;
	private final int idX;
	private final int idY;
	private int value;
	private int ownerId;
	private AnimatedImage ownerImage;
	private final AnimatedImage defaultImage;
	private int overloads;
	private int rounds;
	private Boolean allowedClick = null;
	private Boolean allowedRepair = null;
	private GeneralPath polygon;
	private Map<String, GeneralPath> segments;
	private final Color colorAllowed;
	private final Color colorNotAllowed;
	private final Color lineColorEdit;
	private boolean mouseover = false;
	private final transient AlphaComposite fieldAlpha;
	private final transient AlphaComposite alpha100 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F);
	private final transient AlphaComposite alpha75 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75F);
	private final transient AlphaComposite alpha50 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F);
	private final Font font;
	private final FontMetrics fontMetrics;
	private int centerX;
	private int centerY;
	private int frame = 0;
	private Thread thread = null;

	/**
	 * @param parentPanel
	 *            BoardPanel
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param enabled
	 *            boolean
	 * @param xmlData
	 *            Element
	 */
	protected FieldComponent(final BoardPanel parentPanel, final int idX, final int idY, final boolean enabled,
			final Element xmlData) {
		super();
		boardPanel = parentPanel;
		this.idX = idX;
		this.idY = idY;
		defaultImage = AnimatedImageUtil.createMyImage(Color.WHITE);
		if (xmlData == null) {
			value = 0;
			setOwner(-1);
			setEnabled(enabled && !FieldFormatterSwing.getInstance().isEmpty(idX, idY));
			overloads = 0;
			rounds = 0;
		} else {
			value = Integer.parseInt(xmlData.getAttributeValue("value"));
			setOwner(Integer.parseInt(xmlData.getAttributeValue("ownerId")));
			setEnabled(Boolean.parseBoolean(xmlData.getAttributeValue("enabled")));
			overloads = Integer.parseInt(xmlData.getAttributeValue("overloads"));
			rounds = Integer.parseInt(xmlData.getAttributeValue("rounds"));
		}
		colorAllowed = Color.GREEN;
		colorNotAllowed = Color.RED;
		lineColorEdit = Color.GREEN;

		setSize(GameSession.gameOptions.getFieldWidth(), GameSession.gameOptions.getFieldHeight());
		setPreferredSize(getSize());

		fieldAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, GameSession.gameOptions.getFieldAlpha());
		font = FontCreator.createFont("fonts/arial.ttf", Font.BOLD, 12.0F);
		fontMetrics = boardPanel.getFontMetrics(font);

		addMouseListener(this);
		addMouseListener(boardPanel);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */
	@Override
	public void setBounds(final int posX, final int posY, final int width, final int height) {

		/*
		 * segments = FieldFormatter.getInstance().getSegments(width, height,
		 * idX, idY, GameSession.gameOptions.getNumFieldsWidth(),
		 * GameSession.gameOptions.getNumFieldsHeight(),
		 * GameSession.gameOptions.isBorderless(), false);
		 */

		// Die Darstellung des Polygons an die geaenderte Groesse anpassen
		polygon = FieldFormatterSwing.getInstance().getPolygon(width, height, false, idX, idY,
				GameSession.gameOptions.getNumFieldsWidth(), GameSession.gameOptions.getNumFieldsHeight(),
				GameSession.gameOptions.isBorderless());

		// Die Grenzen des Feldes auf die Groesse des Polygons anpassen
		super.setBounds(posX, posY, Math.max(width, polygon.getBounds().width),
				Math.max(height, polygon.getBounds().height));

		centerX = (int) polygon.getBounds().getCenterX();
		centerY = (int) polygon.getBounds().getCenterY();

		ownerImage.resize(getWidth(), getHeight());
	}

	/**
	 * Setzt das allowedClick-Flag zurueck.
	 */
	protected void resetAllowedClick() {
		allowedClick = null;
	}

	/**
	 * Setzt das allowedRepair-Flag zurueck.
	 */
	protected void resetAllowedRepair() {
		allowedRepair = null;
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
	 * @param value
	 *            int
	 */
	protected void setValue(final int value) {
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
	protected void addValue() {
		value++;
	}

	/**
	 * @return int
	 */
	public int getMaxValue() {
		// Die Maximalkapazitaet eines Feldes wird durch die Anzahl seiner
		// Nachbarn definiert
		return boardPanel.getActiveRelations(idX, idY).size();
	}

	/**
	 * @param playerId
	 *            int
	 */
	protected void setOwner(final int playerId) {
		if (playerId == -1) {
			ownerImage = defaultImage.getResizedCopy(getWidth(), getHeight());
			setRunning(false);
		}
		ownerId = playerId;
		if (playerId > -1) {
			ownerImage = boardPanel.getPlayer(playerId).getPlayerImage().getResizedCopy(getWidth(), getHeight());
			setRunning(true);
		}
	}

	/**
	 * @return int
	 */
	public int getOwnerId() {
		return ownerId;
	}

	/**
	 * @param overloads
	 *            int
	 */
	protected void setOverloads(final int overloads) {
		this.overloads = overloads;
	}

	/**
	 * @return int
	 */
	protected int getOverloads() {
		return overloads;
	}

	/**
	 * 
	 */
	protected void addOverload() {
		overloads++;
	}

	/**
	 * 
	 */
	protected void subOverload() {
		overloads--;
	}

	/**
	 * @param rounds
	 *            int
	 */
	protected void setRounds(final int rounds) {
		this.rounds = rounds;
	}

	/**
	 * @return int
	 */
	protected int getRounds() {
		return rounds;
	}

	/**
	 * 
	 */
	protected void addRound() {
		rounds++;
	}

	/**
	 * @param override
	 *            boolean
	 * @return boolean
	 */
	public boolean isAllowedClick(final boolean override) {
		if (allowedClick == null) {
			allowedClick = Boolean.valueOf(boardPanel.isAllowed(idX, idY, override));
		}
		return allowedClick.booleanValue();
	}

	/**
	 * @param override
	 *            boolean
	 * @return boolean
	 */
	private boolean isAllowedRepair(final boolean override) {
		if (allowedRepair == null) {
			allowedRepair = Boolean.valueOf(boardPanel.isAllowed(idX, idY, override));
		}
		return allowedRepair.booleanValue();
	}

	/**
	 * @return Element
	 */
	protected Element getXmlData() {
		final Element field = new Element("field");
		field.setAttribute("idX", Integer.toString(idX));
		field.setAttribute("idY", Integer.toString(idY));
		field.setAttribute("value", Integer.toString(value));
		field.setAttribute("ownerId", Integer.toString(ownerId));
		field.setAttribute("enabled", Boolean.toString(isEnabled()));
		field.setAttribute("overloads", Integer.toString(overloads));
		field.setAttribute("rounds", Integer.toString(rounds));
		return field;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.swing.JComponent#contains(int, int)
	 */
	@Override
	public boolean contains(final int posX, final int posY) {
		return polygon.contains(posX, posY);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(final MouseEvent mouseEvent) {
		// not implemented
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(final MouseEvent mouseEvent) {
		// Mauszeiger hat sich vorher nicht in diesem Polygon befunden
		// Ein Neuzeichnen ist notwendig
		mouseover = true;
		setRunning(false);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(final MouseEvent mouseEvent) {
		// Mauszeiger hat sich vorher in diesem Polygon befunden
		// Ein Neuzeichnen ist notwendig
		mouseover = false;
		setRunning(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(final MouseEvent mouseEvent) {
		// not implemented
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(final MouseEvent mouseEvent) {
		// not implemented
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(final Graphics graphics) {
		// Nur zeichnen wenn Felder existieren oder wir uns im EditMode befinden
		if (isEnabled() || boardPanel.isEditMode()) {
			final Graphics2D g2d = (Graphics2D) graphics;
			Color lineColor = Color.BLACK;
			Color fillColor = Color.WHITE;

			if (GameSession.gameOptions.getMaxOverload() == 0) {
				g2d.setComposite(fieldAlpha);
			} else {
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
						1.0F - (float) getOverloads() / GameSession.gameOptions.getMaxOverload()));
			}
			if (isEnabled()) {
				if (!ownerImage.isAnim()) {
					frame = 0;
				}
				g2d.setClip(polygon);
				g2d.drawImage(ownerImage.getFrame(frame), 0, 0, this);
				g2d.setClip(null);
			}
			g2d.setComposite(alpha100);

			if (mouseover) {
				if (boardPanel.isEditMode()) {
					lineColor = lineColorEdit;
				} else if (boardPanel.isRepairMode()) {
					lineColor = isAllowedRepair(false) ? colorAllowed : colorNotAllowed;
				} else {
					fillColor = isAllowedClick(false) ? colorAllowed : colorNotAllowed;
					g2d.setComposite(alpha50);
					g2d.setColor(fillColor);
					g2d.fill(polygon);
					g2d.setComposite(alpha100);
				}
			}

			g2d.setColor(lineColor);
			g2d.draw(polygon);

			if (isEnabled()) {
				g2d.setFont(font);
				final int width = fontMetrics.stringWidth(Integer.toString(value));
				final int height = fontMetrics.getHeight();
				final int posX = centerX - width / 2;
				final int posY = centerY + height / 3;
				g2d.setColor(Color.WHITE);
				g2d.setComposite(alpha75);
				g2d.fillRect(posX - 1, posY - height / 2 - 5, width + 2, height / 2 + 7);
				g2d.setComposite(alpha100);
				g2d.setColor(lineColor);
				g2d.drawString(Integer.toString(value), posX, posY);
			}
		}
	}

	/**
	 * @return boolean
	 */
	private boolean isRunning() {
		return thread != null && thread.isAlive() && !thread.isInterrupted();
	}

	/**
	 * @param running
	 *            boolean
	 */
	private void setRunning(final boolean running) {
		if (ownerImage.isAnim() && running) {
			if (thread == null) {
				thread = new Thread(this, "Field-Animation");
				thread.start();
			} else if (thread.isInterrupted()) {
				thread.start();
			}
		} else if (thread != null && !thread.isInterrupted()) {
			thread.interrupt();
			thread = null;
		}
		repaint();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (isRunning()) {
			repaint();
			frame += 1;
			if (frame >= ownerImage.getNumFrames()) {
				frame = 0;
			}
			try {
				Thread.sleep(ownerImage.getFrameDelay(frame));
			} catch (final InterruptedException exception) {
				return;
			}
		}
	}

	/**
	 * 
	 */
	protected void dispose() {
		if (thread != null && !thread.isInterrupted()) {
			thread.interrupt();
			thread = null;
		}
	}
}
