package predictivegui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Class for main controller for the MainView (UI) and the PredictiveDictionaryModel
 * The class implement observer for the PredictiveDictionaryModel and ActionListener for the UI
 * @author Hans Sean Nathanael
 *
 */
public class MainController implements Observer, ActionListener {

	// main view or main UI
	private MainView mainView;
	
	// dictionary model contain tree dictionary and backbone of the program data
	private PredictiveDictionaryModel dictionary;
	
	/**
	 * Constructor for the class (empty constructor)
	 */
	public MainController() {}
	
	/**
	 * Function to pair MainView and PredictiveDictionaryModel to the MainController
	 * @param view : main view (main UI) that will be connected
	 * @param dictionary : PredictiveDictionaryModel that will be connected
	 */
	public void pairViewAndModel(MainView view, PredictiveDictionaryModel dictionary)
	{
		this.mainView = view;
		this.dictionary = dictionary;
		
		// this class become observer of the dictionary
		this.dictionary.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof PredictiveDictionaryModel)
		{
			// PredictiveDictionaryModel just have one method that call notifyObservers
			// and the argument was the text need to showed in UI
			this.mainView.setUserTextFieldTo(arg.toString());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		// button command from 0-9, *, and #
		if (command.compareTo("0") == 0)
		{
			this.dictionary.enterUserInputWord();
		}
		else if (command.compareTo("2") == 0)
		{
			this.dictionary.addSignature('2');
		}
		else if (command.compareTo("3") == 0)
		{
			this.dictionary.addSignature('3');
		}
		else if (command.compareTo("4") == 0)
		{
			this.dictionary.addSignature('4');
		}
		else if (command.compareTo("5") == 0)
		{
			this.dictionary.addSignature('5');
		}
		else if (command.compareTo("6") == 0)
		{
			this.dictionary.addSignature('6');
		}
		else if (command.compareTo("7") == 0)
		{
			this.dictionary.addSignature('7');
		}
		else if (command.compareTo("8") == 0)
		{
			this.dictionary.addSignature('8');
		}
		else if (command.compareTo("9") == 0)
		{
			this.dictionary.addSignature('9');
		}
		else if (command.compareTo("*") == 0)
		{
			this.dictionary.changeWords();
		}
		else if (command.compareTo("#") == 0)
		{
			this.dictionary.backspaceSignature();
		}
	}
}
