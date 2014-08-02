package swing.gui.options;

import game.GameSession;

import java.util.Map;
import java.util.Map.Entry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import swing.util.FieldFormatterSwing;
import swing.util.FontCreator;

/**
 * @author Administrator
 * 
 */
public class BoardLayoutOptDlgPnl extends JDialog {

	private static final long serialVersionUID = 1L;
	private final BoardOptionDialogPanel parentDialog;
	private PaintPanel paintPanel = null;
	int fieldWidth = 0;
	int fieldHeight = 0;
	int numFieldsWidth = 0;
	int numFieldsHeight = 0;
	JSlider jSliderW = null;
	JSlider jSliderH = null;
	JSlider jSliderX = null;
	JSlider jSliderY = null;

	/**
	 * @param parentDialog
	 *            BoardOptionDialogPanel
	 */
	protected BoardLayoutOptDlgPnl(final BoardOptionDialogPanel parentDialog) {
		super();
		this.parentDialog = parentDialog;
		setName("Board layout");
		setTitle("Board layout");
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// Esc closes dialog
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Close");
		getRootPane().getActionMap().put("Close", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});

		fieldWidth = GameSession.gameOptions.getFieldWidth();
		fieldHeight = GameSession.gameOptions.getFieldHeight();
		numFieldsWidth = GameSession.gameOptions.getNumFieldsWidth();
		numFieldsHeight = GameSession.gameOptions.getNumFieldsHeight();
		getJSliderW().setValue(-1);
		getJSliderH().setValue(-1);
		getJSliderX().setValue(numFieldsWidth);
		getJSliderY().setValue(numFieldsHeight);

		setSize(500, 500);
		setPreferredSize(getSize());
		setLocationRelativeTo(getParent());

		setName("JContentPane");
		setLayout(new BorderLayout());
		add(getJSliderW(), BorderLayout.NORTH);
		add(getJSliderH(), BorderLayout.WEST);
		add(getJSliderX(), BorderLayout.SOUTH);
		add(getJSliderY(), BorderLayout.EAST);
		add(getPaintPanel(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jSliderW.
	 * 
	 * @return JSlider
	 */
	final JSlider getJSliderW() {
		if (jSliderW == null) {
			jSliderW = new JSlider();
			jSliderW.setName("JSliderW");
			jSliderW.setMinimum(-1);
			jSliderW.setMaximum(100);
			jSliderW.setToolTipText(Integer.toString(jSliderW.getValue()));
			jSliderW.setOrientation(SwingConstants.HORIZONTAL);
			jSliderW.setInverted(false);
			jSliderW.setSnapToTicks(true);
			jSliderW.setPaintTicks(true);
			jSliderW.setPaintLabels(true);
			jSliderW.createStandardLabels(10);
			jSliderW.addChangeListener(new ChangeListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
				 */
				@Override
				public void stateChanged(final ChangeEvent event) {
					jSliderW.setToolTipText(Integer.toString(jSliderW
							.getValue()));
					repaint();
				}
			});
		}
		return jSliderW;
	}

	/**
	 * This method initializes jSliderH.
	 * 
	 * @return JSlider
	 */
	final JSlider getJSliderH() {
		if (jSliderH == null) {
			jSliderH = new JSlider();
			jSliderH.setName("JSliderH");
			jSliderH.setMinimum(-1);
			jSliderH.setMaximum(100);
			jSliderH.setToolTipText(Integer.toString(jSliderH.getValue()));
			jSliderH.setOrientation(SwingConstants.VERTICAL);
			jSliderH.setInverted(true);
			jSliderH.setSnapToTicks(true);
			jSliderH.setPaintTicks(true);
			jSliderH.setPaintLabels(true);
			jSliderH.createStandardLabels(10);
			jSliderH.addChangeListener(new ChangeListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
				 */
				@Override
				public void stateChanged(final ChangeEvent event) {
					jSliderH.setToolTipText(Integer.toString(jSliderH
							.getValue()));
					repaint();
				}
			});
		}
		return jSliderH;
	}

