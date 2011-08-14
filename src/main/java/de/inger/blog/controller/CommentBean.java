package de.inger.blog.controller;

import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlDataTable;

import javax.persistence.EntityManager;
import javax.security.auth.Subject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.inger.blog.faces.FacesContextUtils;
import de.inger.blog.model.Comment;
import de.inger.blog.model.CommentEntity;
import de.inger.blog.model.Entry;
import de.inger.blog.model.EntryEntity;
import de.inger.blog.model.UserEntity;
import de.inger.blog.persistence.PersistenceManager;
import de.inger.blog.security.principals.RegisteredPrincipal;

/**
 * Comments controller.
 * 
 * @author Igor Inger
 * 
 */
@ManagedBean
@RequestScoped
public class CommentBean {

	/**
	 * Logger.
	 */
	private Log log = LogFactory.getLog(CommentBean.class);

	/**
	 * Temporary comment.
	 */
	private Comment comment = new CommentEntity();

	/**
	 * comments html data table.
	 */
	private HtmlDataTable commentsDataTable;

	/**
	 * Returns the comment value.
	 * 
	 * @return the comment value
	 */
	public Comment getComment() {
		return comment;
	}

	/**
	 * Sets the comment value.
	 * 
	 * @param comment
	 *            the comment value to set
	 */
	public void setComment(CommentEntity comment) {
		this.comment = comment;
	}

	/**
	 * Save comment.
	 * 
	 * @return outcome.
	 */
	public String saveComment() {
		log.trace("CommentBean.saveComment()");

		EntryEntity entry = FacesContextUtils.evaluateExpressionGet("#{entryBean.entry}", EntryEntity.class);
		Subject subject = FacesContextUtils.evaluateExpressionGet("#{subjectBean.subject}", Subject.class);
		Set<RegisteredPrincipal> set = subject.getPrincipals(RegisteredPrincipal.class);
		UserEntity entity = null;
		if (!set.isEmpty()) {
			entity = set.iterator().next().getUser();
		}

		EntityManager manager = PersistenceManager.getEntityManager();
		manager.getTransaction().begin();
		try {
			comment.setEntry(entry);
			comment.setAuthor(entity);
			manager.persist(comment);
			entry.getComments().add(comment);
			manager.merge(entry);
			manager.getTransaction().commit();
		} catch (Exception e) {
			log.error(e);
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
		}
		comment = new CommentEntity();
		return "pretty:";
	}

	/**
	 * 
	 * 
	 * @return comments data table.
	 */
	public HtmlDataTable getCommentsDataTable() {
		return commentsDataTable;
	}

	/**
	 * 
	 * @param commentsDataTable
	 *            comments datatable
	 */
	public void setCommentsDataTable(HtmlDataTable commentsDataTable) {
		this.commentsDataTable = commentsDataTable;
	}

	/**
	 * Delete comment.
	 * 
	 * @return outcome.
	 */
	public String delete() {
		log.trace("CommentBean.delete()");
		CommentEntity comment = (CommentEntity) commentsDataTable.getRowData();
		log.debug(comment);

		EntityManager manager = PersistenceManager.getEntityManager();

		manager.getTransaction().begin();
		try {
			comment = manager.merge(comment);
			Entry entry = comment.getEntry();
			entry.removeComment(comment);
			manager.merge(entry);
			manager.remove(comment);
			manager.getTransaction().commit();
		} catch (RuntimeException e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			log.error(e);
		}

		return "pretty:";

	}

}
