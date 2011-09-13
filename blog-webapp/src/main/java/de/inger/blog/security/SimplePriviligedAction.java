package de.inger.blog.security;

import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;

/**
 * Siple priviliged action.
 * 
 * @author Igor Inger
 * 
 */
public class SimplePriviligedAction implements PrivilegedAction<Void> {

	/**
	 * Permission.
	 */
	private Permission permission;

	/**
	 * Constructor.
	 * 
	 * @param permission
	 *            permission.
	 */
	public SimplePriviligedAction(Permission permission) {
		this.permission = permission;
	}

	@Override
	public Void run() {
		AccessController.checkPermission(permission);
		return null;
	}

}
