package de.inger.blog.io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLParser extends SilentBob {

	private static Log log = LogFactory.getLog(XMLParser.class);

	public static Document parseDocument(File file) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			log.error(e);
			setLastException(e);
			return null;
		}
		Document document = null;
		try {
			document = documentBuilder.parse(file);
		} catch (SAXException e) {
			log.error(e);
			setLastException(e);
			return null;
		} catch (IOException e) {
			log.error(e);
			setLastException(e);
			return null;
		}
		return document;

	}

}
