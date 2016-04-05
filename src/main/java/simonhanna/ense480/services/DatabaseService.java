package simonhanna.ense480.services;

import java.util.List;

import javax.persistence.*;

import simonhanna.ense480.models.*;

public final class DatabaseService {
	
	private static final String UNIT_NAME = "simonhanna.ense480.aiproject";
	private static EntityManager entityManager = Persistence.createEntityManagerFactory(UNIT_NAME).createEntityManager();
	
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
		entityManager.refresh(user);
	}
	
	public static List<User> getUserAliases() {
		return entityManager.createQuery("SELECT u FROM User u").getResultList();
	}
	
	/**
	 * Updates an existing set of key metrics with the new set of key metrics
	 * that are passed in
	 * 
	 * @param profile    Profile the key metrics belong to
	 * @param keyMetric  Key metrics we want to add the the existing metrics
	 */
	public static void updateKeyMetrics(Profile profile, KeyMetric[][] keyMetric) {
		
		if(profile.getKeyMetrics() == null || profile.getKeyMetrics().size() == 0) {
			System.out.println("No metrics existing, creating metrics");
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
		entityManager.refresh(profile);
	}
	
	/**
	 * When no previous key metrics exist for a profile, this method is used to initiate the set of metrics
	 * for a profile
	 * 
	 * @param profile    Profile the key metrics belong to
	 * @param keyMetric  Key metrics we want to add the the existing metrics
	 */
	public static void addKeyMetrics(Profile profile, KeyMetric[][] keyMetric) {
		entityManager.getTransaction().begin();	
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				entityManager.persist(keyMetric[i][j]);
			}
		}
		entityManager.getTransaction().commit();
		entityManager.refresh(profile);
	}
	
	public static User getUserFromId(int userId) {
		return entityManager.find(User.class, userId);
	}
	
	public static List<Profile> getProfiles() {
		return entityManager.createQuery("SELECT p FROM Profile p").getResultList();
	}
	
	public static Profile getProfileFromId(int profileId) {	
		return entityManager.find(Profile.class, profileId);
	}
	
	public static List<Profile> getOtherProfiles(Profile profile) {
		return entityManager.createQuery("Select p FROM Profile p WHERE p.profileid != '" + profile.getProfileid() + "'").getResultList();
	}
}
