package simonhanna.ense480.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userid;
	
	@Column(length=35, unique=true)
	private String alias;
	
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	private List<Mistake> mistakes; 
	
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	private List<Profile> profiles; 
	
	public User() {}
	
	public User(String alias) {
		this.alias = alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public void setMistakes(List<Mistake> mistakes) {
		this.mistakes = mistakes;
	}
	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}
	
	public String getAlias() {
		return alias;
	}
	public List<Mistake> getMistakes() {
		return mistakes;
	}
	public List<Profile> getProfiles() {
		return profiles;
	}
	public int getUserid() {
		return userid;
	}	
	
	public Profile getProfileByName(String name) {
		for(int i=0; i<this.profiles.size(); i++) {
			if(this.profiles.get(i).getProfilename().equals(name))
				return this.profiles.get(i);
		}
		return null;
	}
	
	public String toString() {
		return this.getAlias();
	}
	
}
