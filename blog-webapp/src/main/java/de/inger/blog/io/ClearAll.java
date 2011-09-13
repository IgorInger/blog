package de.inger.blog.io;

import javax.persistence.EntityManager;

import de.inger.blog.persistence.PersistenceManager;

public class ClearAll {

	public ClearAll() {
		
		EntityManager manager = PersistenceManager.getEntityManager();
		manager.getTransaction().begin();
		
		manager.createNativeQuery("DELETE FROM b_entry_b_comment").executeUpdate();
		manager.createNativeQuery("DELETE FROM b_comment").executeUpdate();
				
		manager.createNativeQuery("DELETE FROM b_entry_b_keyword").executeUpdate();
		manager.createNativeQuery("DELETE FROM b_keyword_b_entry").executeUpdate();		
		
		manager.createNativeQuery("DELETE FROM b_entry").executeUpdate();
		manager.createNativeQuery("DELETE FROM b_keyword").executeUpdate();
		manager.createNativeQuery("DELETE FROM b_user").executeUpdate();
		
		manager.getTransaction().commit();

	}

	public static void main(String[] args) {
		new ClearAll();
	}
}
