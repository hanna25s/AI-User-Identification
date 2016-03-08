package simonhanna.ense480.controllers;

import javafx.scene.input.KeyEvent;
import simonhanna.ense480.models.KeyMetric;
import simonhanna.ense480.models.Profile;

public abstract class KeyMetricController {

	protected static KeyMetric[][] detectUserKeyMetrics = new KeyMetric[10][10];
	protected static KeyMetric[][] alterProfileKeyMetrics = new KeyMetric[10][10];
	protected static Profile profile;
	
	protected int newKey, oldKey;
	protected double startTime, endTime, timeBetweenKeys;
	
	public void addKeyMetric(KeyEvent k, KeyMetric[][] keyMetrics) {
		System.out.println("Adding key metric");
		endTime = (double)System.nanoTime()/1000000000.0;
    	newKey = getKeyGroup(k);
    	System.out.println(newKey);
    	timeBetweenKeys = (endTime - startTime);
    	
    	//In case the user leaves and comes back. Avoid incredibly large values as they lead to errors.
    	if(timeBetweenKeys > 10.0) {
    		startTime = endTime;
            oldKey = newKey;
    		return;
    	}
    	
    	KeyMetric keyMetric = keyMetrics[oldKey][newKey];
    	
    	keyMetric.setTime(keyMetric.getTime() + timeBetweenKeys);
    	keyMetric.setNumberOfOccurences(keyMetric.getNumberOfOccurences() + 1);

        startTime = endTime;
        oldKey = newKey;
	}
	
	public void initKeyMetrics(KeyMetric[][] keyMetrics) {
		for(int i=0; i<10; i++) {
    		for(int j=0; j<10; j++) {
    			keyMetrics[i][j] = new KeyMetric(i, j);
    		}
    	}
	};
	
	public void initKeyMetrics(KeyMetric[][] keyMetrics, Profile profile) {
		for(int i=0; i<10; i++) {
    		for(int j=0; j<10; j++) {
    			keyMetrics[i][j] = new KeyMetric(i, j, profile);
    		}
    	}
	};
	
	/**
	 * getKeyGroup
	 * Input: Key entered by user
	 * Returns: Group input key belongs to
	 * Description: This is a helped method that is used
	 *      to get the key group for each key entered.
	 **/
	public int getKeyGroup(KeyEvent key) {
		
		if(key.getCharacter().length() == 0)
			return 9;
		
		char input = Character.toUpperCase(key.getCharacter().charAt(0));
		
		switch(input) {
		case 'Q':
		case 'A':
		case 'Z':
			return 0;
		case 'W':
		case 'S':
		case 'X':
			return 1;
		case 'C':
		case 'D':
		case 'E':
			return 2;
		case 'R':
		case 'T':
		case 'F':
		case 'G':
		case 'V':
		case 'B':
			return 3;
		case 'Y':
		case 'U':
		case 'H':
		case 'J':
		case 'N':
		case 'M':
			return 4;
		case 'I':
		case 'K':
			return 5;
		case 'O':
		case 'L':
			return 6;
		case 'P':
			return 7;
		case ' ':
			return 8;
		default:
			return 9;
		}
	}	
}
