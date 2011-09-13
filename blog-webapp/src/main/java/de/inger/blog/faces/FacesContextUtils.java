package de.inger.blog.faces;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Faces context utils.
 * 
 * @author Igor Inger
 * 
 */
public abstract class FacesContextUtils {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(FacesContextUtils.class);

	/**
	 * Constructor.
	 */
	private FacesContextUtils() {
	}

	/**
	 * Gets external context.
	 * 
	 * @return external context.
	 */
	public static ExternalContext getExternalContext() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		return externalContext;
	}

	/**
	 * Gets http servlet request.
	 * 
	 * @return http servlet request.
	 */
	public static HttpServletRequest getHttpServletRequest() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		return (HttpServletRequest) externalContext.getRequest();
	}

	/**
	 * Gets http servlet response.
	 * 
	 * @return http servlet response.
	 */
	public static HttpServletResponse getHttpServletResponse() {
		HttpServletResponse httpServletResponse = null;
		try {
			httpServletResponse = (HttpServletResponse) getExternalContext().getResponse();
		} catch (ClassCastException e) {
			log.error(e);
		}
		return httpServletResponse;
	}

	/**
	 * Sends error.
	 * 
	 * @param errorCode
	 *            http error code.
	 */
	public static void sendError(int errorCode) {
		HttpServletResponse response = getHttpServletResponse();
		try {
			response.sendError(errorCode);
		} catch (IOException e) {
			log.error("Cannot send error.");
			log.error(e);
		}
	}

	/**
	 * Gets http session.
	 * 
	 * @return http session.
	 */
	public static HttpSession getHttpSession() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		return (HttpSession) externalContext.getSession(false);
	}

	/**
	 * Gets evaluated expression.
	 * 
	 * @param <T>
	 *            expected type.
	 * @param expression
	 *            expression.
	 * @param c
	 *            class.
	 * @return evaluated expression.
	 */
	public static <T> T evaluateExpressionGet(String expression, Class<? extends T> c) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		return application.evaluateExpressionGet(facesContext, expression, c);
	}

}
