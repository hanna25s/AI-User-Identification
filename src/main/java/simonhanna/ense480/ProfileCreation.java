package simonhanna.ense480;
import simonhanna.ense480.DatabaseAccess;

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

public class ProfileCreation extends JFrame
	implements KeyListener, ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	public static Node[][] profile = new Node[10][10];
	public char newKey;
	public char oldKey;
	public long startTime, endTime, timeBetweenKeys;
	
	JTextArea displayArea;
    JTextField typingArea;
    static final String newline = System.getProperty("line.separator");
    
    public static void main(String[] args) {
    	
    	for(int i=0; i<10; i++) {
    		for(int j=0; j<10; j++) {
    			profile[i][j] = new Node();
    		}
    	}
    	
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
        ProfileCreation frame = new ProfileCreation("ProfileCreation");
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
    
    public ProfileCreation(String name) {
        super(name);
        this.startTime = System.nanoTime();
    }
    
    
    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
    }
    
    /** Handle the key pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
    	newKey = e.getKeyChar();
    	endTime = System.nanoTime();
    	timeBetweenKeys = (endTime - startTime);
    	
    	System.out.println("Old Key Group: " + getKeyGroup(oldKey));
    	System.out.println("New Key Group: " + getKeyGroup(newKey));
    	
    	profile[getKeyGroup(oldKey)][getKeyGroup(newKey)].addOccurence(timeBetweenKeys);
    	
    	startTime = endTime;
        displayInfo(e, "KEY PRESSED: ");
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
    			+ "Time Between Keys: " + Long.toString(timeBetweenKeys) + newline;
   
        displayArea.append(message);
        displayArea.setCaretPosition(displayArea.getDocument().getLength());
    }
    
    private String displayProfile() {
    	String response = "";
    	
    	for(int i=0; i<10; i++) {
    		response += "Row " + Integer.toString(i) + ":";
    		for(int j=0; j<10; j++) {
    			if(profile[i][j].getNumberOfOccurences() == 0) {
    				response += " 0000000000 ";
    			} else {
    				response += " " + Long.toString(profile[i][j].getTime()/profile[i][j].getNumberOfOccurences()) + " ";
    			}
    		}
    		response += newline;
    	}
    	
    	return response;
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
