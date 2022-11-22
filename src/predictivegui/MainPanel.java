package predictivegui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * Class for main panel that contain all the main UI
 * @author Hans Sean Nathanael
 *
 */
public class MainPanel extends JPanel {
	
	// commandButton is the action command for all button in this UI
	// this is created for easier change and maintaining the application
	// and to not make long nested if
	private String commandButton[] = {"1", "2", "3",
									  "4", "5", "6",
									  "7", "8", "9",
									  "*", "0", "#"};
	
	// buttonDescription is the text for each button
	// written in HTML syntax for text alignment and newline
	private String buttonDescription[] = {"1<br>", "2<br>abc", "3<br>def",
			  							  "4<br>ghi", "5<br>jkl", "6<br>mno",
			  							  "7<br>pqrs", "8<br>tuv", "9<br>wxyz",
			  							  "*<br>", "0<br>_", "#<br>"};
	
	// default text that will be showed in text area when there's no text
	private String defaultUserTextFieldText = "predictive text: enter text with 8 keys.";
	
	// ID for serialization
	private static final long serialVersionUID = 6250522730089729797L;
	
	// text label used to show the written text
	private JTextArea userTextField = new JTextArea();
	
	// scroll pane for the userTextField to allow scroll bar when userTextField is full
	private JScrollPane scrollPaneForUserTextField = new JScrollPane(this.userTextField);
	
	// array of button for all button in this UI
	// the action command for each button is from commandButton with the same index
	// and the text from buttonDescription with the same index
	private JButton buttonNumber[] = new JButton[12];
	
	/**
	 * Constructor of this class to make the UI panel and connecting all button to the listener
	 * @param actionListenerForButton : listener for every buttonNumber
	 */
	public MainPanel(ActionListener actionListenerForButton)
	{
		// layout using GridBagLayout
		// the text area will have half of the top of frame
		// the second half (bottom half) for the buttons
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraint = new GridBagConstraints();
		this.setLayout(gridBagLayout);
		
		// UI will stretched horizontal and vertical to fill the frame
		gridBagConstraint.fill = GridBagConstraints.BOTH;
		gridBagConstraint.anchor = GridBagConstraints.NORTHWEST;

		// setting for userTextField
		this.userTextField.setLineWrap(true); // wrap the line to fit with the width
		this.userTextField.setEditable(false); // user must can't type the text
		this.userTextField.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));
		this.userTextField.setText(defaultUserTextFieldText);

		// disable horizontal scroll for userTextField
		this.scrollPaneForUserTextField.setHorizontalScrollBar(null);

		// the scrollPaneForUserTextField will take half top of the frame
		// supposedly, but in reality it's not, but the UI still works good
		gridBagConstraint.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraint.weightx = 1;
		gridBagConstraint.weighty = 0.5;
		
		this.addComponentToGridBagLayout(scrollPaneForUserTextField, gridBagLayout, gridBagConstraint);

		// buttons will take the second half of frame (bottom half), supposedly... 
		gridBagConstraint.weightx = 0.5 / 3;
		gridBagConstraint.weighty = 0.5 / 4;
		gridBagConstraint.gridwidth = 1;
		gridBagConstraint.gridheight = 1;
		
		// setting all buttons (text, UI style, and listener)
		for (int i = 0; i < 12; i++)
		{
			gridBagConstraint.gridy = 1 + i / 3; // the first row for the scrollPaneForUserTextField
			
			buttonNumber[i] = new JButton();
			buttonNumber[i].setFont(new Font("Arial", Font.PLAIN, 16));
			buttonNumber[i].setHorizontalTextPosition(SwingConstants.CENTER);
			buttonNumber[i].setText("<html><p style=\"text-align:center\">" + buttonDescription[i] + "</p></html>");
			
			// plain style button
			buttonNumber[i].setFocusPainted(false);
			buttonNumber[i].setContentAreaFilled(false);

			buttonNumber[i].setActionCommand(commandButton[i]);
			buttonNumber[i].addActionListener(actionListenerForButton);
			
			this.addComponentToGridBagLayout(buttonNumber[i], gridBagLayout, gridBagConstraint);
		}
	}
	
	/**
	 * Helper method to add component to GridBagLayout with a constraint
	 * @param c : component to be added to the GridBagLayout
	 * @param layout : layout of the view
	 * @param constraint : constraint for the component in the layout
	 */
	private void addComponentToGridBagLayout(Component c, GridBagLayout layout, GridBagConstraints constraint)
	{
		layout.setConstraints(c, constraint);
		this.add(c);
	}
	
	/**
	 * Method to change text in userTextField
	 * @param words : words to be showed in userTextField
	 */
	public void setUserTextFieldWordsTo(String words)
	{
		if (words.length() == 0)
		{
			// the text is empty, so userTextField will show default text
			this.userTextField.setText(defaultUserTextFieldText);
		}
		else
		{
			this.userTextField.setText(words);	
		}
	}
}
