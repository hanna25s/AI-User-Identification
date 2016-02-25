package simonhanna.ense480;

public class Node {

	private long time = 0;
	private int numberOfOccurences = 0;
	
	public void addOccurence(long time) {
		this.numberOfOccurences++;
		this.time += time;
	}
	
	Node(long time) {
		setTime(time);
		setNumberOfOccurences(1);
	}
	
	Node() {};
	
	public void setNumberOfOccurences(int numberOfOccurences) {
		this.numberOfOccurences = numberOfOccurences;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public long getTime() {
		return this.time;
	}
	
	public int getNumberOfOccurences() {
		return this.numberOfOccurences;
	}
}
