package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.awt.Dimension;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Administrator
 * 
 */
public final class FieldFormatter {

	private int maxBorders = 0;
	private FieldFormat[][] fieldFormats = null;
	private boolean colFirst = false;
	private boolean rowFirst = false;
	private static FieldFormatter formatter;

	/**
	 * Constructor.
	 */
	private FieldFormatter() {
		// Singleton --> FieldFormatter.getInstance()
	}

	/**
	 * @return FieldFormatter
	 */
	public static synchronized FieldFormatter getInstance() {

		if (formatter == null) {
			formatter = new FieldFormatter();
		}
		return formatter;
	}

	/**
	 * FieldFormat-Objekt einlesen.
	 * 
	 * @param fileName
	 *            String Dateiname.
	 */
	public void init(final String fileName) {

		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			if (fileName == null) {
				return;
			}
			fileReader = new FileReader(
					fileName
							+ ((fileName.toLowerCase().lastIndexOf(".field") == -1
									&& fileName.toLowerCase().lastIndexOf(
											".xml") == -1 && fileName
									.toLowerCase().lastIndexOf(".json") == -1) ? ".field"
									: ""));
			bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			final StringBuilder stringBuilder = new StringBuilder();
			while (line != null) {
				stringBuilder.append(line);
				line = bufferedReader.readLine();
			}
			String data = stringBuilder.toString();
			if (data.trim().startsWith("<?xml")) {
				initXml(getXmlObject(data));
			} else {
				initJson(getJsonObject(data));
			}
		} catch (final IOException exception) {
			Logger.getInstance().log(exception.getMessage());
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (final IOException exception) {
					Logger.getInstance().log(exception.getMessage());
				}
			}
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (final IOException exception) {
					Logger.getInstance().log(exception.getMessage());
				}
			}
		}
	}

	/**
	 * Umwandeln der XML-Daten in ein XML-Objekt.
	 * 
	 * @param xmlData
	 *            String
	 * @return Document
	 */
	private Document getXmlObject(String xmlData) {

		StringReader stringReader = null;
		try {
			stringReader = new StringReader(xmlData);
			return new SAXBuilder().build(stringReader);
		} catch (final IOException exception) {
			Logger.getInstance().log(exception.getMessage());
		} catch (final JDOMException exception) {
			Logger.getInstance().log(exception.getMessage());
		} finally {
			if (stringReader != null) {
				stringReader.close();
			}
		}
		return null;
	}

	/**
	 * Initialisieren des FieldFormatter mit Feldbeschreibungen im XML-Format.
	 * 
	 * @param document
	 *            Document
	 */
	private void initXml(final Document document) {

		Element el0;
		List<Element> list1;
		Element el1;
		List<Element> list2;
		Element el2;
		FieldFormat fieldFormat;
		el0 = document.getRootElement();
		list1 = el0.getChildren();
		colFirst = "col".equalsIgnoreCase(list1.get(0).getName());
		rowFirst = "row".equalsIgnoreCase(list1.get(0).getName());
		fieldFormats = new FieldFormat[list1.size()][];
		for (int i = 0; i < list1.size(); i++) {
			el1 = list1.get(i);
			list2 = el1.getChildren();
			fieldFormats[i] = new FieldFormat[list2.size()];
			for (int j = 0; j < list2.size(); j++) {
				el2 = list2.get(j);
				fieldFormat = new FieldFormat(i, j, el1, el2);
				maxBorders = Math.max(fieldFormat.getBorderFormats().size(),
						maxBorders);
				fieldFormats[i][j] = fieldFormat;
			}
		}
	}

	/**
	 * Umwandeln der JSON-Daten in ein JSON-Objekt.
	 * 
	 * @param jsonData
	 *            String
	 * @return JSONObject
	 */
	private JSONObject getJsonObject(String jsonData) {
		return new JSONObject(jsonData);
	}

	/**
	 * Initialisieren des FieldFormatter mit Feldbeschreibungen im JSON-Format.
	 * 
	 * @param object
	 *            JSONObject
	 */
	private void initJson(final JSONObject object) {

		JSONObject el0;
		JSONArray list1;
		JSONObject el1;
		JSONArray list2;
		JSONObject el2;
		FieldFormat fieldFormat;
		el0 = object.getJSONObject("field");
		String key = el0.keys().next();
		colFirst = "col".equalsIgnoreCase(key);
		rowFirst = "row".equalsIgnoreCase(key);
		String key1 = colFirst ? "col" : "row";
		String key2 = rowFirst ? "col" : "row";
		list1 = el0.getJSONArray(key1);
		fieldFormats = new FieldFormat[list1.length()][];
		for (int i = 0; i < list1.length(); i++) {
			el1 = list1.getJSONObject(i);
			list2 = el1.getJSONArray(key2);
			fieldFormats[i] = new FieldFormat[list2.length()];
			for (int j = 0; j < list2.length(); j++) {
				el2 = list2.getJSONObject(j);
				fieldFormat = new FieldFormat(i, j, key1, key2, el1, el2);
				maxBorders = Math.max(fieldFormat.getBorderFormats().size(),
						maxBorders);
				fieldFormats[i][j] = fieldFormat;
			}
		}
	}

	/**
	 * Gibt das FieldFormat zurueck, das den uebergebenen Parametern entspricht.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @return boolean
	 * 
	 * @see FieldFormat#isEmpty()
	 */
	private FieldFormat getFieldFormat(final int idX, final int idY) {

		FieldFormat retVal = null;
		if (colFirst) {
			final int c = idX % fieldFormats.length;
			final int r = idY % fieldFormats[c].length;
			retVal = fieldFormats[c][r];
		}
		if (rowFirst) {
			final int r = idY % fieldFormats.length;
			final int c = idX % fieldFormats[r].length;
			retVal = fieldFormats[r][c];
		}
		return retVal;
	}

	/**
	 * Prueft, ob es sich bei dem angegebenen Feld um einen Platzhalter handelt,
	 * der nicht dargestellt werden soll.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @return boolean
	 * 
	 * @see FieldFormat#isEmpty()
	 */
	public boolean isEmpty(final int idX, final int idY) {

		return getFieldFormat(idX, idY).isEmpty();
	}

	/**
	 * Gibt die maximale Anzahl von Nachbarschaftsbeziehungen zurueck. Bei
	 * Feldern mit abweichender Anzahl immer der maximal moegliche Wert.
	 * 
	 * @return int
	 */
	public int getMaxRelations() {

		return maxBorders;
	}

	/**
	 * Gibt ein Array von Point-Objekten zurueck, das die IDs der Nachbarfelder
	 * enthaelt.
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
	public List<FieldRelation> getRelations(final int idX, final int idY,
			final int maxX, final int maxY, final boolean borderless) {

		return getFieldFormat(idX, idY).getRelations(idX, idY, maxX, maxY,
				borderless);
	}

	/**
	 * Gibt die grafische Entsprechung des Feldes als GeneralPath zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param translate
	 *            boolean true, wenn das Feld positioniert werden soll.
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @return GeneralPath
	 * 
	 * @see FieldFormat#getPolygon(int, int, double, double, boolean, int, int,
	 *      int, int)
	 */
	public GeneralPath getPolygon(final int width, final int height,
			final boolean translate, final int idX, final int idY,
			final int maxX, final int maxY) {

		final double[] sumFactors = getSumFactors(idX, idY);
		return getFieldFormat(idX, idY).getPolygon(width, height,
				sumFactors[0], sumFactors[1], translate, idX, idY, maxX, maxY);
	}

	/**
	 * Gibt die Position des Feldes als double[] zurueck.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @return double[]
	 */
	private double[] getSumFactors(final int idX, final int idY) {

		double sumColFactor = 0;
		double sumRowFactor = 0;
		for (int i = 0; i < idX; i++) {
			sumColFactor += getFieldFormat(i, idY).getColFactor();
		}
		for (int j = 0; j < idY; j++) {
			sumRowFactor += getFieldFormat(idX, j).getRowFactor();
		}
		return new double[] { sumColFactor, sumRowFactor };
	}

	/**
	 * Gibt die Position des Feldes als double[] zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param translate
	 *            boolean
	 * @return double[]
	 * 
	 * @see FieldFormat#getPosition(int, int, double, double, boolean)
	 */
	public double[] getPosition(final int width, final int height,
			final int idX, final int idY, final boolean translate) {

		final double[] sumFactors = getSumFactors(idX, idY);
		return getFieldFormat(idX, idY).getPosition(width, height,
				sumFactors[0], sumFactors[1], translate);
	}

	/**
	 * Gibt den Platzbedarf aller Felder als Dimension zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @return Dimension
	 * 
	 * @see FieldFormat#getPolygon(int, int, double, double, boolean, int, int,
	 *      int, int)
	 */
	public Dimension getBoardsize(final int width, final int height,
			final int maxX, final int maxY) {

		double maxWidth = 0;
		double maxHeight = 0;
		GeneralPath poly;
		for (int j = 0; j < maxY; j++) {
			for (int i = 0; i < maxX; i++) {
				poly = getPolygon(width, height, true, i, j, maxX, maxY);
				maxWidth = Math.max(maxWidth, poly.getBounds().getMaxX());
				maxHeight = Math.max(maxHeight, poly.getBounds().getMaxY());
			}
		}

		return new Dimension((int) Math.rint(maxWidth) + 1,
				(int) Math.rint(maxHeight) + 1);
	}

	/**
	 * @param containerWidth
	 *            int
	 * @param containerHeight
	 *            int
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @param maxX
	 *            int
	 * @param maxY
	 *            int
	 * @return Dimension
	 */
	public Dimension getFieldsize(final int containerWidth,
			final int containerHeight, final int idX, final int idY,
			final int maxX, final int maxY) {

		double maxOffsetX = 0;
		double maxOffsetY = 0;
		for (final FieldFormat[] formats : fieldFormats) {
			for (final FieldFormat format : formats) {
				maxOffsetX = Math.max(format.getOffsetX(), maxOffsetX);
				maxOffsetY = Math.max(format.getOffsetY(), maxOffsetY);
			}
		}

		return getFieldFormat(idX, idY).getSize(containerWidth,
				containerHeight, maxX, maxY, maxOffsetX, maxOffsetY);
	}

	/**
	 * Gibt ein Array von GeneralPath-Objekten zurueck, die jeweils den Linien
	 * entsprechen, die an ein Nachbarfeld angrenzen. Eine Grenze kann dabei aus
	 * mehr als einer Linie bestehen.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param translate
	 *            boolean true, wenn das Feld positioniert werden soll.
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
	 * @return Map<String, GeneralPath>
	 * 
	 * @see FieldFormat#getWalls(int, int, double, double, boolean, int, int,
	 *      int, int, boolean)
	 */
	public Map<String, GeneralPath> getWalls(final int width, final int height,
			final boolean translate, final int idX, final int idY,
			final int maxX, final int maxY, final boolean borderless) {

		final double[] sumFactors = getSumFactors(idX, idY);
		return getFieldFormat(idX, idY).getWalls(width, height, sumFactors[0],
				sumFactors[1], translate, idX, idY, maxX, maxY, borderless);
	}

	/**
	 * Gibt die Segmente mit der exakten Begrenzung zu den korrekten
	 * Nachbarfeldern zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param translate
	 *            boolean true, wenn das Feld positioniert werden soll.
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
	 * @return Map<String, GeneralPath>
	 * 
	 * @see FieldFormat#getSegments(int, int, double, double, boolean, int, int,
	 *      int, int, boolean)
	 */
	public Map<String, GeneralPath> getSegments(final int width,
			final int height, final boolean translate, final int idX,
			final int idY, final int maxX, final int maxY,
			final boolean borderless) {

		final double[] sumFactors = getSumFactors(idX, idY);
		return getFieldFormat(idX, idY).getSegments(width, height,
				sumFactors[0], sumFactors[1], translate, idX, idY, maxX, maxY,
				borderless);
	}

	// ********* Folgende Methoden sind nur zur Visualisierung gedacht *********

	/**
	 * Gibt die Pfeile in der exakten Laenge zu den korrekten Nachbarfeldern
	 * zurueck.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param translate
	 *            boolean true, wenn das Feld positioniert werden soll.
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
	 * @return Map<String, GeneralPath>
	 */
	public Map<String, GeneralPath> getArrows(final int width,
			final int height, final boolean translate, final int idX,
			final int idY, final int maxX, final int maxY,
			final boolean borderless) {

		final Map<String, GeneralPath> arrows = new HashMap<>();
		double[] sumFactors = getSumFactors(idX, idY);
		final FieldFormat fieldFormat = getFieldFormat(idX, idY);
		double[] pos = fieldFormat.getPosition(width, height, sumFactors[0],
				sumFactors[1], translate);
		final double centerX = pos[0] + width * fieldFormat.getMinMaxPosX()
				* 0.5D;
		final double centerY = pos[1] + height * fieldFormat.getMinMaxPosY()
				* 0.5D;
		FieldFormat fieldFormatRef;
		BorderFormat borderFormatRef;
		double[] wallCenter;
		// Alle Nachbarschaftsbeziehungen dieses Feldes durchlaufen
		for (FieldRelation relation : fieldFormat.getRelations(idX, idY, maxX,
				maxY, borderless)) {

			fieldFormatRef = getFieldFormat(relation.getRefFieldX(),
					relation.getRefFieldY());
			borderFormatRef = fieldFormatRef.getBorderFormats().get(
					Integer.toString(relation.getRefBorderId()));

			sumFactors = getSumFactors(relation.getRefFieldX(),
					relation.getRefFieldY());
			pos = fieldFormatRef.getPosition(width, height, sumFactors[0],
					sumFactors[1], translate);
			// Pruefen, welche der Feldgrenzen dieses Feldes mit
			// welcher Feldgrenze des Nachbarfeldes uebereinstimmt.
			wallCenter = borderFormatRef.getWallCenter(width, height, pos[0],
					pos[1]);

			final GeneralPath arrow = getArrow(centerX, centerY, wallCenter[0],
					wallCenter[1]);

			arrows.put(relation.getKey(), arrow);

		}
		for (final BorderFormat borderFormat : fieldFormat.getBorderFormats()
				.values()) {
			final int refFieldX = idX + borderFormat.getRefFieldX();
			final int refFieldY = idY + borderFormat.getRefFieldY();
			if (refFieldX >= 0 && refFieldY >= 0 && refFieldX < maxX
					&& refFieldY < maxY) {

				fieldFormatRef = getFieldFormat(refFieldX, refFieldY);
				borderFormatRef = fieldFormatRef.getBorderFormats().get(
						Integer.toString(borderFormat.getRefId()));

				sumFactors = getSumFactors(refFieldX, refFieldY);
				pos = fieldFormatRef.getPosition(width, height, sumFactors[0],
						sumFactors[1], translate);
				// Pruefen, welche der Feldgrenzen dieses Feldes mit
				// welcher Feldgrenze des Nachbarfeldes uebereinstimmt.
				wallCenter = borderFormatRef.getWallCenter(width, height,
						pos[0], pos[1]);

				final GeneralPath arrow = getArrow(centerX, centerY,
						wallCenter[0], wallCenter[1]);

				arrows.put(borderFormat.getKey(), arrow);
			} else if (borderless) {
				// TODO Show arrows for borderless
			}
		}

		return arrows;
	}

	/**
	 * Gibt einen Pfeil aus, der ueber einen Startpunkt und einen Endpunkt
	 * definiert wird.
	 * 
	 * @param startX
	 *            double
	 * @param startY
	 *            double
	 * @param endX
	 *            double
	 * @param endY
	 *            double
	 * @return GeneralPath
	 */
	protected GeneralPath getArrow(final double startX, final double startY,
			final double endX, final double endY) {

		final GeneralPath arrow = new GeneralPath();
		final double angle = getDegrees(startX, startY, endX, endY);
		final double sideAngle = 30.0D;
		final double sideLength = Point2D.distance(startX, startY, endX, endY) * 0.3D;

		arrow.moveTo(startX, startY);
		arrow.lineTo(endX, endY);

		arrow.moveTo(endX, endY);
		arrow.lineTo(
				endX - sideLength
						* Math.cos((angle + sideAngle) * Math.PI / 180.0D),
				endY - sideLength
						* Math.sin((angle + sideAngle) * Math.PI / 180.0D));

		arrow.moveTo(endX, endY);
		arrow.lineTo(
				endX - sideLength
						* Math.cos((angle - sideAngle) * Math.PI / 180.0D),
				endY - sideLength
						* Math.sin((angle - sideAngle) * Math.PI / 180.0D));

		return arrow;
	}

	/**
	 * Gibt den Winkel in Grad zurueck, wobei eine Linie, die gerade von links
	 * nach rechts (3 auf dem Ziffernblatt einer analogen Uhr) verlaeuft 0/360
	 * Grad entspricht.
	 * 
	 * @param posX1
	 *            double
	 * @param posY1
	 *            double
	 * @param posX2
	 *            double
	 * @param posY2
	 *            double
	 * @return double
	 */
	private double getDegrees(final double posX1, final double posY1,
			final double posX2, final double posY2) {

		final double distX = posX2 - posX1;
		final double distY = posY2 - posY1;
		double angle = 0.0D;

		if (distX == 0.0D) {
			if (distY > 0.0D) {
				angle = Math.PI / 2.0D;
			} else {
				angle = Math.PI * 1.5D;
			}
		} else if (distY == 0.0D) {
			if (distX <= 0.0D) {
				angle = Math.PI;
			}
		} else {
			angle = Math.atan(distY / distX);
			if (distX < 0.0D) {
				angle += Math.PI;
			} else if (distY < 0.0D) {
				angle += Math.PI * 2.0D;
			}
		}

		return angle / Math.PI * 180.0D;
	}
}