import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import util.field.FieldConverterJsonToXml;
import util.field.FieldConverterXmlToJson;

/**
 * @author Administrator
 * 
 */
public final class FieldConverterTest {

	private String fieldName = "A0.xml";
	private String data = "";

	/**
	 * 
	 */
	@Before
	public void init() {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader("src/test/resources/" + fieldName);
			bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			final StringBuilder stringBuilder = new StringBuilder();
			while (line != null) {
				stringBuilder.append(line);
				line = bufferedReader.readLine();
			}
			data = stringBuilder.toString().trim();
		} catch (final IOException exception) {
			exception.printStackTrace();
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (final IOException exception) {
					exception.printStackTrace();
				}
			}
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (final IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testFieldConverter() {
		if (data.trim().startsWith("<?xml")) {
			FieldConverterXmlToJson.getJson(data);
		} else {
			FieldConverterJsonToXml.getXml(data);
		}
	}

	/**
	 * 
	 */
	@Test
	public void testFieldConverterOrderedJsonEqual() {
		String jsonOrdered = FieldConverterXmlToJson.getJson(data, true);
		String xmlOrdered = FieldConverterJsonToXml.getXml(jsonOrdered);

		// Vergleich ohne Whitespace-Unterschiede
		Assert.assertEquals(data.replaceAll("\\s", ""),
				xmlOrdered.replaceAll("\\s", ""));
	}

	/**
	 * 
	 */
	@Test
	public void testFieldConverterUnorderedJsonEqual() {
		String jsonUnordered = FieldConverterXmlToJson.getJson(data, false);
		String xmlUnordered = FieldConverterJsonToXml.getXml(jsonUnordered);

		// Vergleich ohne Whitespace-Unterschiede
		Assert.assertEquals(data.replaceAll("\\s", ""),
				xmlUnordered.replaceAll("\\s", ""));
	}
}