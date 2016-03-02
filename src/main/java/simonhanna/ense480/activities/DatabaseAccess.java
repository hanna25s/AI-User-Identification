package simonhanna.ense480.activities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import simonhanna.ense480.entities.*;

public final class DatabaseAccess {
	
	private static final String UNIT_NAME = "simonhanna.ense480.aiproject";
	private static EntityManager entityManager = Persistence.createEntityManagerFactory(UNIT_NAME).createEntityManager();
	
	public static EntityManager getEntityManager() {
		return entityManager;
	}

	public static void addUser(String alias) {		
		entityManager.getTransaction().begin();
		
		User user = new User(alias);
		entityManager.persist(user);
		
		entityManager.getTransaction().commit();
	}
	
	public static void addProfile(String name, User user) {	
		entityManager.getTransaction().begin();
		
		Profile profile = new Profile();
		profile.setUser(user);
		profile.setProfilename(name);
		
		entityManager.persist(profile);
		entityManager.getTransaction().commit();
	}
	
	public static List<User> getUserAliases() {
		
		List<User> users = new ArrayList<User>();
		users = entityManager.createQuery("SELECT u FROM User u").getResultList();
		return users;
	}
	
	public static void updateKeyMetrics(Profile profile, KeyMetric[][] keyMetric) {
		
		if(profile.getKeyMetrics() == null || profile.getKeyMetrics().size() == 0) {
			addKeyMetrics(profile, keyMetric);
			return;
		}

		entityManager.getTransaction().begin();
		profile.getKeyMetrics().forEach((km) -> {
			km.setNumberOfOccurences(km.getNumberOfOccurences() + keyMetric[km.getFrom()][km.getTo()].getNumberOfOccurences());
			km.setTime(km.getTime() + keyMetric[km.getFrom()][km.getTo()].getTime());
			entityManager.persist(km);
		});
		entityManager.getTransaction().commit();
	}
	
	public static void addKeyMetrics(Profile profile, KeyMetric[][] keyMetric) {
		System.out.println("Adding new profile");
		entityManager.getTransaction().begin();	
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				entityManager.persist(keyMetric[i][j]);
			}
		}
		entityManager.getTransaction().commit();
	}
	
	public static User getUser(int userId) {
		User user = null;
		
		user = entityManager.find(User.class, userId);
		
		if(user == null) {
			System.out.println("No user with id: " + Integer.toString(userId));
		} else {
			System.out.println("Found user " + user.getAlias() + " and id " + Integer.toString(userId));
		}
		return user;
	}
	
	public static Profile getProfile(int profileId) {
		Profile profile = null;
		
		profile = entityManager.find(Profile.class, profileId);
		
		if(profile == null) {
			System.out.println("No user with id: " + Integer.toString(profileId));
		} else {
			System.out.println("Found user " + profile.getProfilename() + " and id " + Integer.toString(profileId));
		}
		return profile;
	}
	
}
