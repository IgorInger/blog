package de.inger.blog.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Persistence manager.
 * 
 * @author Igor Inger
 * 
 */
public abstract class PersistenceManager {

	/**
	 * Entity manager.
	 */
	private static EntityManager em;

	/**
	 * Constructor.
	 */
	private PersistenceManager() {
	}

	/**
	 * Gets entity manager.
	 * 
	 * @return entity manager.
	 */
	public static EntityManager getEntityManager() {
		if (em == null) {
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("blog");
			em = factory.createEntityManager();
		}
		return em;
	}

}
