package de.inger.blog.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Role.
 * 
 * @author Igor Inger
 * 
 */
@Entity(name = "Role")
@Table(name = "b_role")
public class RoleEntity {

	/**
	 * Id.
	 */
	@Id
	private String name;

	/**
	 * Gets name.
	 * 
	 * @return name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 * 
	 * @param name
	 *            name.
	 */
	public void setName(String name) {
		this.name = name;
	}

}
