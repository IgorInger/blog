package de.inger.blog.security;

import java.io.IOException;
import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.Configuration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.PrettyContext;

/**
 * Security filter.
 * 
 * @author Igor Inger
 * 
 */
public class SecurityFilter implements Filter {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(SecurityFilter.class);

	@Override
	public void destroy() {
		log.trace("SecurityFilter.destroy()");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		log.debug(servletRequest.getRequestURI());
		log.debug(PrettyContext.getCurrentInstance((HttpServletRequest) request).getRequestQueryString());
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		log.debug("Try to obtain the default configuration.");
		Configuration configuration = null;
		try {
			configuration = Configuration.getConfiguration();
		} catch (SecurityException e) {
			log.info(e.getMessage());
		}

		if (configuration != null) {
			log.debug("Try to wrap the default configuration.");
		}

		SecurityConfiguration securityConfiguration = new SecurityConfiguration(configuration);

		log.debug("Try to set configuration");
		try {
			Configuration.setConfiguration(securityConfiguration);
		} catch (SecurityException e) {
			log.error(e);
		}

		List<Policy> policies = new ArrayList<Policy>();

		log.debug("Try to obtain policy.");
		try {
			Policy wrappedPolicy = Policy.getPolicy();
			policies.add(wrappedPolicy);
		} catch (SecurityException e) {
			log.warn(e);
		}

		SecurityPolicy securityPolicy = new SecurityPolicy();
		policies.add(securityPolicy);
		CompositePolicy compositePolicy = new CompositePolicy(policies);

		log.debug("Try to set policy.");
		try {
			Policy.setPolicy(compositePolicy);
		} catch (SecurityException e) {
			log.error(e);
		}
	}

}
