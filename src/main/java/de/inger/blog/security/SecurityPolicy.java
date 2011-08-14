package de.inger.blog.security;

import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.Principal;
import java.security.ProtectionDomain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.inger.blog.security.permissions.CommentPermission;
import de.inger.blog.security.principals.AdministratorPrincipal;

/**
 * Security policy.
 * 
 * @author Igor Inger
 * 
 */
public class SecurityPolicy extends Policy {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(SecurityPolicy.class);

	@Override
	public PermissionCollection getPermissions(ProtectionDomain domain) {
		PermissionCollection permissions = new Permissions();
		Principal[] principals = domain.getPrincipals();
		for (Principal principal : principals) {
			if (principal instanceof AdministratorPrincipal) {
				permissions.add(new CommentPermission("save,delete"));
			}
		}
		// TODO Auto-generated method stub
		return permissions;
	}

	@Override
	public boolean implies(ProtectionDomain domain, Permission permission) {
		log.trace("SecurityPolicy.implies()");
		log.debug(permission);
		boolean implies = getPermissions(domain).implies(permission);
		log.debug(implies);
		return implies;
	}

}
