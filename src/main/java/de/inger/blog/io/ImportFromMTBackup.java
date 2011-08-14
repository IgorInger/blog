package de.inger.blog.io;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.inger.blog.dom.NodeParser;
import de.inger.blog.model.CommentEntity;
import de.inger.blog.model.EntryEntity;
import de.inger.blog.model.KeywordEntity;
import de.inger.blog.model.UserEntity;
import de.inger.blog.persistence.PersistenceManager;

public class ImportFromMTBackup {

	private static Log log = LogFactory.getLog(ImportFromMTBackup.class);

	private Map<String, Object> entryIds = new HashMap<String, Object>();
	private Map<String, Object> authorIds = new HashMap<String, Object>();
	private Map<String, Object> categoryIds = new HashMap<String, Object>();
	private Map<String, Object> authorNames = new HashMap<String, Object>();

	private ImportFromMTBackup() {
		File file = new File("mt-backup/Movable_Type-2011-01-03-23-12-22-Backup-1.xml");
		NodeList entryNodeList = (NodeList) XPathParser.parse(file, "/movabletype/entry[@blog_id='1']");
		parseEntryNodeList(entryNodeList);
		NodeList authorNodeList = (NodeList) XPathParser.parse(file, "/movabletype/author");
		parseAuthorNodeList(authorNodeList);
		NodeList commentNodeList = (NodeList) XPathParser.parse(file, "/movabletype/comment");
		parseCommentNodeList(commentNodeList);
		NodeList categoryNodeList = (NodeList) XPathParser.parse(file, "/movabletype/category");
		parseCategoryNodeList(categoryNodeList);
		NodeList placementNodeList = (NodeList) XPathParser.parse(file, "/movabletype/placement");
		parsePlacementNodeList(placementNodeList);

	}

	private void parsePlacementNodeList(NodeList placementNodeList) {
		for (int i = 0; i < placementNodeList.getLength(); i++) {
			Node placementNode = placementNodeList.item(i);
			parsePlacementNode(placementNode);
		}
	}

	private void parsePlacementNode(Node placementNode) {
		String categoryId = NodeParser.getAttributeAsString(placementNode, "category_id");
		String entryId = NodeParser.getAttributeAsString(placementNode, "entry_id");

		String category = (String) categoryIds.get(categoryId);
		Long entry = (Long) entryIds.get(entryId);

		if (category == null || entry == null) {
			return;
		}

		EntityManager manager = PersistenceManager.getEntityManager();

		KeywordEntity keywordEntity = manager.find(KeywordEntity.class, category);
		EntryEntity entryEntity = manager.find(EntryEntity.class, entry);

		entryEntity.addKeyword(keywordEntity);

		manager.getTransaction().begin();
		manager.persist(entryEntity);
		manager.persist(keywordEntity);
		manager.getTransaction().commit();
	}

	private void parseCategoryNodeList(NodeList categoryNodeList) {
		for (int i = 0; i < categoryNodeList.getLength(); i++) {
			Node categoryNode = categoryNodeList.item(i);
			parseCategoryNode(categoryNode);
		}
	}

	private void parseCategoryNode(Node categoryNode) {
		KeywordEntity keywordEntity = new KeywordEntity();

		String categoryLabel = NodeParser.getAttributeAsString(categoryNode, "label");
		String categoryId = NodeParser.getAttributeAsString(categoryNode, "id");

		keywordEntity.setName(categoryLabel);

		EntityManager manager = PersistenceManager.getEntityManager();
		manager.getTransaction().begin();
		try {
			manager.persist(keywordEntity);
			manager.getTransaction().commit();
			categoryIds.put(categoryId, categoryLabel);
		} catch (Exception e) {
			log.error(e.getMessage());
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
		}
	}

	private void parseCommentNodeList(NodeList commentNodeList) {
		for (int i = 0; i < commentNodeList.getLength(); i++) {
			Node commentNode = commentNodeList.item(i);
			parseCommentNode(commentNode);
		}
	}

