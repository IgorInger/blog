package de.inger;

import java.beans.PersistenceDelegate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;


public class TestH {

	@Test
	public void test() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("blog");
		
	}

}
