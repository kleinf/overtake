package util;

import java.io.IOException;
import java.io.StringWriter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Administrator
 * 
 */
public final class FieldConverterJsonToXml {

	/**
	 * Umwandeln von JSON-String in ein JSON-Objekt.
	 * 
	 * @param json
	 *            String
	 * @return JSONObject
	 */
	private static JSONObject getJsonObject(String json) {
		return new JSONObject(json);
	}

	/**
	 * Umwandlung von JSON-String in XML-String.
	 * 
	 * @param json
	 *            String
	 * @return String
	 */
	public static String getXml(String json) {
		return getXml(getJsonObject(json));
	}

	/**
	 * Umwandlung von JSON-Objekt in XML-String.
	 * 
	 * @param obj
	 *            JSONObject
	 * @return String
	 */
	public static String getXml(JSONObject obj) {
		Element field = new Element("field");
		Namespace xsiNS = Namespace.getNamespace("xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		field.addNamespaceDeclaration(xsiNS);
		field.setAttribute("noNamespaceSchemaLocation", "field.xsd", xsiNS);
		Document doc = new Document();
		doc.setRootElement(field);

		String key = obj.getJSONObject("field").keys().next();
		doColsOrRows(field, key, obj.getJSONObject("field").getJSONArray(key),
				true);

		StringWriter sw = new StringWriter();
		try {
			new XMLOutputter().output(doc, sw);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				sw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sw.toString();
	}

	private static void doColsOrRows(Element parent, String key,
			JSONArray array, boolean first) {
		if (array.length() == 0) {
			return;
		}
		JSONObject obj;
		Element el;
		for (int i = 0; i < array.length(); i++) {
			obj = array.getJSONObject(i);
			el = new Element(key);
			addAttribute(el, "factor", obj);
			addAttribute(el, "offsetX", obj);
			addAttribute(el, "offsetY", obj);
			String key2 = obj.keys().next();
			if (first) {
				doColsOrRows(el, key2, obj.getJSONArray(key2), false);
			} else if (obj.has("border")) {
				doBorders(el, obj.getJSONArray("border"));
			}
			parent.addContent(el);
		}
	}

	private static void doBorders(Element parent, JSONArray array) {
		if (array.length() == 0) {
			return;
		}
		JSONObject obj;
		Element border;
		for (int i = 0; i < array.length(); i++) {
			obj = array.getJSONObject(i);
			border = new Element("border");
			addAttribute(border, "id", obj);
			addAttribute(border, "refBorderId", obj);
			addAttribute(border, "refFieldX", obj);
			addAttribute(border, "refFieldY", obj);
			if (obj.has("wall")) {
				doWalls(border, obj.getJSONArray("wall"));
			}
			parent.addContent(border);
		}
	}

	private static void doWalls(Element parent, JSONArray array) {
		if (array.length() == 0) {
			return;
		}
		JSONObject obj;
		Element wall;
		for (int i = 0; i < array.length(); i++) {
			obj = array.getJSONObject(i);
			wall = new Element(obj.get("type").toString());
			addAttribute(wall, "x1", obj);
			addAttribute(wall, "y1", obj);
			addAttribute(wall, "x2", obj);
			addAttribute(wall, "y2", obj);
			addAttribute(wall, "x3", obj);
			addAttribute(wall, "y3", obj);
			parent.addContent(wall);
		}
	}

	private static void addAttribute(Element element, String key, JSONObject obj) {
		if (obj.has(key)) {
			element.setAttribute(key, obj.get(key).toString());
		}
	}
}