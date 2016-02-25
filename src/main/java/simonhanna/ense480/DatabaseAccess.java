package simonhanna.ense480;

import javax.persistence.*;

import simonhanna.ense480.Entities.User;

public final class DatabaseAccess {
	
	private static final String UNIT_NAME = "simonhanna.ense480.aiproject";
	
	public static EntityManager getEntityManager() {
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(UNIT_NAME);
		EntityManager entityManager =  factory.createEntityManager();
		
		return entityManager;

	}

	public static void addUser(String alias) {	
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(UNIT_NAME);
		EntityManager entityManager =  factory.createEntityManager();
		
		entityManager.getTransaction().begin();
		
		User user = new User(alias);
		entityManager.persist(user);
		
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
