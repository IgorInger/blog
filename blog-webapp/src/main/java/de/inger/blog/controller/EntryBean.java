package de.inger.blog.controller;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.inger.blog.faces.FacesContextUtils;
import de.inger.blog.model.EntryEntity;

import de.inger.blog.model.KeywordEntity;
import de.inger.blog.persistence.PersistenceManager;
import de.inger.textutils.RetrieveFieldClosure;
import de.inger.textutils.TextUtils;

/**
 * Entry controller.
 * 
 * @author Igor Inger
 * 
 */
@ManagedBean
@RequestScoped
public class EntryBean {

	/**
	 * Logger.
	 */
	private Log log = LogFactory.getLog(EntryBean.class);

	/**
	 * Entry id.
	 */
	private long entryID;

	/**
	 * Page number.
	 */
	private int page = 1;

	/**
	 * Current entry.
	 */
	private EntryEntity entry = new EntryEntity();

	/**
	 * Entity manager.
	 */
	private EntityManager entityManager;

	/**
	 * Default page size.
	 */
	private static final int DEFAULT_PAGE_SIZE = 20;

	/**
	 * Parsed keywords.
	 */
	private List<String> parsedKeyword = new ArrayList<String>();

	/**
	 * Default constructor.
	 */
	public EntryBean() {
		entityManager = PersistenceManager.getEntityManager();
	}

	/**
	 * @return the entry
	 */
	public EntryEntity getEntry() {
		return entry;
	}

	/**
	 * @param entry
	 *            the entry to set
	 */
	public void setEntry(EntryEntity entry) {
		this.entry = entry;
	}

	/**
	 * Save entry.
	 * 
	 * @return outcome
	 */
	public String save() {
		entityManager.getTransaction().begin();
		try {
			removeDeletedKeywords();
			persistNewKeywords();
			removeUnlinkedKeywords();
			entityManager.persist(entry);
			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			log.error(e);
		}
		return "pretty:";
	}

	/**
	 * Remove unlinked keywords.
	 */
	private void removeUnlinkedKeywords() {
		@SuppressWarnings("unchecked")
		List<KeywordEntity> keywords = entityManager.createQuery("SELECT k FROM Keyword k WHERE k.entries.size = 0")
				.getResultList();
		for (KeywordEntity keyword : keywords) {
			entityManager.remove(keyword);
		}
	}

	/**
	 * Persists new keywords attached to entry.
	 */
	private void persistNewKeywords() {
		for (String string : parsedKeyword) {
			Query query = entityManager.createQuery("Select k FROM Keyword k WHERE k.name = :name");
			query.setParameter("name", string);
			KeywordEntity keyword = null;
			try {
				keyword = (KeywordEntity) query.getSingleResult();
			} catch (NoResultException e) {
				keyword = new KeywordEntity(string);
			}
			entry.addKeyword(keyword);
			entityManager.persist(keyword);
		}
	}

	/**
	 * Removes obsoleted keywords.
	 */
	private void removeDeletedKeywords() {
		@SuppressWarnings("unchecked")
		Collection<String> intersection = CollectionUtils.intersection(entry.getKeywordsAsStringList(), parsedKeyword);
		@SuppressWarnings("unchecked")
		Collection<String> subtraction = CollectionUtils.subtract(entry.getKeywordsAsStringList(), intersection);
		List<KeywordEntity> keywords = new ArrayList<KeywordEntity>();
		for (String string : subtraction) {
			Query query = entityManager.createQuery("Select k FROM Keyword k WHERE k.name = :name");
			query.setParameter("name", string);
			KeywordEntity keyword = (KeywordEntity) query.getSingleResult();
			keywords.add(keyword);
		}
		for (KeywordEntity keyword : keywords) {
			entry.removeKeyword(keyword);
			entityManager.persist(keyword);
		}
	}

