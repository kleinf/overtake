package util.field;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import util.PseudoLogger;

/**
 * @author Administrator
 * 
 */
public abstract class FieldFormatter {

	private int maxBorders = 0;
	private FieldFormat[][] fieldFormats = null;
	private boolean colFirst = false;
	private boolean rowFirst = false;

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
			PseudoLogger.getInstance().log(exception.getMessage());
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (final IOException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
				}
			}
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (final IOException exception) {
					PseudoLogger.getInstance().log(exception.getMessage());
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
			PseudoLogger.getInstance().log(exception.getMessage());
		} catch (final JDOMException exception) {
			PseudoLogger.getInstance().log(exception.getMessage());
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
	protected FieldFormat getFieldFormat(final int idX, final int idY) {

		FieldFormat retVal = null;
		if (this.colFirst) {
			final int c = idX % this.fieldFormats.length;
			final int r = idY % this.fieldFormats[c].length;
			retVal = this.fieldFormats[c][r];
		}
		if (this.rowFirst) {
			final int r = idY % this.fieldFormats.length;
			final int c = idX % this.fieldFormats[r].length;
			retVal = this.fieldFormats[r][c];
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

		return this.maxBorders;
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
	 * Gibt die Position des Feldes als double[] zurueck.
	 * 
	 * @param idX
	 *            int
	 * @param idY
	 *            int
	 * @return double[]
	 */
	protected double[] getSumFactors(final int idX, final int idY) {

		double sumColFactor = 0.0D;
		double sumRowFactor = 0.0D;
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
	 * @return int[]
	 */
	public int[] getFieldsize(final int containerWidth,
			final int containerHeight, final int idX, final int idY,
			final int maxX, final int maxY) {

		double maxOffsetX = 0.0D;
		double maxOffsetY = 0.0D;
		for (final FieldFormat[] formats : this.fieldFormats) {
			for (final FieldFormat format : formats) {
				maxOffsetX = Math.max(format.getOffsetX(), maxOffsetX);
				maxOffsetY = Math.max(format.getOffsetY(), maxOffsetY);
			}
		}

		return getFieldFormat(idX, idY).getSize(containerWidth,
				containerHeight, maxX, maxY, maxOffsetX, maxOffsetY);
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
	 * @return Map<String, Shape>
	 */
	public Map<String, Shape> getArrowShapes(final int width, final int height,
			final boolean translate, final int idX, final int idY,
			final int maxX, final int maxY, final boolean borderless) {

		final Map<String, Shape> arrows = new HashMap<>();
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
			if (borderFormatRef == null) {
				continue;
			}
			sumFactors = getSumFactors(relation.getRefFieldX(),
					relation.getRefFieldY());
			pos = fieldFormatRef.getPosition(width, height, sumFactors[0],
					sumFactors[1], translate);
			// Pruefen, welche der Feldgrenzen dieses Feldes mit
			// welcher Feldgrenze des Nachbarfeldes uebereinstimmt.
			wallCenter = borderFormatRef.getWallCenter(width, height, pos[0],
					pos[1]);

			final Shape arrow = getArrow(centerX, centerY, wallCenter[0],
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

				final Shape arrow = getArrow(centerX, centerY, wallCenter[0],
						wallCenter[1]);

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
	 * @return Shape
	 */
	protected Shape getArrow(final double startX, final double startY,
			final double endX, final double endY) {

		final Shape arrow = new Shape();
		final double angle = getDegrees(startX, startY, endX, endY);
		final double sideAngle = 30.0D;

		double x = startX;
		double y = startY;
		x -= endX;
		y -= endY;
		double distance = Math.sqrt(x * x + y * y);
		final double sideLength = distance * 0.3D;

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