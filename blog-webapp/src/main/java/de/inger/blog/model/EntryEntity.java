package de.inger.blog.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.petebevin.markdown.MarkdownProcessor;

/**
 * Entry.
 * 
 * @author Igor Inger
 * 
 */
@Entity(name = "Entry")
@Table(name = "b_entry")
public class EntryEntity implements Entry {

	/**
	 * Logger.
	 */
	private static final long serialVersionUID = 1848143326621257692L;

	/**
	 * Id.
	 */
	@Id
	@GeneratedValue
	private long id;

	/**
	 * Title.
	 */
	private String title;

	/**
	 * Content.
	 */
	@Column(columnDefinition = "text")
	private String content;

	/**
	 * Creation date.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date created = new Date();

	/**
	 * Children comments.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = CommentEntity.class)
	private List<Comment> comments = new ArrayList<Comment>();

	/**
	 * Keywords.
	 */
	@ManyToMany(targetEntity = KeywordEntity.class)
	private List<Keyword> keywords = new ArrayList<Keyword>();

	/**
	 * Draft status.
	 */
	private boolean draft = true;

	/**
	 * Constructor.
	 */
	public EntryEntity() {
		this(null, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param content
	 *            content.
	 */
	public EntryEntity(String content) {
		this(null, content);
	}

	/**
	 * Сonstructor.
	 * 
	 * @param title
	 *            title.
	 * @param content
	 *            content.
	 */
	public EntryEntity(String title, String content) {
		setTitle(title);
		setContent(content);
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Gets content as markdown.
	 * 
	 * @return parsed content.
	 */
	@Transient
	public String getMarkdownContent() {
		return new MarkdownProcessor().markdown(getContent());
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public Date getCreated() {
		return created;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the comments value.
	 * 
	 * @return the comments value
	 */
	public List<Comment> getComments() {
		return comments;
	}

	@Override
	public void addComment(Comment comment) {
		comment.setEntry(this);
		getComments().add(comment);
	}

	@Override
	public void removeComment(Comment comment) {
		comment.setEntry(null);
		getComments().remove(comment);
	}

	/**
	 * Returns comments count.
	 * 
	 * @return comments count.
	 */
	@Transient
	public int getCommentsCount() {
		return comments.size();
	}

	@Override
	public List<Keyword> getKeywords() {
		return keywords;
	}

	@Override
	public void addKeyword(Keyword keyword) {
		if (!keywords.contains(keyword)) {
			keyword.getEntries().add(this);
			keywords.add(keyword);
		}
	}

	@Override
	public void removeKeyword(Keyword keyword) {
		if (keywords.contains(keyword)) {
			keyword.getEntries().remove(this);
			keywords.remove(keyword);
		}
	}

	/**
	 * Gets string list.
	 * 
	 * {'physik', 'blog', 'spots'}.
	 * 
	 * @return keyword as string list.
	 */
	public List<String> getKeywordsAsStringList() {
		List<String> strings = new ArrayList<String>();
		for (Keyword keyword : keywords) {
			strings.add(keyword.getName());
		}
		return strings;
	}

	@Override
	public boolean isDraft() {
		return draft;
	}

	@Override
	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	@Override
	public void setCreated(Date date) {
		this.created = date;
	}

	@Override
	public void clearComments() {
		for (Comment comment : comments) {
			comment.setEntry(null);
		}
		comments.clear();
	}

	@Override
	public void clearKeywords() {
		for (Keyword keyword : keywords) {
			keyword.getEntries().remove(this);
		}
		keywords.clear();
	}
}
