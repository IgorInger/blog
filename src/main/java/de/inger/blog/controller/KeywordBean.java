package de.inger.blog.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.inger.blog.model.KeywordEntity;
import de.inger.blog.persistence.PersistenceManager;

/**
 * Keywords controller.
 * 
 * @author Igor Inger
 * 
 */
@ManagedBean
@RequestScoped
public class KeywordBean {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(KeywordBean.class);

	/**
	 * Keyword name.
	 */
	private String keywordName;

	/**
	 * Keyword.
	 */
	private KeywordEntity keyword;

	/**
	 * Gers keywords.
	 * 
	 * @return keywords.
	 */
	@SuppressWarnings("unchecked")
	public List<KeywordEntity> getKeywords() {
		EntityManager manager = PersistenceManager.getEntityManager();
		return manager.createNamedQuery("findAll").getResultList();
	}

	/**
	 * Gets keyword name.
	 * 
	 * @return keyword name.
	 */
	public String getKeywordName() {
		return keywordName;
	}

	/**
	 * Sets keyword name.
	 * 
	 * @param keywordName
	 *            keyword name.
	 */
	public void setKeywordName(String keywordName) {
		this.keywordName = keywordName;
	}

	/**
	 * Gets keyword.
	 * 
	 * @return keyword.
	 */
	public KeywordEntity getKeyword() {
		return keyword;
	}

	/**
	 * Sets keyword.
	 * 
	 * @param keyword
	 *            keyword.
	 */
	public void setKeyword(KeywordEntity keyword) {
		this.keyword = keyword;
	}

	/**
	 * Load keyword.
	 */
	public void loadKeyword() {
		String decodedKeywordName = null;
		try {
			decodedKeywordName = URLDecoder.decode(keywordName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warn(e);
		}
		EntityManager manager = PersistenceManager.getEntityManager();
		keyword = manager.find(KeywordEntity.class, decodedKeywordName);
	}
}
