package simonhanna.ense480.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue
	private int userid;
	
	@Column(length=35, unique=false)
	private String alias;
	
	@OneToMany(mappedBy="user")
	private List<Mistake> mistakes; 
	
	@OneToMany(mappedBy="user")
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
}
