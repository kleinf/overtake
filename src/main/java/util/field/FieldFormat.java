package util.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jdom2.Element;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Administrator
 * 
 */
public class FieldFormat implements Comparable<FieldFormat> {

	private static final String FACTOR = "factor";
	private static final String OFFSET_X = "offsetX";
	private static final String OFFSET_Y = "offsetY";
	private static final String COL = "col";
	private static final String ROW = "row";
	private final Map<String, String> map = new HashMap<>();
	private Map<Integer, BorderFormat> borderFormats;
	private double minPosX = Double.MAX_VALUE;
	private double minPosY = Double.MAX_VALUE;
	private double maxPosX = Double.MIN_VALUE;
	private double maxPosY = Double.MIN_VALUE;

	/**
	 * Constructor.
	 * 
	 * @param id1
	 *            Id des Row- oder Col-Elements (je nachdem, was zuerst kommt)
	 * @param id2
	 *            Id des Row- oder Col-Elements (je nachdem, was danach kommt)
	 * @param element1
	 *            Row- oder Col-Element (je nachdem, was zuerst kommt)
	 * @param element2
	 *            Row- oder Col-Element (je nachdem, was danach kommt)
	 */
	protected FieldFormat(final int id1, final int id2, final Element element1, final Element element2) {

		final String name1 = element1.getName().toLowerCase();
		final String name2 = element2.getName().toLowerCase();
		map.put(name1, Integer.toString(id1));
		map.put(name2, Integer.toString(id2));
		map.put(name1 + FACTOR, element1.getAttributeValue(FACTOR));
		map.put(name2 + FACTOR, element2.getAttributeValue(FACTOR));
		map.put(name1 + OFFSET_X, element1.getAttributeValue(OFFSET_X));
		map.put(name2 + OFFSET_X, element2.getAttributeValue(OFFSET_X));
		map.put(name1 + OFFSET_Y, element1.getAttributeValue(OFFSET_Y));
		map.put(name2 + OFFSET_Y, element2.getAttributeValue(OFFSET_Y));
		parseBorders(element2);
	}

	/**
	 * Constructor.
	 * 
	 * @param id1
	 *            Id des Row- oder Col-Elements (je nachdem, was zuerst kommt)
	 * @param id2
	 *            Id des Row- oder Col-Elements (je nachdem, was danach kommt)
	 * @param key1
	 *            "row" oder "col" (je nachdem, was zuerst kommt)
	 * @param key2
	 *            "col" oder "row" (je nachdem, was danach kommt)
	 * @param object1
	 *            Row- oder Col-Element (je nachdem, was zuerst kommt)
	 * @param object2
	 *            Row- oder Col-Element (je nachdem, was danach kommt)
	 */
	protected FieldFormat(final int id1, final int id2, String key1, String key2, final JSONObject object1,
			final JSONObject object2) {

		final String name1 = key1.toLowerCase();
		final String name2 = key2.toLowerCase();
		map.put(name1, Integer.toString(id1));
		map.put(name2, Integer.toString(id2));
		map.put(name1 + FACTOR, object1.has(FACTOR) ? object1.get(FACTOR).toString() : "1");
		map.put(name2 + FACTOR, object2.has(FACTOR) ? object2.get(FACTOR).toString() : "1");
		map.put(name1 + OFFSET_X, object1.has(OFFSET_X) ? object1.get(OFFSET_X).toString() : "0");
		map.put(name2 + OFFSET_X, object2.has(OFFSET_X) ? object2.get(OFFSET_X).toString() : "0");
		map.put(name1 + OFFSET_Y, object1.has(OFFSET_Y) ? object1.get(OFFSET_Y).toString() : "0");
		map.put(name2 + OFFSET_Y, object2.has(OFFSET_Y) ? object2.get(OFFSET_Y).toString() : "0");
		parseBorders(object2);
	}