	/**
	 * This method initializes jSliderX.
	 * 
	 * @return JSlider
	 */
	final JSlider getJSliderX() {
		if (jSliderX == null) {
			jSliderX = new JSlider();
			jSliderX.setName("JSliderX");
			jSliderX.setMinimum(1);
			jSliderX.setMaximum(25);
			jSliderX.setToolTipText(Integer.toString(jSliderX.getValue()));
			jSliderX.setOrientation(SwingConstants.HORIZONTAL);
			jSliderX.setInverted(false);
			jSliderX.addChangeListener(new ChangeListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
				 */
				@Override
				public void stateChanged(final ChangeEvent event) {
					jSliderX.setToolTipText(Integer.toString(jSliderX
							.getValue()));
					repaint();
				}
			});
		}
		return jSliderX;
	}

	/**
	 * This method initializes jSliderY.
	 * 
	 * @return JSlider
	 */
	final JSlider getJSliderY() {
		if (jSliderY == null) {
			jSliderY = new JSlider();
			jSliderY.setName("JSliderY");
			jSliderY.setMinimum(1);
			jSliderY.setMaximum(25);
			jSliderY.setToolTipText(Integer.toString(jSliderY.getValue()));
			jSliderY.setOrientation(SwingConstants.VERTICAL);
			jSliderY.setInverted(true);
			jSliderY.addChangeListener(new ChangeListener() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
				 */
				@Override
				public void stateChanged(final ChangeEvent event) {
					jSliderY.setToolTipText(Integer.toString(jSliderY
							.getValue()));
					repaint();
				}
			});
		}
		return jSliderY;
	}

	/**
	 * This method initializes paintPanel.
	 * 
	 * @return PaintPanel
	 */
	private PaintPanel getPaintPanel() {
		if (paintPanel == null) {
			paintPanel = new PaintPanel(this);
		}
		return paintPanel;
	}

	/**
	 * This method re-initializes the paintPanel.
	 */
	protected void reset() {
		paintPanel.init();
	}

	/**
	 * @return String
	 */
	String getFieldName() {
		return (String) parentDialog.getJCbFieldName().getSelectedItem();
	}

	/**
	 * @return boolean
	 */
	boolean isBorderless() {
		return parentDialog.getJCbBorderless().isSelected();
	}

	/**
	 * @author Administrator
	 * 
	 */
	private class PaintPanel extends JPanel implements MouseListener {

		private static final long serialVersionUID = 1L;
		private final BoardLayoutOptDlgPnl parentPanel;
		private boolean isBorderless;
		private boolean showSegments = false;
		private boolean showArrows = true;
		private boolean showBorderRefs = false;

		/**
		 * Constructor.
		 * 
		 * @param parentPanel
		 *            BoardLayoutOptDlgPnl
		 */
		PaintPanel(final BoardLayoutOptDlgPnl parentPanel) {
			super();
			this.parentPanel = parentPanel;
			init();
			addMouseListener(this);
		}

		/**
		 * This method initializes paintPanel.
		 */
		protected final void init() {
			FieldFormatterSwing.getInstance().init(parentPanel.getFieldName());
			isBorderless = parentPanel.isBorderless();
			int[] size = FieldFormatterSwing.getInstance().getBoardsize(
					fieldWidth, fieldHeight, getJSliderX().getValue(),
					getJSliderY().getValue());
			final double maxWidth = size[0]
					+ parentPanel.getJSliderH().getPreferredSize().getWidth()
					+ parentPanel.getJSliderY().getPreferredSize().getWidth()
					+ 10;
			final double maxHeight = size[1]
					+ parentPanel.getJSliderW().getPreferredSize().getHeight()
					+ parentPanel.getJSliderX().getPreferredSize().getHeight()
					+ 30;
			parentPanel.setSize((int) Math.rint(maxWidth),
					(int) Math.rint(maxHeight));
			parentPanel.setPreferredSize(getSize());
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see javax.swing.JComponent#paintComponent(Graphics)
		 */
		@Override
		protected void paintComponent(final Graphics graphics) {
			final Graphics2D g2d = (Graphics2D) graphics;
			final BufferedImage bufImg = new BufferedImage(getWidth(),
					getHeight(), BufferedImage.TYPE_INT_ARGB);
			final Graphics2D bufG2d = (Graphics2D) bufImg.getGraphics();
			bufG2d.setColor(Color.BLACK);
			numFieldsWidth = parentPanel.getJSliderX().getValue();
			numFieldsHeight = parentPanel.getJSliderY().getValue();
			fieldWidth = parentPanel.getJSliderW().getValue();
			fieldHeight = parentPanel.getJSliderH().getValue();
			Rectangle bounds;
			GeneralPath poly;
			String value;
			bufG2d.setFont(FontCreator.createFont("fonts/arial.ttf", Font.BOLD,
					12.0F));
			final FontMetrics fontMetrics = getFontMetrics(bufG2d.getFont());
			final int fontHeight = fontMetrics.getHeight();
			if (fieldWidth == -1 || fieldHeight == -1) {
				int[] size = FieldFormatterSwing.getInstance().getFieldsize(
						getWidth(), getHeight(), 0, 0, numFieldsWidth,
						numFieldsHeight);
				if (fieldWidth == -1) {
					fieldWidth = size[0];
				}
				if (fieldHeight == -1) {
					fieldHeight = size[1];
				}
			}
			for (int idY = 0; idY < numFieldsHeight; idY++) {
				for (int idX = 0; idX < numFieldsWidth; idX++) {
					if (!FieldFormatterSwing.getInstance().isEmpty(idX, idY)) {
						poly = FieldFormatterSwing.getInstance().getPolygon(
								fieldWidth, fieldHeight, true, idX, idY,
								numFieldsWidth, numFieldsHeight);
						if (showSegments) {
							drawSegments(idX, idY, g2d, bufG2d, fontMetrics,
									fontHeight);
						}
						if (showArrows) {
							drawArrows(idX, idY, g2d, bufG2d, fontMetrics,
									fontHeight);
						}
						bufG2d.draw(poly);
						value = idX + "/" + idY;
						bounds = poly.getBounds();
						bufG2d.drawString(value,
								(int) Math.rint(bounds.getCenterX())
										- fontMetrics.stringWidth(value) / 2,
								(int) Math.round(bounds.getCenterY())
										+ fontHeight / 3);
					}
				}
			}
			g2d.drawImage(bufImg, 0, 0, this);
		}

		private void drawSegments(final int idX, final int idY,
				final Graphics2D g2d, final Graphics2D bufG2d,
				final FontMetrics fontMetrics, final int fontHeight) {
			final Map<String, GeneralPath> segments = FieldFormatterSwing
					.getInstance().getSegments(fieldWidth, fieldHeight, true,
							idX, idY, numFieldsWidth, numFieldsHeight,
							isBorderless);
			for (final Entry<String, GeneralPath> entry : segments.entrySet()) {
				final String[] borders = entry.getKey().split("\\|");
				final int borderId = Integer.parseInt(borders[0]);
				final int refBorderId = Integer.parseInt(borders[1]);
				final int refFieldIdX = idX + Integer.parseInt(borders[2]);
				final int refFieldIdY = idY + Integer.parseInt(borders[3]);
				final GeneralPath path = entry.getValue();
				if (path != null) {
					if (borderId % 8 == 0) {
						g2d.setColor(Color.PINK);
					} else if (borderId % 7 == 0) {
						g2d.setColor(Color.ORANGE);
					} else if (borderId % 6 == 0) {
						g2d.setColor(Color.YELLOW);
					} else if (borderId % 5 == 0) {
						g2d.setColor(Color.MAGENTA);
					} else if (borderId % 4 == 0) {
						g2d.setColor(Color.CYAN);
					} else if (borderId % 3 == 0) {
						g2d.setColor(Color.BLUE);
					} else if (borderId % 2 == 0) {
						g2d.setColor(Color.GREEN);
					} else {
						g2d.setColor(Color.RED);
					}
					g2d.fill(path);
					if (showBorderRefs) {
						final String borderConnection = borderId + "->"
								+ refBorderId;
						final String fieldConnection = "(" + refFieldIdX + "/"
								+ refFieldIdY + ")";
						final Rectangle bounds = path.getBounds();
						bufG2d.drawString(
								borderConnection,
								(int) Math.rint(bounds.getCenterX())
										- fontMetrics
												.stringWidth(borderConnection)
										/ 2,
								(int) Math.round(bounds.getCenterY())
										- fontHeight / 3);
						bufG2d.drawString(
								fieldConnection,
								(int) Math.rint(bounds.getCenterX())
										- fontMetrics
												.stringWidth(fieldConnection)
										/ 2,
								(int) Math.round(bounds.getCenterY())
										+ fontHeight / 2);
					}
				}
			}
		}

		private void drawArrows(final int idX, final int idY,
				final Graphics2D g2d, final Graphics2D bufG2d,
				final FontMetrics fontMetrics, final int fontHeight) {
			final Map<String, GeneralPath> arrows = FieldFormatterSwing
					.getInstance().getArrows(fieldWidth, fieldHeight, true,
							idX, idY, numFieldsWidth, numFieldsHeight,
							isBorderless);
			g2d.setColor(Color.RED);
			for (final Entry<String, GeneralPath> entry : arrows.entrySet()) {
				final String[] temp = entry.getKey().split("\\|");
				final GeneralPath path = entry.getValue();
				if (path != null) {
					g2d.draw(path);
					if (showBorderRefs) {
						final String value1 = temp[0] + "->" + temp[1];
						final String value2 = "("
								+ (idX + Integer.parseInt(temp[2])) + "/"
								+ (idY + Integer.parseInt(temp[3])) + ")";
						final Rectangle bounds = path.getBounds();
						bufG2d.drawString(value1,
								(int) Math.rint(bounds.getCenterX())
										- fontMetrics.stringWidth(value1) / 2,
								(int) Math.round(bounds.getCenterY())
										- fontHeight / 4);
						bufG2d.drawString(value2,
								(int) Math.rint(bounds.getCenterX())
										- fontMetrics.stringWidth(value2) / 2,
								(int) Math.round(bounds.getCenterY())
										+ fontHeight);
					}
				}
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(final MouseEvent mouseEvent) {
			if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
				showArrows = !showArrows;
				showSegments = !showSegments;
			}
			if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
				showBorderRefs = !showBorderRefs;
			}
			parentPanel.repaint();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(final MouseEvent mouseEvent) {
			// not implemented
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(final MouseEvent mouseEvent) {
			// not implemented
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
	}

	/**
	 *
	 */
	protected void updateOptions() {
		GameSession.gameOptions.setNumFieldsWidth(numFieldsWidth);
		GameSession.gameOptions.setNumFieldsHeight(numFieldsHeight);
		GameSession.gameOptions.setFieldWidth(fieldWidth);
		GameSession.gameOptions.setFieldHeight(fieldHeight);
		GameSession.gameOptions
				.setFieldWidthRelative(getJSliderW().getValue() == -1);
		GameSession.gameOptions
				.setFieldHeightRelative(getJSliderH().getValue() == -1);
	}
}
