package predictivegui;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 * Class for the main UI extends the JFrame
 * @author Hans Sean Nathanael
 *
 */
public class MainView extends JFrame {

	// ID for serialization
	private static final long serialVersionUID = -7721035791590742902L;

	// main panel that contain all the UI
	private MainPanel mainPanel;
	
	/**
	 * Constructor of the class, create frame with a title and ActionListener for all button in MainPanel
	 * @param title : name of the application
	 * @param actionListenerForButton : ActionListener for all button in MainPanel
	 */
	public MainView(String title, ActionListener actionListenerForButton)
	{
		super(title);

		// don't forget to write this, if not the application will make memory leak because
		// the application will not be closed when press x in top
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// minimum size will be 360pixel x 480 pixel (width x height)
		// ration 3 : 4
		this.setMinimumSize(new Dimension(360, 480));
		this.setVisible(true);
		
		// change the contentPane to main UI panel
		this.mainPanel = new MainPanel(actionListenerForButton);
		this.setContentPane(this.mainPanel);
	}
	
	/**
	 * Function to change main panel text area text that used to show the written text
	 * that have been written by the user
	 * @param words : words to be showed in text area UI
	 */
	public void setUserTextFieldTo(String words)
	{
		this.mainPanel.setUserTextFieldWordsTo(words);
	}
}