	/**
	 * Aufbereitung der XML-Datei mit den Feldbeschreibungen.
	 * 
	 * @param fieldElement
	 *            Element
	 */
	private void parseBorders(final Element fieldElement) {

		borderFormats = new TreeMap<Integer, BorderFormat>();
		for (final Element elementBorder : (List<Element>) fieldElement.getChildren("border")) {
			final BorderFormat borderFormat = new BorderFormat(Integer.parseInt(elementBorder.getAttributeValue("id")),
					Integer.parseInt(elementBorder.getAttributeValue("refBorderId")),
					Integer.parseInt(elementBorder.getAttributeValue("refFieldX")),
					Integer.parseInt(elementBorder.getAttributeValue("refFieldY")));
			final List<PointFormat> pointFormats = new ArrayList<>();
			for (final Element elementPoint : (List<Element>) elementBorder.getChildren()) {
				final PointType pt = PointType.valueOf(elementPoint.getName().toUpperCase());
				final double posX1 = Double.parseDouble(elementPoint.getAttributeValue("x1"));
				final double posY1 = Double.parseDouble(elementPoint.getAttributeValue("y1"));
				setMinMax(posX1, posY1);
				if (PointType.MOVE == pt) {
					pointFormats.add(new PointFormat(pt, posX1, posY1));
				}
				if (PointType.LINE == pt) {
					pointFormats.add(new PointFormat(pt, posX1, posY1));
				}
				if (PointType.QUAD == pt) {
					final double posX2 = Double.parseDouble(elementPoint.getAttributeValue("x2"));
					final double posY2 = Double.parseDouble(elementPoint.getAttributeValue("y2"));
					setMinMax(posX2, posY2);
					pointFormats.add(new PointFormat(pt, posX1, posY1, posX2, posY2));
				}
				if (PointType.CURVE == pt) {
					final double posX2 = Double.parseDouble(elementPoint.getAttributeValue("x2"));
					final double posY2 = Double.parseDouble(elementPoint.getAttributeValue("y2"));
					setMinMax(posX2, posY2);
					final double posX3 = Double.parseDouble(elementPoint.getAttributeValue("x3"));
					final double posY3 = Double.parseDouble(elementPoint.getAttributeValue("y3"));
					setMinMax(posX3, posY3);
					pointFormats.add(new PointFormat(pt, posX1, posY1, posX2, posY2, posX3, posY3));
				}
			}
			borderFormat.setPointFormats(pointFormats);
			borderFormats.put(Integer.valueOf(borderFormat.getId()), borderFormat);
		}
	}

	/**
	 * Aufbereitung der JSON-Datei mit den Feldbeschreibungen.
	 * 
	 * @param fieldObject
	 *            JSONObject
	 */
	private void parseBorders(final JSONObject fieldObject) {

		borderFormats = new TreeMap<Integer, BorderFormat>();
		if (fieldObject.has("border")) {
			JSONArray borders = fieldObject.getJSONArray("border");
			for (int i = 0; i < borders.length(); i++) {
				JSONObject elementBorder = borders.getJSONObject(i);
				final BorderFormat borderFormat = new BorderFormat(elementBorder.getInt("id"),
						elementBorder.getInt("refBorderId"), elementBorder.getInt("refFieldX"),
						elementBorder.getInt("refFieldY"));
				final List<PointFormat> pointFormats = new ArrayList<>();
				JSONArray walls = elementBorder.getJSONArray("wall");
				for (int j = 0; j < walls.length(); j++) {
					JSONObject elementPoint = walls.getJSONObject(j);
					final PointType pt = PointType.valueOf(elementPoint.getString("type").toUpperCase());
					final double posX1 = elementPoint.getDouble("x1");
					final double posY1 = elementPoint.getDouble("y1");
					setMinMax(posX1, posY1);
					if (PointType.MOVE == pt) {
						pointFormats.add(new PointFormat(pt, posX1, posY1));
					}
					if (PointType.LINE == pt) {
						pointFormats.add(new PointFormat(pt, posX1, posY1));
					}
					if (PointType.QUAD == pt) {
						final double posX2 = elementPoint.getDouble("x2");
						final double posY2 = elementPoint.getDouble("y2");
						setMinMax(posX2, posY2);
						pointFormats.add(new PointFormat(pt, posX1, posY1, posX2, posY2));
					}
					if (PointType.CURVE == pt) {
						final double posX2 = elementPoint.getDouble("x2");
						final double posY2 = elementPoint.getDouble("y2");
						setMinMax(posX2, posY2);
						final double posX3 = elementPoint.getDouble("x3");
						final double posY3 = elementPoint.getDouble("y3");
						setMinMax(posX3, posY3);
						pointFormats.add(new PointFormat(pt, posX1, posY1, posX2, posY2, posX3, posY3));
					}
				}
				borderFormat.setPointFormats(pointFormats);
				this.borderFormats.put(Integer.valueOf(borderFormat.getId()), borderFormat);
			}
		}
	}

