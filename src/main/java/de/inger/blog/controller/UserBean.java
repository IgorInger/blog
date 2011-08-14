package de.inger.blog.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.inger.blog.faces.FacesContextUtils;
import de.inger.blog.model.RoleEntity;
import de.inger.blog.model.UserEntity;
import de.inger.blog.persistence.PersistenceManager;
import de.inger.blog.security.UserCallback;

/**
 * User controller.
 * 
 * @author Igor Inger
 * 
 */
@ManagedBean
@RequestScoped
public class UserBean implements CallbackHandler {

	/**
	 * User.
	 */
	private UserEntity user = new UserEntity();

	/**
	 * Login context.
	 */
	private LoginContext loginContext;

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(UserBean.class);

	/**
	 * Entity manager.
	 */
	private EntityManager entityManager;

	/**
	 * User id.
	 */
	private long userID;

	/**
	 * User bean.
	 */
	public UserBean() {
		Subject subject = FacesContextUtils.evaluateExpressionGet("#{subjectBean.subject}", Subject.class);
		try {
			loginContext = new LoginContext("de.inger.blog.db", subject, this);
		} catch (LoginException e) {
			log.error(e);
		}
		entityManager = PersistenceManager.getEntityManager();
	}

	/**
	 * Returns the user value.
	 * 
	 * @return the user value
	 */
	public UserEntity getUser() {
		return user;
	}

	/**
	 * Sets the user value.
	 * 
	 * @param user
	 *            the user value to set
	 */
	public void setUser(UserEntity user) {
		this.user = user;
	}

	/**
	 * Login.
	 * 
	 * @return outcome
	 */
	public String login() {
		try {
			loginContext.login();
		} catch (LoginException e) {
			log.warn(e);
		}
		return "pretty:";
	}

	/**
	 * Logout.
	 * 
	 * @return outcome.
	 */
	public String logout() {
		try {
			loginContext.logout();
		} catch (LoginException e) {
			log.warn(e);
		}
		return "pretty:";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
	 */
	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (Callback callback : callbacks) {
			if (callback instanceof UserCallback) {
				UserCallback userCallback = (UserCallback) callback;
				userCallback.setUser(user);
			} else {
				throw new UnsupportedCallbackException(callback);
			}
		}
	}

	/**
	 * User registration.
	 */
	public void registration() {

		entityManager.getTransaction().begin();
		try {
			entityManager.persist(user);
			entityManager.getTransaction().commit();
		} catch (PersistenceException e) {
			log.error(e);
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
		}

		setUser(new UserEntity());
	}

	/**
	 * Gets users.
	 * 
	 * @return users.
	 */
	@SuppressWarnings("unchecked")
	public List<UserEntity> getUsers() {
		EntityManager manager = PersistenceManager.getEntityManager();
		return manager.createQuery("SELECT u FROM User u").getResultList();
	}

	/**
	 * @return the userID
	 */
	public long getUserID() {
		return userID;
	}

	/**
	 * @param userID
	 *            the userID to set
	 */
	public void setUserID(long userID) {
		this.userID = userID;
	}

	/**
	 * Load user from db.
	 */
	public void loadUser() {
		EntityManager manager = PersistenceManager.getEntityManager();
		user = manager.find(UserEntity.class, userID);
	}

	/**
	 * Save user details.
	 * 
	 * @return outcome.
	 */
	public String saveUser() {
		EntityManager manager = PersistenceManager.getEntityManager();
		manager.getTransaction().begin();
		try {
			manager.persist(user);
			manager.getTransaction().commit();
		} catch (Exception e) {
			log.error(e);
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
		}
		return "pretty:";
	}

	/**
	 * @return the test
	 */
	public List<String> getTest() {
		List<RoleEntity> roleEntities = user.getRoles();
		List<String> strings = new ArrayList<String>();
		for (RoleEntity roleEntity : roleEntities) {
			strings.add(roleEntity.getName());
		}
		return strings;
	}

	/**
	 * @param test
	 *            the test to set
	 */
	public void setTest(List<String> test) {
		EntityManager manager = PersistenceManager.getEntityManager();
		List<RoleEntity> roleEntities = new ArrayList<RoleEntity>();
		for (String string : test) {
			RoleEntity roleEntity = manager.find(RoleEntity.class, string);
			roleEntities.add(roleEntity);
		}
		user.setRoles(roleEntities);
	}

	/**
	 * Gets available roles.
	 * 
	 * @return roles.
	 */
	public List<SelectItem> getAvailableRoles() {
		EntityManager manager = PersistenceManager.getEntityManager();
		Query query = manager.createQuery("SELECT r FROM Role r");
		@SuppressWarnings("unchecked")
		List<RoleEntity> list = query.getResultList();
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		for (RoleEntity roleEntity : list) {
			SelectItem selectItem = new SelectItem(roleEntity.getName());
			selectItems.add(selectItem);
		}
		return selectItems;
	}

}
