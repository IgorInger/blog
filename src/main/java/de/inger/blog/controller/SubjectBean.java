package de.inger.blog.controller;

import java.util.Iterator;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.security.auth.Subject;

import de.inger.blog.security.principals.RegisteredPrincipal;

/**
 * Subject bean.
 * 
 * @author Igor Inger
 * 
 */
@ManagedBean
@SessionScoped
public class SubjectBean {

	/**
	 * Security context subject.
	 */
	private Subject subject;

	/**
	 * Constructor.
	 */
	public SubjectBean() {
		subject = new Subject();
	}

	/**
	 * Gets subject.
	 * 
	 * @return subject.
	 */
	public Subject getSubject() {
		return subject;
	}

	/**
	 * Gets name of registered principal.
	 * 
	 * @return name.
	 */
	public String getName() {
		Set<RegisteredPrincipal> set = subject.getPrincipals(RegisteredPrincipal.class);
		Iterator<RegisteredPrincipal> iterator = set.iterator();
		if (iterator.hasNext()) {
			RegisteredPrincipal principal = iterator.next();
			return principal.getName();
		}
		return null;
	}

	/**
	 * Is authenticated.
	 * @return true or false.
	 */
	public boolean isAuthenticated() {
		if (subject.getPrincipals(RegisteredPrincipal.class).isEmpty()) {
			return false;
		}
		return true;
	}

}
