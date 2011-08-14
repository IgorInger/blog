package de.inger.blog.dom;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class NodeParser {

	public static String getAttributeAsString(Node node, String name) {
		NamedNodeMap namedNodeMap = node.getAttributes();
		Node attribute = namedNodeMap.getNamedItem(name);
		if (attribute == null) {
			return null;
		}
		String textContent = attribute.getTextContent();
		return textContent;
	}
	
	public static Date getAttributeAsDate(Node node, String name, String pattern) throws ParseException {
		String unparsedDate = getAttributeAsString(node, name);
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.parse(unparsedDate);
	}
	
	public static Node getSubnode(Node node, String name) {
		NodeList childNodes = node.getChildNodes();
		int length = childNodes.getLength();
		for (int i = 0; i < length; i++) {
			Node subnode = childNodes.item(i);
			if (subnode.getNodeName().equals(name)) {
				return subnode;
			}
		}
		return null;
	}

	public static List<Node> getSubnodes(Node node, String name) {
		NodeList childNodes = node.getChildNodes();
		int length = childNodes.getLength();
		List<Node> result = new ArrayList<Node>();
		for (int i = 0; i < length; i++) {
			Node subnode = childNodes.item(i);
			if (subnode.getNodeName().equals(name)) {
				result.add(subnode);
			}
		}
		return result;
	}

}
