package de.inger.blog.security;

import javax.security.auth.callback.Callback;

import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.ParameterList;

/**
 * Openid callback.
 * 
 * @author Igor Inger
 * 
 */
public class OpenidCallback implements Callback {

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
	 */
	private String authenticationURL;

	/**
	 * Query string.
	 */
	private String queryString;

	/**
	 * Consumer manager.
	 */
	private ConsumerManager consumerManager;
	
	/**
	 * Gets discovery information.
	 * 
	 * @return discovery information.
	 */
	public DiscoveryInformation getDiscoveryInformation() {
		return discoveryInformation;
	}

	/**
	 * Sets discovery information.
	 * 
	 * @param discoveryInformation
	 *            discovery information.
	 */
	public void setDiscoveryInformation(DiscoveryInformation discoveryInformation) {
		this.discoveryInformation = discoveryInformation;
	}

	/**
	 * Gets parameter list.
	 * 
	 * @return parameter list.
	 */
	public ParameterList getParameterList() {
		return parameterList;
	}

	/**
	 * Sets parameter list.
	 * 
	 * @param parameterList
	 *            parameter list.
	 */
	public void setParameterList(ParameterList parameterList) {
		this.parameterList = parameterList;
	}

	/**
	 * Gets authentication url.
	 * 
	 * @return authentication url.
	 */
	public String getAuthenticationURL() {
		return authenticationURL;
	}

	/**
	 * Sets authentication url.
	 * 
	 * @param authenticationURL
	 *            authentication url.
	 */
	public void setAuthenticationURL(String authenticationURL) {
		this.authenticationURL = authenticationURL;
	}

	/**
	 * Gets query string.
	 * 
	 * @return query string.
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * Sets query string.
	 * 
	 * @param queryString
	 *            query string.
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * @return the consumerManager
	 */
	public ConsumerManager getConsumerManager() {
		return consumerManager;
	}

	/**
	 * @param consumerManager the consumerManager to set
	 */
	public void setConsumerManager(ConsumerManager consumerManager) {
		this.consumerManager = consumerManager;
	}

}
