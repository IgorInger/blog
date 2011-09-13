package de.inger.blog.model;

import java.util.Date;

/**
 * Creation history interface.
 * 
 * @author Igor Inger
 * 
 */
public interface CreationHistory {

	/**
	 * Gets creation date.
	 * 
	 * @return creation date.
	 */
	Date getCreated();

	/**
	 * Sets creation date.
	 * 
	 * @param date
	 *            creation date.
	 */
	void setCreated(Date date);

}
