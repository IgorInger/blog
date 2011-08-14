package de.inger.blog.security.principals;

import de.inger.blog.model.UserEntity;

/**
 * Administration principal.
 * 
 * @author Igor Inger
 * 
 */
public class AdministratorPrincipal extends RegisteredPrincipal {

	/**
	 * Constructor name.
	 * 
	 * @param entity
	 *            user.
	 */
	public AdministratorPrincipal(UserEntity entity) {
		super(entity);
	}

}