	private void setMinMax(final double posX, final double posY) {

		this.minPosX = Math.min(this.minPosX, posX);
		this.minPosY = Math.min(this.minPosY, posY);
		this.maxPosX = Math.max(this.maxPosX, posX);
		this.maxPosY = Math.max(this.maxPosY, posY);
	}

	/**
	 * @return double
	 */
	protected double getMinMaxPosX() {

		return this.minPosX + this.maxPosX;
	}

	/**
	 * @return double
	 */
	protected double getMinMaxPosY() {

		return this.minPosY + this.maxPosY;
	}

	/**
	 * @param key
	 *            String
	 * @return double
	 */
	private double getValue(final String key) {

		double retVal = 0.0D;
		if (this.map.get(key) != null) {
			retVal = Double.parseDouble(this.map.get(key));
		}
		return retVal;
	}

	/**
	 * @return int
	 */
	protected int getColId() {

		int retVal = 0;
		if (this.map.get(COL) != null) {
			retVal = Integer.parseInt(this.map.get(COL));
		}
		return retVal;
	}

	/**
	 * @return int
	 */
	protected int getRowId() {

		int retVal = 0;
		if (this.map.get(ROW) != null) {
			retVal = Integer.parseInt(this.map.get(ROW));
		}
		return retVal;
	}

	/**
	 * Spalten-Abstand als Faktor 0 - 1 (komplette Feldbreite).
	 * 
	 * @return double
	 */
	protected double getColFactor() {

		return getValue(COL + FACTOR);
	}

	/**
	 * Zeilen-Abstand als Faktor 0 - 1 (komplette Feldhoehe).
	 * 
	 * @return double
	 */
	protected double getRowFactor() {

		return getValue(ROW + FACTOR);
	}

	/**
	 * Versetzung X-Achse als Faktor 0 - 1 (komplette Feldbreite).
	 * 
	 * @return double
	 */
	protected double getOffsetX() {

		return getValue(COL + OFFSET_X) + getValue(ROW + OFFSET_X);
	}

	/**
	 * Versetzung Y-Achse als Faktor 0 - 1 (komplette Feldhoehe).
	 * 
	 * @return double
	 */
	protected double getOffsetY() {

		return getValue(COL + OFFSET_Y) + getValue(ROW + OFFSET_Y);
	}

	/**
	 * @return boolean
	 */
	protected boolean isEmpty() {

		return borderFormats == null || borderFormats.isEmpty();
	}

	/**
	 * @return Map<Integer, BorderFormat>
	 */
	protected Map<Integer, BorderFormat> getBorderFormats() {

		return borderFormats;
	}

	/**
	 * Gibt nur die BorderFormat-Objekte zurueck, die eine gueltige
	 * Nachbarschaftsbeziehungen darstellen. Handelt es sich um ein randloses
	 * Spielbrett, muessen die Nachbarschaftsbeziehungen nicht gefiltert werden.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @param borderless
	 *            boolean
	 * @return Collection<BorderFormat>
	 */
	private Collection<BorderFormat> getBorderFormatsFiltered(final int idX, final int idY, final int maxX,
			final int maxY, final boolean borderless) {

		if (borderless) {
			return borderFormats.values();
		}

		final Map<String, BorderFormat> borderFormatsFiltered = new HashMap<>();
		// Alle Nachbarschaftsbeziehungen dieses Feldes durchlaufen
		for (final BorderFormat borderFormat : borderFormats.values()) {
			if (idX + borderFormat.getRefFieldX() >= 0 && idY + borderFormat.getRefFieldY() >= 0
					&& idX + borderFormat.getRefFieldX() < maxX && idY + borderFormat.getRefFieldY() < maxY) {
				borderFormatsFiltered.put(borderFormat.getKey(), borderFormat);
			}
		}
		return borderFormatsFiltered.values();
	}

