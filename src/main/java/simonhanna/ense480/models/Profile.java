package simonhanna.ense480.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "profile")
public class Profile {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int profileid;
	
	@Column(length=35)
	private String profilename;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userid")
	private User user;
	
	@OneToMany(mappedBy="profile")
	private List<KeyMetric> keyMetrics = new ArrayList<KeyMetric>();
	
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
	public void setKeyMetrics(KeyMetric[][] profileMetrics) {
		
		if(keyMetrics.size() == 0) {
			System.out.println("Adding new metrics");
			
			for(int i=0; i<10; i++) {
				for(int j=0; j<10; j++) {
					keyMetrics.add(profileMetrics[i][j]);
				}
			}
		} else {
			updateKeyMetrics(profileMetrics);
			return;
		}
	}
	
	public void updateKeyMetrics(KeyMetric[][] profileMetrics) {
		//TODO add a for each to update entries as needed
		System.out.println("Would be updating metrics");
	}
	
	public void displayKeyMetrics() {
		
	}
	
}
