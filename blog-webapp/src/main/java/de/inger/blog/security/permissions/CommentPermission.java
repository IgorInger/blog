package de.inger.blog.security.permissions;

import java.security.BasicPermission;

/**
 * Comment permission.
 * 
 * @author Igor Inger
 * 
 */
public class CommentPermission extends BasicPermission {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2238933505864803456L;

	/**
	 * Comment permission.
	 * 
	 * @param actions
	 *            actions.
	 */
	public CommentPermission(String actions) {
		super("comment", actions);
	}

}
