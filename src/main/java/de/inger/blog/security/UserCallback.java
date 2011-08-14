package de.inger.blog.security;

import javax.security.auth.callback.Callback;

import de.inger.blog.model.UserEntity;

/**
 * User callback.
 * 
 * @author Igor Inger
 * 
 */
public class UserCallback implements Callback {

	/**
	 * User.
	 */
	private UserEntity user;

	/**
	 * Gets user.
	 * 
	 * @return user.
	 */
	public UserEntity getUser() {
		return user;
	}

	/**
	 * Sets user.
	 * 
	 * @param user
	 *            user.
	 */
	public void setUser(UserEntity user) {
		this.user = user;
	}

}
