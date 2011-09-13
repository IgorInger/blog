package de.inger.blog.model;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Keyword.
 * 
 * @author Igor Inger
 * 
 */
@Entity(name = "Keyword")
@Table(name = "b_keyword")
@NamedQueries({ @NamedQuery(name = "findAll", query = "SELECT k FROM Keyword k") })
public class KeywordEntity implements Keyword {

	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 3870488457265240289L;

	/**
	 * Id.
	 */
	@Id
	private String name;

	/**
	 * Parented entries.
	 */
	@ManyToMany(targetEntity = EntryEntity.class)
	private List<Entry> entries = new ArrayList<Entry>();

	/**
	 * Contructor.
	 */
	public KeywordEntity() {

	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name.
	 */
	public KeywordEntity(String name) {
		setName(name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public List<Entry> getEntries() {
		return entries;
	}

}
