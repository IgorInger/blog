package de.inger.blog.model;

import java.io.Serializable;

/**
 * Comment entity interface.
 * 
 * @author Igor Inger
 * 
 */
public interface Comment extends Serializable, CreationHistory {

	/**
	 * Gets comment content.
	 * 
	 * @return comment content.
	 */
	String getContent();

	/**
	 * Sets comment content.
	 * 
	 * @param content
	 *            - comment content.
	 */
	void setContent(String content);

	/**
	 * Gets parent entry entity.
	 * 
	 * @return parent entry entity.
	 */
	Entry getEntry();

	/**
	 * Sets parent entry entity.
	 * 
	 * @param entry
	 *            parent entry entity.
	 */
	void setEntry(Entry entry);

	/**
	 * Gets author.
	 * 
	 * @return user.
	 */
	UserEntity getAuthor();

	/**
	 * Sets author.
	 * 
	 * @param user user.
	 */
	void setAuthor(UserEntity user);

	/**
	 * Entity type.
	 */
	Class<?> ENTITY_TYPE = CommentEntity.class;

}
