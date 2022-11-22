package predictivegui;

import javax.swing.SwingUtilities;

/**
 * Main class for GUI Dictionary
 * @author Hans Sean Nathanael
 *
 */
public class Main {

	// the main controller for the main view and dictionary model
	public static MainController mainController;
	
	// main view that contain all UI of the application
	public static MainView mainView;
	
	// dictionary model contain the dictionary in form of tree dictionary and text that will be showed in UI
	public static PredictiveDictionaryModel dictionary;
	
	// Runnable for EventDispatcherThread (thread for UI)
	public static Runnable UIThread = new Runnable() {
		
		@Override
		public void run() {
			mainController = new MainController();
			mainView = new MainView("Predictive Text", mainController);
			dictionary = new PredictiveDictionaryModel();
			
			// pair the view and the model
			mainController.pairViewAndModel(mainView, dictionary);
		}
	};
	
	public static void main(String[] args) {
		
		// EventDispatcherThread, used for UI safety (UI should inside this)
		SwingUtilities.invokeLater(UIThread);
	}

}
