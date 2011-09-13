package de.inger.blog.model;

import java.io.Serializable;
import java.util.List;

/**
 * Entry interface.
 * 
 * @author Igor Inger
 * 
 */
public interface Entry extends Serializable, CreationHistory {

	/**
	 * Gets title.
	 * 
	 * @return title.
	 */
	String getTitle();

	/**
	 * Sets title.
	 * 
	 * @param title
	 *            title.
	 */
	void setTitle(String title);

	/**
	 * Gets content.
	 * 
	 * @return content.
	 */
	String getContent();

	/**
	 * Sets content.
	 * 
	 * @param content
	 *            content.
	 */
	void setContent(String content);

	/**
	 * Gets comments.
	 * 
	 * @return comments.
	 */
	List<Comment> getComments();

	/**
	 * Adds comment.
	 * 
	 * @param comment
	 *            comment to add.
	 */
	void addComment(Comment comment);

	/**
	 * Remove comment comment.
	 * 
	 * @param comment
	 *            comment to remove.
	 */
	void removeComment(Comment comment);

	/**
	 * Clears comments.
	 */
	void clearComments();

	/**
	 * Gets keywords.
	 * 
	 * @return keywords.
	 */
	List<Keyword> getKeywords();

	/**
	 * Adds keyword.
	 * 
	 * @param k
	 *            keyword
	 */
	void addKeyword(Keyword k);

	/**
	 * Removes keyword.
	 * 
	 * @param k
	 *            keyword
	 */
	void removeKeyword(Keyword k);

	/**
	 * Remove all keyword.
	 */
	void clearKeywords();

	/**
	 * Is draft.
	 * 
	 * @return is draft.
	 */
	boolean isDraft();

	/**
	 * Sets draft.
	 * 
	 * @param draft
	 *            draft status.
	 */
	void setDraft(boolean draft);

}
