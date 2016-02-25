package simonhanna.ense480.Entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "key_metric" )
public class KeyMetric {
	
	@Id
	@GeneratedValue
	private int keymetricid;
	
	private int from;
	private int to;
	private long time;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="profileid")
	private Profile profile;
	
	public void setFrom(int from) {
		this.from = from;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	public int getFrom() {
		return from;
	}
	public int getKeymetricid() {
		return keymetricid;
	}
	public long getTime() {
		return time;
	}
	public int getTo() {
		return to;
	}
	public Profile getProfile() {
		return profile;
	}
	public int getProfileid() {
		return profile.getProfileid();
	}
}
