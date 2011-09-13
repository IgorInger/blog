package de.inger.blog.security;

import java.security.Permission;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.security.auth.Subject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Security context.
 * 
 * @author Igor Inger
 * 
 */
public abstract class SecurityContext {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(SecurityContext.class);

	/**
	 * Context.
	 */
	private SecurityContext() {
	}

	/**
	 * Checks permission.
	 * 
	 * @param permission
	 *            permission.
	 */
	public static void checkPermission(Permission permission) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		Subject subject = application.evaluateExpressionGet(context, "#{userBean.subject}", Subject.class);
		try {
			Subject.doAsPrivileged(subject, new SimplePriviligedAction(permission), null);
		} catch (SecurityException e) {
			log.warn(e);
		}
	}

}