	private void parseCommentNode(Node commentNode) {
		CommentEntity commentEntity = new CommentEntity();

		String entryId = NodeParser.getAttributeAsString(commentNode, "entry_id");
		Node commentTextNode = NodeParser.getSubnode(commentNode, "text");
		String commentText = commentTextNode.getTextContent();
		String commentAuthor = NodeParser.getAttributeAsString(commentNode, "author");
		Date commentCreated = new Date();
		try {
			commentCreated = NodeParser.getAttributeAsDate(commentNode, "created_on", "yyyyMMddhhmmss");
		} catch (ParseException e) {
			log.error(e);
		}

		long entryEntityId = 0;
		try {
			entryEntityId = (Long) entryIds.get(entryId);
		} catch (NullPointerException e) {
			log.error(entryId);
			return;
		}

		long userEntityId = 0;
		try {
			userEntityId = (Long) authorNames.get(commentAuthor);
		} catch (NullPointerException e) {
			log.error(commentAuthor);
		}

		commentEntity.setContent(commentText);
		commentEntity.setCreated(commentCreated);

		EntityManager manager = PersistenceManager.getEntityManager();
		EntryEntity entryEntity = manager.find(EntryEntity.class, entryEntityId);

		UserEntity userEntity = manager.find(UserEntity.class, userEntityId);
		commentEntity.setAuthor(userEntity);

		manager.getTransaction().begin();
		manager.persist(commentEntity);
		entryEntity.addComment(commentEntity);
		manager.persist(entryEntity);
		manager.getTransaction().commit();
	}

	private void parseAuthorNodeList(NodeList authorNodeList) {
		for (int i = 0; i < authorNodeList.getLength(); i++) {
			Node authorNode = authorNodeList.item(i);
			parseAuthorNode(authorNode);
		}
	}

	private void parseAuthorNode(Node authorNode) {
		UserEntity userEntity = new UserEntity();

		String authorId = NodeParser.getAttributeAsString(authorNode, "id");
		String authorName = NodeParser.getAttributeAsString(authorNode, "name");
		Date authorCreated = new Date();
		try {
			authorCreated = NodeParser.getAttributeAsDate(authorNode, "created_on", "yyyyMMddhhmmss");
		} catch (ParseException e) {
			log.error(e);
		}
		String authorPassword = NodeParser.getAttributeAsString(authorNode, "password");
		String authorEmail = NodeParser.getAttributeAsString(authorNode, "email");

		userEntity.setName(authorName);
		userEntity.setCreated(authorCreated);
		userEntity.setPassword(authorPassword);
		userEntity.setEmail(authorEmail);

		EntityManager manager = PersistenceManager.getEntityManager();
		manager.getTransaction().begin();
		try {
			manager.persist(userEntity);
			manager.getTransaction().commit();
			authorIds.put(authorId, userEntity.getId());
			authorNames.put(authorName, userEntity.getId());

		} catch (Exception e) {
			log.error(e);
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
		}
	}

	private void parseEntryNodeList(NodeList entryNodeList) {
		for (int i = 0; i < entryNodeList.getLength(); i++) {
			Node entryNode = entryNodeList.item(i);
			parseEntryNode(entryNode);
		}
	}

	private void parseEntryNode(Node entryNode) {
		EntryEntity entry = new EntryEntity();
		String entryId = NodeParser.getAttributeAsString(entryNode, "id");
		String title = NodeParser.getAttributeAsString(entryNode, "title");
		entry.setTitle(title);

		entry.setContent(entryNode.getTextContent());

		DateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			String textContent = NodeParser.getAttributeAsString(entryNode, "authored_on");
			Date date = format.parse(textContent);
			entry.setCreated(date);
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		EntityManager manager = PersistenceManager.getEntityManager();
		manager.getTransaction().begin();
		try {
			entry.setDraft(false);
			manager.persist(entry);
			manager.getTransaction().commit();
			entryIds.put(entryId, entry.getId());
		} catch (Exception e) {
			log.error(e);
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
		}

	}

	public static void main(String[] args) {
		new ImportFromMTBackup();
	}
}
