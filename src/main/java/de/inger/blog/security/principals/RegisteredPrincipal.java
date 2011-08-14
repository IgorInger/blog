package de.inger.blog.security.principals;

import java.security.Principal;

import de.inger.blog.model.UserEntity;

/**
 * Registered principal.
 * 
 * @author Igor Inger
 * 
 */
public class RegisteredPrincipal implements Principal {

	/**
	 * Name.
	 */
	private UserEntity user;

	/**
	 * Constructor.
	 * 
	 * @param user
	 *            name.
	 */
	public RegisteredPrincipal(UserEntity user) {
		this.user = user;
	}

	@Override
	public String getName() {
		if (user.getOpenid() != null) {
			return user.getOpenid();
		}
		return user.getName();
	}

	/**
	 * Gets user.
	 * @return user.
	 */
	public UserEntity getUser() {
		return user;
	}

}
