package simonhanna.ense480.activities;
import simonhanna.ense480.activities.DatabaseAccess;
import simonhanna.ense480.entities.*;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.structure.NeuralStructure;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ProfileActivity extends JFrame
	implements KeyListener, ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	
	BasicNetwork network = new BasicNetwork();
	NeuralStructure ns = new NeuralStructure(network);
	
	private static KeyMetric[][] keyMetrics = new KeyMetric[10][10];
	private static User user;
	private static Profile profile;
	
	private char newKey;
	private char oldKey;
	private double startTime, endTime, timeBetweenKeys;
	
	
	JTextArea displayArea;
    JTextField typingArea;
    static final String newline = System.getProperty("line.separator");
    
    public static void main(String[] args) {
    	
    	user = DatabaseAccess.getUser(1);
    	profile = DatabaseAccess.getProfile(4);
    	
    	initKeyMetrics();
    	
    	NeuralNetwork.createNeuralNetwork(profile.getKeyMetrics(), profile);
    	
        /* Use an appropriate Look and Feel */
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        ProfileActivity frame = new ProfileActivity("ProfileCreation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Set up the content pane.
        frame.addComponentsToPane();
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    private void addComponentsToPane() {
        
        JButton button = new JButton("Show Profile");
        button.addActionListener(this);
        
        typingArea = new JTextField(20);
        typingArea.addKeyListener(this);
        
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(375, 125));
        
        getContentPane().add(typingArea, BorderLayout.PAGE_START);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(button, BorderLayout.PAGE_END);
    }
    
    public ProfileActivity(String name) {
        super(name);
        this.startTime = (double)System.nanoTime()/1000000000.0;
    }
    
    
    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
    }
    
    /** Handle the key pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
    	endTime = (double)System.nanoTime()/1000000000.0;
    	newKey = e.getKeyChar();
    	timeBetweenKeys = (endTime - startTime);
    	    	
    	KeyMetric keyMetric = keyMetrics[getKeyGroup(oldKey)][getKeyGroup(newKey)];
    	
    	keyMetric.setTime(keyMetric.getTime() + timeBetweenKeys);
    	keyMetric.setNumberOfOccurences(keyMetric.getNumberOfOccurences() + 1);

        displayInfo(e, "KEY PRESSED: ");
        startTime = endTime;
        oldKey = newKey;
    }
   

	/** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e) {
    	
    }
    
    /** Handle the button click. */
    public void actionPerformed(ActionEvent e) {
        String profile = displayProfile();
        displayArea.setText(profile);
    }
   
    private void displayInfo(KeyEvent e, String keyStatus){
        
    	int start = getKeyGroup(oldKey);
    	int end = getKeyGroup(newKey);
    	
    	String message = newline + "From " + start + " to " + end + newline
    			+ "oldKey: " + oldKey + newline + "newKey: " + newKey + newline
    			+ "Time Between Keys: " + Double.toString(timeBetweenKeys) + newline;
   
        displayArea.append(message);
        displayArea.setCaretPosition(displayArea.getDocument().getLength());
    }
    
    private String displayProfile() {
    	String response = "";
    	KeyMetric keyMetric = null;
    	
    	DatabaseAccess.updateKeyMetrics(profile, keyMetrics);
    	
    	for(int i=0; i<10; i++) {
    		response += "Row " + Integer.toString(i) + ":";
    		for(int j=0; j<10; j++) {
    			keyMetric = keyMetrics[i][j];
    			if(keyMetric.getNumberOfOccurences() == 0) {
    				response += " 0 ";
    			} else {
    				response += " " + Double.toString(keyMetric.getTime()/keyMetric.getNumberOfOccurences()) + " ";
    			}
    		}
    		response += newline;
    	}	
    	initKeyMetrics();
    	return response;    	
    }
    
    public static void initKeyMetrics() {
    	for(int i=0; i<10; i++) {
    		for(int j=0; j<10; j++) {
    			keyMetrics[i][j] = new KeyMetric(i, j, profile);
    		}
    	}
    }
    
	/**
	 * getKeyGroup
	 * Input: Key entered by user
	 * Returns: Group input key belongs to
	 * Description: This is a helped method that is used
	 *      to get the key group for each key entered.
	 **/
	public int getKeyGroup(char key) {
		
		key = Character.toUpperCase(key);
		
		switch(key) {
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
