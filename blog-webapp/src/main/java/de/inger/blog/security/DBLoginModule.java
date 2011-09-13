package de.inger.blog.security;

import java.io.IOException;
import java.security.Principal;

import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.inger.blog.model.UserEntity;
import de.inger.blog.persistence.PersistenceManager;
import de.inger.blog.security.principals.RegisteredPrincipal;

/**
 * Data base login module.
 * 
 * @author Igor Inger
 * 
 */
public class DBLoginModule implements javax.security.auth.spi.LoginModule {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(DBLoginModule.class);

	/**
	 * Subject.
	 */
	private Subject subject;

	/**
	 * Callback handler.
	 */
	private CallbackHandler callbackHandler;

	/**
	 * User entity.
	 */
	private UserEntity registeredUser;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	@Override
	public boolean abort() throws LoginException {
		log.trace("DBLoginModule.abort()");
		return true;
	}

	@Override
	public boolean commit() throws LoginException {
		log.trace("DBLoginModule.commit()");
		RegisteredPrincipal registeredPrincipal = new RegisteredPrincipal(registeredUser);
		Set<Principal> principals = subject.getPrincipals();
		principals.add(registeredPrincipal);
		return true;
	}

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ? extends Object> sharedState,
			Map<String, ? extends Object> options) {
		log.trace("DBLoginModule.initialize()");
		this.subject = subject;
		this.callbackHandler = callbackHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	@Override
	public boolean login() throws LoginException {
		log.trace("DBLoginModule.login()");
		UserCallback userCallback = new UserCallback();
		try {
			callbackHandler.handle(new Callback[] { userCallback });
		} catch (UnsupportedCallbackException e) {
			throw new LoginException(e.getMessage());
		} catch (IOException e) {
			throw new LoginException(e.getMessage());
		}

		UserEntity user = userCallback.getUser();

		EntityManager manager = PersistenceManager.getEntityManager();

		String queryPlain = String.format("SELECT u FROM User u WHERE u.email = :email AND u.password = :password");
		Query query = manager.createQuery(queryPlain);
		query.setParameter("email", user.getEmail());
		query.setParameter("password", user.getPassword());

		try {
			registeredUser = (UserEntity) query.getSingleResult();
		} catch (NoResultException e) {
			throw new LoginException(e.toString());
		}

		return true;
	}

	@Override
	public boolean logout() throws LoginException {
		log.trace("DBLoginModule.logout()");
		subject.getPrincipals().clear();
		return true;
	}

}
