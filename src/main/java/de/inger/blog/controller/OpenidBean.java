package de.inger.blog.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;

import de.inger.blog.faces.FacesContextUtils;
import de.inger.blog.security.OpenidCallback;

/**
 * Openid bean controller.
 * 
 * @author Igor Inger
 * 
 */
@ManagedBean
@SessionScoped
public class OpenidBean implements CallbackHandler {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(OpenidBean.class);

	/**
	 * Openid url.
	 */
	private String url = "http://igor.inger.myopenid.com";

	/**
	 * Login context.
	 */
	private LoginContext loginContext;

	/**
	 * Consumer manager.
	 */
	private ConsumerManager consumerManager;

	/**
	 * Discovery information.
	 */
	private DiscoveryInformation discoveryInformation;

	/**
	 * Parameter list.
	 */
	private ParameterList parameterList;

	/**
	 * Authentication url.
	 * 
	 * @TODO CONSTANT!
	 */
	private String authenticationURL = "http://localhost:8080/blog/authenticate";

	/**
	 * Query string.
	 */
	private String queryString;

	/**
	 * Constructor.
	 */
	public OpenidBean() {
		Subject subject = FacesContextUtils.evaluateExpressionGet("#{subjectBean.subject}", Subject.class);
		try {
			loginContext = new LoginContext("de.inger.blog.openid", subject, this);
		} catch (LoginException e) {
			log.error(e);
		}

		consumerManager = new ConsumerManager();
	}

	/**
	 * Authenticate.
	 * 
	 * @return outcome.
	 */
	public String authenticate() {
		HttpServletRequest request = FacesContextUtils.getHttpServletRequest();
		parameterList = new ParameterList(request.getParameterMap());
		queryString = request.getQueryString();

		try {
			loginContext.login();
		} catch (LoginException e) {
			log.error(e);
		}

		return "pretty:login";
	}

	/**
	 * Request.
	 */
	public void request() {

		@SuppressWarnings("rawtypes")
		List discoveries = Collections.EMPTY_LIST;
		try {
			discoveries = consumerManager.discover(getUrl());
		} catch (DiscoveryException e) {
			log.error(e);
			return;
		}

		discoveryInformation = consumerManager.associate(discoveries);

		AuthRequest authRequest = null;
		try {
			authRequest = consumerManager.authenticate(discoveryInformation, authenticationURL);
		} catch (MessageException e) {
			log.error(e);
			return;
		} catch (ConsumerException e) {
			log.error(e);
			return;
		}

		String url = authRequest.getDestinationUrl(true);
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (IOException e) {
			log.error(e);
		}
	}

	/**
	 * Sets url.
	 * 
	 * @return url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Gets url.
	 * 
	 * @param url
	 *            url.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (Callback callback : callbacks) {
			if (callback instanceof OpenidCallback) {
				OpenidCallback openidCallback = (OpenidCallback) callback;
				openidCallback.setAuthenticationURL(authenticationURL);
				openidCallback.setDiscoveryInformation(discoveryInformation);
				openidCallback.setParameterList(parameterList);
				openidCallback.setQueryString(queryString);
				openidCallback.setConsumerManager(consumerManager);
			} else {
				throw new UnsupportedCallbackException(callback);
			}
		}
	}

}