	/**
	 * Gibt ein Array von Point-Objekten zurueck, das die IDs der moeglichen
	 * Nachbarfelder enthaelt.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @param borderless
	 *            boolean
	 * @return List<FieldRelation>
	 */
	protected List<FieldRelation> getRelations(final int idX, final int idY, final int maxX, final int maxY,
			final boolean borderless) {

		final Collection<BorderFormat> bf = getBorderFormatsFiltered(idX, idY, maxX, maxY, borderless);
		int x;
		int y;
		final List<FieldRelation> relations = new ArrayList<>();
		for (final BorderFormat borderFormat : bf) {
			x = idX + borderFormat.getRefFieldX();
			y = idY + borderFormat.getRefFieldY();
			// Bei Randueberlauf muessen die Nachbarfelder, die ueber den Rand
			// hinausgehen, auf die gegenueberliegende Seite umgeleitet werden.
			if (borderless) {
				if (x < 0) {
					x = maxX - 1;
				}
				if (x >= maxX) {
					x = 0;
				}
				if (y < 0) {
					y = maxY - 1;
				}
				if (y >= maxY) {
					y = 0;
				}
			}
			relations.add(new FieldRelation(borderFormat.getId(), borderFormat.getRefId(), x, y));
		}
		return relations;
	}

	/**
	 * Gibt die grafische Entsprechung des Feldes als Field-Objekt zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param sumColFactor
	 *            double
	 * @param sumRowFactor
	 *            double
	 * @param translate
	 *            boolean
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @param borderless
	 *            boolean
	 * @return Field
	 */
	public Field getField(final int width, final int height, final double sumColFactor, final double sumRowFactor,
			final boolean translate, final int idX, final int idY, final int maxX, final int maxY,
			final boolean borderless) {
		return new Field(idX, idY,
				getFieldParts(width, height, sumColFactor, sumRowFactor, translate, idX, idY, maxX, maxY, borderless));
	}

	/**
	 * Gibt die Position (linke obere Ecke) des Feldes als double[] zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param sumColFactor
	 *            double
	 * @param sumRowFactor
	 *            double
	 * @param translate
	 *            boolean
	 * @return double[]
	 */
	protected double[] getPosition(final int width, final int height, final double sumColFactor,
			final double sumRowFactor, final boolean translate) {

		// X - Koordinaten des Spaltenanfangs
		double pathPosBoardX = 0.0D;
		if (translate) {
			pathPosBoardX = width * sumColFactor;
		}
		// X - Abweichung der Zeile/Spalte
		double pathPosOffsetX = 0.0D;
		if (translate) {
			pathPosOffsetX = width * getOffsetX();
		}
		// Y - Koordinaten des Zeilenanfangs
		double pathPosBoardY = 0.0D;
		if (translate) {
			pathPosBoardY = height * sumRowFactor;
		}
		// Y - Abweichung innerhalb der Zeile/Spalte
		double pathPosOffsetY = 0.0D;
		if (translate) {
			pathPosOffsetY = height * getOffsetY();
		}
		final double pathPosSumX = pathPosBoardX + pathPosOffsetX;
		final double pathPosSumY = pathPosBoardY + pathPosOffsetY;
		return new double[] { pathPosSumX, pathPosSumY };
	}

	/**
	 * Gibt ein Array von Shape-Objekten zurueck, die jeweils den Linien
	 * entsprechen, die an ein Nachbarfeld angrenzen. Eine Grenze kann dabei aus
	 * mehr als einer Linie bestehen.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param sumColFactor
	 *            double
	 * @param sumRowFactor
	 *            double
	 * @param translate
	 *            boolean
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @param borderless
	 *            boolean
	 * @return Map<String, Shape>
	 */
	public Map<String, Shape> getWalls(final int width, final int height, final double sumColFactor,
			final double sumRowFactor, final boolean translate, final int idX, final int idY, final int maxX,
			final int maxY, final boolean borderless) {

		final double[] pos = getPosition(width, height, sumColFactor, sumRowFactor, translate);
		final Map<String, Shape> walls = new HashMap<>();
		for (final BorderFormat borderFormat : getBorderFormatsFiltered(idX, idY, maxX, maxY, borderless)) {
			walls.put(borderFormat.getKey(), borderFormat.getWall(width, height, pos[0], pos[1]));
		}
		return walls;
	}