	/**
	 * Creates new rntry.
	 * 
	 * @return outcome.
	 */
	public String create() {
		entityManager.getTransaction().begin();
		try {
			entityManager.persist(entry);
			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			log.error(e);
			return "pretty:";
		}
		return "pretty:adminIndex";

	}

	/**
	 * Gets all entries.
	 * 
	 * @return entries.
	 */
	public List<EntryEntity> getEntries() {
		@SuppressWarnings("unchecked")
		List<EntryEntity> entries = entityManager.createQuery("SELECT e FROM Entry e ORDER BY created DESC")
				.getResultList();
		return entries;
	}

	/**
	 * Gets entries count.
	 * 
	 * @return entries count.
	 */
	public long getEntriesCount() {
		Query query = entityManager.createQuery("SELECT COUNT(e.id) FROM Entry e WHERE e.draft = :draft");
		query.setParameter("draft", false);
		return (Long) query.getSingleResult();
	}

	/**
	 * Gets entries paged.
	 * 
	 * @return entries paged.
	 */
	public List<EntryEntity> getEntriesPaged() {
		return getEntries(page, DEFAULT_PAGE_SIZE);
	}

	/**
	 * Get entries pages.
	 * 
	 * @param pageNumber
	 *            - page number.
	 * @param pageSize
	 *            - page size.
	 * @return entries paged.
	 */
	public List<EntryEntity> getEntries(int pageNumber, int pageSize) {
		Query query = entityManager.createQuery("SELECT e FROM Entry e WHERE e.draft = :draft ORDER BY e.created DESC");
		query.setParameter("draft", false);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<EntryEntity> list = query.getResultList();
		return list;
	}

	/**
	 * Get pages count.
	 * 
	 * @return pages count.
	 */
	public int getPagesCount() {
		return (int) (getEntriesCount() / DEFAULT_PAGE_SIZE + 1);
	}

	/**
	 * Clean current entry.
	 */
	public void clean() {
		entry = new EntryEntity();
	}

	/**
	 * @return the entryId
	 */
	public long getEntryID() {
		return entryID;
	}

	/**
	 * @param entryID
	 *            the entryId to set
	 */
	public void setEntryID(long entryID) {
		this.entryID = entryID;
	}

	/**
	 * Load entry from db.
	 * 
	 * @return outcome.
	 */
	public String loadEntry() {
		EntryEntity entry = entityManager.find(EntryEntity.class, entryID);
		if (entry == null) {
			FacesContextUtils.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		this.entry = entry;
		return null;
	}

	/**
	 * Delete entry.
	 * 
	 * @return outcome
	 */
	public String delete() {
		entityManager.getTransaction().begin();
		try {
			entry.clearKeywords();
			removeUnlinkedKeywords();
			entityManager.remove(entry);
			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			log.error(e);
			return "pretty:";
		}

		return "pretty:adminIndex";
	}

	/**
	 * Gets unparsed keywords.
	 * 
	 * @return unparsed keywords.
	 */
	public String getUnparsedKeywords() {
		String ret = TextUtils.generateList(entry.getKeywords(), ", ", new RetrieveFieldClosure() {

			@Override
			public String retrieveField(Object o) {
				return ((KeywordEntity) o).getName();
			}
		});
		return ret;
	}

	/**
	 * Sets unparsed keywords.
	 * 
	 * @param unparsedKeywords
	 *            unparsed keywords.
	 */
	public void setUnparsedKeywords(String unparsedKeywords) {
		parsedKeyword = TextUtils.parseList(unparsedKeywords, ",");
	}

	/**
	 * Gets current page number.
	 * 
	 * @return current page number.
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Sets current page number.
	 * 
	 * @param page
	 *            page number.
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * Sets page number as string.
	 * 
	 * @param page
	 *            page number as string.
	 */
	public void setPage(String page) {
		try {
			this.page = Integer.parseInt(page);
		} catch (NumberFormatException e) {
			this.page = 1;
		}
	}

}
