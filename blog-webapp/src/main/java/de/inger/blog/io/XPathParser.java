package de.inger.blog.io;

import java.io.File;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XPathParser extends SilentBob {

	private static Log log = LogFactory.getLog(XPathParser.class);

	public static Object parse(File file, String expression, QName returnType) {
		Document document = XMLParser.parseDocument(file);
		setLastException(XMLParser.getLastException());
		if (getLastException() != null) {
			return null;
		}
		return parse(document, expression, returnType);
	}

	public static Object parse(Document document, String expression, QName returnType) {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		Object object = null;
		try {
			object = xPath.evaluate(expression, document, returnType);
		} catch (XPathExpressionException e) {
			log.error(e);
			setLastException(e);
		}
		return object;
	}

	public static NodeList parse(Document document, String expression) {
		Object object = parse(document, expression, XPathConstants.NODESET);
		return (NodeList) object;
	}

	public static NodeList parse(File file, String expression) {
		Object object = parse(file, expression, XPathConstants.NODESET);
		return (NodeList) object;
	}

}
