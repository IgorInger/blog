package de.inger.blog.security;

import java.util.Collections;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;

/**
 * Security configuration.
 * 
 * @author Igor Inger
 * 
 */
public class SecurityConfiguration extends Configuration {

	/**
	 * Constructor.
	 */
	public SecurityConfiguration() {
	}

	/**
	 * Configuration.
	 * 
	 * @param configuration
	 *            configuration.
	 */
	public SecurityConfiguration(Configuration configuration) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
		if (name.equals("de.inger.blog.db")) {
			AppConfigurationEntry db = new AppConfigurationEntry(DBLoginModule.class.getName(),
					LoginModuleControlFlag.REQUIRED, Collections.EMPTY_MAP);
			return new AppConfigurationEntry[] { db };
		}
		if (name.equals("de.inger.blog.openid")) {
			AppConfigurationEntry openid = new AppConfigurationEntry(OpenidLoginModule.class.getName(),
					LoginModuleControlFlag.REQUIRED, Collections.EMPTY_MAP);
			return new AppConfigurationEntry[] { openid };
		}

		return null;
	}

}
