package simonhanna.ense480.Entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table( name = "profile" )
public class Profile {

	@Id
	@GeneratedValue
	private int profileid;
	
	@Column(length=35)
	private String profilename;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userid")
	private User user;
	
	@OneToMany(mappedBy="profile")
	private List<KeyMetric> keyMetrics; 
	
	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setKeyMetrics(List<KeyMetric> keyMetrics) {
		this.keyMetrics = keyMetrics;
	}
	
	public String getProfilename() {
		return profilename;
	}
	public User getUser() {
		return user;
	}
	public int getUserid() {
		return user.getUserid();
	}
	public int getProfileid() {
		return profileid;
	}
	public List<KeyMetric> getKeyMetrics() {
		return keyMetrics;
	}
}
