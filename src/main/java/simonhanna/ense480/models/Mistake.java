package simonhanna.ense480.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "mistake" )
public class Mistake {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int mistakeid;
	
	private int keyhit;
	private int intendedkey;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userid")
	private User user;
	
	public void setIntendedkey(int intendedkey) {
		this.intendedkey = intendedkey;
	}
	public void setKeyhit(int keyhit) {
		this.keyhit = keyhit;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public int getIntendedkey() {
		return intendedkey;
	}	
	public int getKeyhit() {
		return keyhit;
	}
	public int getMistakeid() {
		return mistakeid;
	}
	public int getUserid() {
		return user.getUserid();
	}
	public User getUser() {
		return user;
	}
}