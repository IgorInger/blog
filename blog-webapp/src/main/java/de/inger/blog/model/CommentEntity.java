package de.inger.blog.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.petebevin.markdown.MarkdownProcessor;

/**
 * Comments entry.
 * 
 * @author Igor Inger
 * 
 */
@Entity(name = "Comment")
@Table(name = "b_comment")
public class CommentEntity implements Comment {

	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -8056950422106609366L;

	/**
	 * Id.
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Content.
	 */
	@Column(columnDefinition = "text")
	private String content;

	/**
	 * Parent entry.
	 */
	@ManyToOne(targetEntity = EntryEntity.class)
	private Entry entry;

	/**
	 * Author.
	 */
	@ManyToOne(targetEntity = UserEntity.class)
	private UserEntity author;

	/**
	 * Creation date.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date created = new Date();

	/**
	 * Constructor.
	 */
	public CommentEntity() {
		this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param content
	 *            content.
	 */
	public CommentEntity(String content) {
		setContent(content);
	}

	/**
	 * Returns the id value.
	 * 
	 * @return the id value
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Returns the content value.
	 * 
	 * @return the content value
	 */
	@Override
	public String getContent() {
		return content;
	}

	/**
	 * Converts markdown plain text to html.
	 * 
	 * @return parsed html text.
	 */
	@Transient
	public String getMarkdownContent() {
		return new MarkdownProcessor().markdown(content);
	}

	/**
	 * Sets the content value.
	 * 
	 * @param content
	 *            the content value to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Returns the entry value.
	 * 
	 * @return the entry value
	 */
	@Override
	public Entry getEntry() {
		return entry;
	}

	/**
	 * Sets the entry value.
	 * 
	 * @param entry
	 *            the entry value to set
	 */
	@Override
	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	@Override
	public Date getCreated() {
		return created;
	}

	@Override
	public void setCreated(Date date) {
		this.created = date;
	}

	/**
	 * @return the author
	 */
	public UserEntity getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(UserEntity author) {
		this.author = author;
	}

}
