package de.inger.blog.faces.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.inger.latex2png.LatexToPng;

/**
 * Latex converter.
 * 
 * @author Igor Inger
 * 
 */
@FacesConverter("latex")
public class LatexConverter implements Converter {

	/**
	 * Default resolution [dpi].
	 */
	private static final int DEFAULT_RESOLUTION = 300;

	/**
	 * Logger.
	 */
	private Log log = LogFactory.getLog(LatexConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		log.trace("LatexConverter.getAsObject()");
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String result = (String) value;
		try {
			result = parseString(result);
		} catch (XPathExpressionException e) {
			log.error(e);
		} catch (ParserConfigurationException e) {
			log.error(e);
		} catch (SAXException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} catch (TransformerException e) {
			log.error(e);
		}
		return result;
	}

	/**
	 * Parses string.
	 * 
	 * @param result
	 *            unparsed string.
	 * @return parsed string.
	 * @throws ParserConfigurationException
	 *             on parser configuration error.
	 * @throws SAXException
	 *             on sax-error.
	 * @throws IOException
	 *             on io-error.
	 * @throws XPathExpressionException
	 *             on xpath-expression.
	 * @throws TransformerException
	 *             on transformer-exception.
	 */
	private String parseString(String result) throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>");
		stringBuilder.append("<latex:root>");
		stringBuilder.append(result);
		stringBuilder.append("</latex:root>");
		String string = stringBuilder.toString();
		byte[] bytes = string.getBytes("UTF-8");
		ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

		Document document = builder.parse(stream);

		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression xPathExpression = xPath.compile("//latex");
		NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
		List<Object[]> replacements = new ArrayList<Object[]>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node latexNode = nodeList.item(i);
			NodeList latexChildren = latexNode.getChildNodes();
			for (int j = 0; j < latexChildren.getLength(); j++) {
				Node latexNodeContent = latexChildren.item(j);
				String value = "";
				switch (latexNodeContent.getNodeType()) {
				case Node.TEXT_NODE:
					value = latexNodeContent.getTextContent();
					break;
				case Node.CDATA_SECTION_NODE:
					value = latexNodeContent.getTextContent();
					break;
				default:
					break;
				}
				Element el = document.createElement("img");
				el.setAttribute("alt", value);

				LatexToPng latexToPng = new LatexToPng();
				latexToPng.setTightImageSize(true);
				latexToPng.setTransparentBackground(true);
				latexToPng.setResolution(DEFAULT_RESOLUTION);
				byte[] picture = latexToPng.convertString(value);
				String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
				String filePath = String.format("%s%s%s", path, System.getProperty("file.separator"), "img.png");
				FileOutputStream fileOutputStream = new FileOutputStream(filePath);
				fileOutputStream.write(picture);
				fileOutputStream.flush();
				fileOutputStream.close();
				log.debug(filePath);
				el.setAttribute("src", "/blog/img.png");

				log.debug("Parent: " + latexNodeContent.getParentNode().getParentNode());
				log.debug("LatexNode: " + latexNodeContent.getParentNode());
				log.debug("ImgEl: " + el);
				Object[] array = new Object[] { latexNodeContent.getParentNode().getParentNode(),
						latexNodeContent.getParentNode(), el };
				replacements.add(array);
			}
			log.debug(latexNode);

		}
		for (int i = 0; i < replacements.size(); i++) {
			Object[] array = replacements.get(i);
			Node parent = (Node) array[0];
			Node latex = (Node) array[1];
			Node img = (Node) array[2];
			parent.replaceChild(img, latex);
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Source source = new DOMSource(document);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		StreamResult streamResult = new StreamResult(byteArrayOutputStream);
		transformer.transform(source, streamResult);
		String resultString = byteArrayOutputStream.toString("UTF-8");
		resultString = resultString.replaceAll("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>", "");
		resultString = resultString.replaceAll("<latex:root>", "");
		resultString = resultString.replaceAll("</latex:root>", "");
		return resultString;
	}

}
