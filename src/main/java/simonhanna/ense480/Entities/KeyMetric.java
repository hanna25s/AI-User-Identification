package simonhanna.ense480.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "key_metric")
public class KeyMetric {
	
	@Id
	@GeneratedValue
	private int keymetricid;
	
	@Column(name="startkey")
	private int startKey;
	@Column(name="endkey")
	private int endKey;
	
	private double time;
	private int numberOfOccurences;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="profileid")
	private Profile profile;
	
	public KeyMetric() {
		this.numberOfOccurences = 0;
	}
	
	public KeyMetric(int from, int to, Profile profile) {
		this.startKey = from;
		this.endKey = to;
		this.profile = profile;
		this.numberOfOccurences = 0;
	}
	
	public void setFrom(int from) {
		this.startKey = from;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public void setTo(int to) {
		this.endKey = to;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public void setNumberOfOccurences(int numberOfOccurences) {
		this.numberOfOccurences = numberOfOccurences;
	}
	
	public int getFrom() {
		return startKey;
	}
	public int getKeymetricid() {
		return keymetricid;
	}
	public double getTime() {
		return time;
	}
	public int getTo() {
		return endKey;
	}
	public Profile getProfile() {
		return profile;
	}
	public int getProfileid() {
		return profile.getProfileid();
	}
	public int getNumberOfOccurences() {
		return numberOfOccurences;
	}
	
	public void writeContents() {
		System.out.println("Writing KeyMetric contents...");
		System.out.println("Profile Id: " + this.getProfileid());
		System.out.println("From: " + this.startKey);
		System.out.println("To: " + this.endKey);
		System.out.println("Number of Occurences: " + this.numberOfOccurences);
		System.out.println("Time: " + this.time);
	}
}
