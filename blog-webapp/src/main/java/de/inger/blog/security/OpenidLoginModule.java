package de.inger.blog.security;

import java.io.IOException;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openid4java.association.AssociationException;

import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.Message;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;

import de.inger.blog.model.UserEntity;
import de.inger.blog.persistence.PersistenceManager;
import de.inger.blog.security.principals.RegisteredPrincipal;

/**
 * Openid login module.
 * 
 * @author Igor Inger
 * 
 */
public class OpenidLoginModule implements LoginModule {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(OpenidLoginModule.class);

	/**
	 * Subject.
	 */
	private Subject subject;

	/**
	 * Callback handler.
	 */
	private CallbackHandler callbackHandler;

	/**
	 * Identifier.
	 */
	private Identifier identifier;

	@Override
	public boolean abort() throws LoginException {
		log.trace("OpenIDLoginModule.abort()");
		return true;
	}

	@Override
	public boolean commit() throws LoginException {
		log.trace("OpenidLoginModule.commit()");
		log.debug(identifier);
		String id = identifier.getIdentifier();
		UserEntity entity = updateDbUser(id);
		entity.setName(id);
		RegisteredPrincipal principal = new RegisteredPrincipal(entity);
		subject.getPrincipals().add(principal);
		return true;
	}

	/**
	 * Update registered user.
	 * 
	 * 
	 * @param id
	 *            openid.
	 * 
	 * @return user entity.
	 * 
	 */
	private UserEntity updateDbUser(String id) {
		EntityManager manager = PersistenceManager.getEntityManager();
		Query query = manager.createQuery("SELECT u FROM User u WHERE u.openid = :openid");
		query.setParameter("openid", id);
		UserEntity userEntity = null;
		try {
			userEntity = (UserEntity) query.getSingleResult();
		} catch (NoResultException e) {
			log.debug(e);
			userEntity = new UserEntity();
			userEntity.setOpenid(id);
		}
		manager.getTransaction().begin();
		try {
			manager.persist(userEntity);
			manager.getTransaction().commit();
		} catch (Exception e) {
			log.error(e);
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
		}
		return userEntity;
	}

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ? extends Object> sharedState,
			Map<String, ? extends Object> options) {
		log.trace("OpenIDLoginModule.initialize()");
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
		log.trace("OpenIDLoginModule.login()");

		OpenidCallback openidCallback = new OpenidCallback();
		try {
			callbackHandler.handle(new Callback[] { openidCallback });
		} catch (IOException e) {
			log.info(e);
			throw new LoginException(e.getMessage());
		} catch (UnsupportedCallbackException e) {
			log.info(e);
			throw new LoginException(e.getMessage());
		}

		DiscoveryInformation discoveryInformation = openidCallback.getDiscoveryInformation();
		log.debug(discoveryInformation.getClaimedIdentifier().getIdentifier());
		String authenticationURL = openidCallback.getAuthenticationURL();
		String queryString = openidCallback.getQueryString();
		ParameterList parameterList = openidCallback.getParameterList();
		ConsumerManager consumerManager = openidCallback.getConsumerManager();

		String verifyURL = String.format("%s?%s", authenticationURL, queryString);

		VerificationResult verificationResult = null;
		try {
			verificationResult = consumerManager.verify(verifyURL, parameterList, discoveryInformation);
		} catch (MessageException e) {
			log.info(e);
			throw new LoginException(e.getMessage());
		} catch (DiscoveryException e) {
			log.info(e);
			throw new LoginException(e.getMessage());
		} catch (AssociationException e) {
			log.info(e);
			throw new LoginException(e.getMessage());
		} catch (Exception e) {
			log.error(e);
			throw new LoginException(e.getMessage());
		}

		Message message = verificationResult.getAuthResponse();

		if (!(message instanceof AuthSuccess)) {
			log.info(message.getClass().getName());
			throw new LoginException(message.getClass().getName());
		}

		identifier = verificationResult.getVerifiedId();

		return true;
	}

	@Override
	public boolean logout() throws LoginException {
		log.trace("OpenIDLoginModule.logout()");
		subject.getPrincipals().clear();
		return true;
	}

}
