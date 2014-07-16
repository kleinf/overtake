package util;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * @author Administrator
 * 
 */
public final class FieldConverterXmlToJson {

	/**
	 * Umwandlung von XML-Objekt in JSON-String.
	 * 
	 * @param doc
	 *            Document
	 * @return String
	 */
	public static String getJson(Document doc) {
		return getJson(doc, false);
	}

	/**
	 * Umwandlung von XML-Objekt in JSON-String.
	 * 
	 * preserveOrder = true gibt an, dass die Reihenfolge der Objekte im JSON,
	 * mit der Reihenfolge der Elemente im XML identisch sein soll.
	 * 
	 * @param doc
	 *            Document
	 * @param preserveOrder
	 *            boolean
	 * @return String
	 */
	public static String getJson(Document doc, boolean preserveOrder) {
		if (preserveOrder) {
			return FieldConverterXmlToJsonPretty.getJson(doc);

		}
		return FieldConverterXmlToJsonSimple.getJson(doc);
	}

	/**
	 * Umwandlung von XML-String in JSON-String.
	 * 
	 * @param xml
	 *            String
	 * @return String
	 */
	public static String getJson(String xml) {
		return getJson(getXmlObject(xml), false);
	}

	/**
	 * Umwandlung von XML-String in JSON-String.
	 * 
	 * preserveOrder = true gibt an, dass die Reihenfolge der Objekte im JSON,
	 * mit der Reihenfolge der Elemente im XML identisch sein soll.
	 * 
	 * @param xml
	 *            String
	 * @param preserveOrder
	 *            boolean
	 * @return String
	 */
	public static String getJson(String xml, boolean preserveOrder) {
		return getJson(getXmlObject(xml), preserveOrder);
	}

	/**
	 * Umwandeln von XML-String in ein XML-Objekt.
	 * 
	 * @param xml
	 *            String
	 * @return Document
	 */
	private static Document getXmlObject(String xml) {

		StringReader stringReader = null;
		try {
			stringReader = new StringReader(xml);
			return new SAXBuilder().build(stringReader);
		} catch (final IOException exception) {
			exception.printStackTrace();
		} catch (final JDOMException exception) {
			exception.printStackTrace();
		} finally {
			if (stringReader != null) {
				stringReader.close();
			}
		}
		return null;
	}
}

class FieldConverterXmlToJsonSimple {

	/**
	 * Umwandlung von XML-Objekt in JSON-String.
	 * 
	 * @param doc
	 *            Document
	 * @return String
	 */
	static String getJson(Document doc) {
		JSONObject root = new JSONObject();
		JSONObject field = new JSONObject();
		doColsOrRows2(field, doc.getRootElement().getChildren(), true);
		root.put("field", field);
		return root.toString();
	}

	private static void doColsOrRows2(JSONObject parent,
			List<Element> elements, boolean first) {
		if (elements.size() == 0) {
			return;
		}
		JSONObject obj;
		for (Element element : elements) {
			obj = new JSONObject();
			addKeyValue(obj, element, "factor");
			addKeyValue(obj, element, "offsetX");
			addKeyValue(obj, element, "offsetY");
			if (first) {
				doColsOrRows2(obj, element.getChildren(), false);
			} else {
				doBorders(obj, element.getChildren());
			}
			parent.append(elements.get(0).getName(), obj);
		}
	}

	private static void doBorders(JSONObject parent, List<Element> elements) {
		if (elements.size() == 0) {
			return;
		}
		JSONObject obj;
		for (Element element : elements) {
			obj = new JSONObject();
			addKeyValue(obj, element, "id");
			addKeyValue(obj, element, "refBorderId");
			addKeyValue(obj, element, "refFieldX");
			addKeyValue(obj, element, "refFieldY");
			doWalls(obj, element.getChildren());
			parent.append("border", obj);
		}
	}

	private static void doWalls(JSONObject parent, List<Element> elements) {
		if (elements.size() == 0) {
			return;
		}
		JSONObject obj;
		for (Element element : elements) {
			obj = new JSONObject();
			obj.put("type", element.getName());
			addKeyValue(obj, element, "x1");
			addKeyValue(obj, element, "y1");
			addKeyValue(obj, element, "x2");
			addKeyValue(obj, element, "y2");
			addKeyValue(obj, element, "x3");
			addKeyValue(obj, element, "y3");
			parent.append("wall", obj);
		}
	}

	private static void addKeyValue(JSONObject parent, Element element,
			String key) {
		if (element != null && element.getAttributeValue(key) != null) {
			parent.put(key,
					JSONObject.stringToValue(element.getAttributeValue(key)));
		}
	}
}

class FieldConverterXmlToJsonPretty {

	/**
	 * Umwandlung von XML-Objekt in JSON-String.
	 * 
	 * @param doc
	 *            Document
	 * @return String
	 */
	static String getJson(Document doc) {
		JSONStringer field = new JSONStringer();
		field.object();
		field.key("field");
		field.object();
		doColsOrRows(field, doc.getRootElement().getChildren(), true);
		field.endObject();
		field.endObject();
		return field.toString();
	}

	private static void doColsOrRows(JSONStringer parent,
			List<Element> elements, boolean first) {
		if (elements.size() == 0) {
			return;
		}
		parent.key(elements.get(0).getName());
		parent.array();
		for (Element element : elements) {
			parent.object();
			addKeyValue(parent, element, "factor");
			addKeyValue(parent, element, "offsetX");
			addKeyValue(parent, element, "offsetY");
			if (first) {
				doColsOrRows(parent, element.getChildren(), false);
			} else {
				doBorders(parent, element.getChildren());
			}
			parent.endObject();
		}
		parent.endArray();
	}

	private static void doBorders(JSONStringer parent, List<Element> elements) {
		if (elements.size() == 0) {
			return;
		}
		parent.key("border");
		parent.array();
		for (Element element : elements) {
			parent.object();
			addKeyValue(parent, element, "id");
			addKeyValue(parent, element, "refBorderId");
			addKeyValue(parent, element, "refFieldX");
			addKeyValue(parent, element, "refFieldY");
			doWalls(parent, element.getChildren());
			parent.endObject();
		}
		parent.endArray();
	}

	private static void doWalls(JSONStringer parent, List<Element> elements) {
		if (elements.size() == 0) {
			return;
		}
		parent.key("wall");
		parent.array();
		for (Element element : elements) {
			parent.object();
			parent.key("type");
			parent.value(element.getName());
			addKeyValue(parent, element, "x1");
			addKeyValue(parent, element, "y1");
			addKeyValue(parent, element, "x2");
			addKeyValue(parent, element, "y2");
			addKeyValue(parent, element, "x3");
			addKeyValue(parent, element, "y3");
			parent.endObject();
		}
		parent.endArray();
	}

	private static void addKeyValue(JSONStringer parent, Element element,
			String key) {
		if (element != null && element.getAttributeValue(key) != null) {
			parent.key(key);
			parent.value(JSONObject.stringToValue(element
					.getAttributeValue(key)));
		}
	}
}
