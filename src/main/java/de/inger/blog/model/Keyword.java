package de.inger.blog.model;

import java.io.Serializable;
import java.util.List;

/**
 * Keyword interface.
 * 
 * @author Igor Inger
 * 
 */
public interface Keyword extends Serializable {

	/**
	 * Gets name.
	 * 
	 * @return name.
	 */
	String getName();

	/**
	 * Sets name.
	 * 
	 * @param name
	 *            name.
	 */
	void setName(String name);

	/**
	 * Parented entries.
	 * 
	 * @return entries.
	 */
	List<Entry> getEntries();

}
