package de.inger.blog.security;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Composite policy.
 * 
 * @author Igor Inger
 * 
 */
public class CompositePolicy extends Policy {

	/**
	 * Policies.
	 */
	private List<Policy> policies = Collections.emptyList();

	/**
	 * Constructor.
	 * 
	 * @param policies
	 *            policies.
	 */
	public CompositePolicy(List<Policy> policies) {
		this.policies = policies;
	}

	@Override
	public PermissionCollection getPermissions(ProtectionDomain domain) {
		return getPermissionsFromObject(domain);
	}

	@Override
	public PermissionCollection getPermissions(CodeSource codeSource) {
		return getPermissionsFromObject(codeSource);
	}

	/**
	 * Gets permissions from object.
	 * 
	 * @param object
	 *            ProctectionDomain or CodeSource value.
	 * @return permissions.
	 */
	private Permissions getPermissionsFromObject(Object object) {
		Permissions permissions = new Permissions();
		for (Policy policy : policies) {
			PermissionCollection collection = null;
			if (object instanceof ProtectionDomain) {
				collection = policy.getPermissions((ProtectionDomain) object);
			} else if (object instanceof CodeSource) {
				collection = policy.getPermissions((CodeSource) object);
			} else {
				throw new SecurityException();
			}
			Enumeration<Permission> enumeration = collection.elements();
			while (enumeration.hasMoreElements()) {
				permissions.add(enumeration.nextElement());
			}
		}
		return permissions;
	}

	@Override
	public void refresh() {
		for (Policy policy : policies) {
			policy.refresh();
		}
	}

	@Override
	public boolean implies(ProtectionDomain domain, Permission permission) {
		for (Policy policy : policies) {
			if (policy.implies(domain, permission)) {
				return true;
			}
		}
		return false;
	}

}