	/**
	 * Ermittelt die Groesse einer FieldComponent relativ zur Groesse des
	 * Containers, so dass die Flaeche des Containers ausgefuellt wird.
	 * 
	 * @param containerWidth
	 *            int
	 * @param containerHeight
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @param maxOffsetX
	 *            double
	 * @param maxOffsetY
	 *            double
	 * @return int[]
	 */
	protected int[] getSize(final int containerWidth, final int containerHeight, final int maxX, final int maxY,
			final double maxOffsetX, final double maxOffsetY) {

		final int width = (int) Math.rint(containerWidth / ((maxX - 1.0D) * getColFactor() + 1.0D + maxOffsetX));
		final int height = (int) Math.rint(containerHeight / ((maxY - 1.0D) * getRowFactor() + 1.0D + maxOffsetY));
		return new int[] { width, height };
	}

	/**
	 * Gibt die Segmente mit der exakten Begrenzung zu den korrekten
	 * Nachbarfeldern zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param sumColFactor
	 *            double
	 * @param sumRowFactor
	 *            double
	 * @param translate
	 *            boolean
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @param borderless
	 *            boolean
	 * @return List<FieldPart>
	 */
	protected List<FieldPart> getFieldParts(final int width, final int height, final double sumColFactor,
			final double sumRowFactor, final boolean translate, final int idX, final int idY, final int maxX,
			final int maxY, final boolean borderless) {

		final double[] pos = getPosition(width, height, sumColFactor, sumRowFactor, translate);
		final double centerX = pos[0] + width * getMinMaxPosX() * 0.5D;
		final double centerY = pos[1] + height * getMinMaxPosY() * 0.5D;

		FieldRelation fieldRelation;
		Shape segment;
		Shape wall;
		boolean possibleBorder;
		// Alle Nachbarschaftsbeziehungen dieses Feldes durchlaufen
		int x;
		int y;
		final List<FieldPart> fieldParts = new ArrayList<FieldPart>();
		for (final BorderFormat borderFormat : getBorderFormats().values()) {
			x = idX + borderFormat.getRefFieldX();
			y = idY + borderFormat.getRefFieldY();
			// Nachbarschaftsbeziehungen dieses Feldes pruefen
			possibleBorder = borderless || (x >= 0 && y >= 0 && x < maxX && y < maxY);

			// Bei Randueberlauf muessen die Nachbarfelder, die ueber den Rand
			// hinausgehen, auf die gegenueberliegende Seite umgeleitet werden.
			if (borderless) {
				if (x < 0) {
					x = maxX - 1;
				}
				if (x >= maxX) {
					x = 0;
				}
				if (y < 0) {
					y = maxY - 1;
				}
				if (y >= maxY) {
					y = 0;
				}
			}
			fieldRelation = null;
			segment = null;
			wall = borderFormat.getWall(width, height, pos[0], pos[1]);
			if (possibleBorder) {
				fieldRelation = new FieldRelation(borderFormat.getId(), borderFormat.getRefId(), x, y);
				segment = new Shape();
				segment.moveTo(centerX, centerY);
				segment.append(wall, true);
				segment.closePath();
			}
			fieldParts.add(new FieldPart(fieldRelation, segment, wall));
		}
		return fieldParts;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final FieldFormat object) {

		int retVal = 0;
		if (object != this) {
			if (getColId() == object.getColId()) {
				if (getRowId() != object.getRowId()) {
					if (getRowId() < object.getRowId()) {
						retVal = -1;
					} else {
						retVal = 1;
					}
				}
			} else {
				if (getColId() < object.getColId()) {
					retVal = -1;
				} else {
					retVal = 1;
				}
			}
		}
		return retVal;
	}
}