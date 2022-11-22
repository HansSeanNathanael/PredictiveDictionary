package predictivegui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import predictive.Dictionary;
import predictive.DictionaryTreeImpl;

/**
 * Class for dictionary model and backbone data for application
 * This class extends Observable
 * @author Hans Sean Nathanael
 *
 */
public class PredictiveDictionaryModel extends Observable {
	
	// string signature for word that still been written by user and still not entered (or finished)
	private StringBuffer signature = new StringBuffer();
	
	// dictionary class (Tree Dictionary) that store all words from dictionary file
	private Dictionary dictionary = new DictionaryTreeImpl("assets/words");
	
	// result of words from search in dictionary by prefix signature
	// it is better for resultOfDictionary implement TreeSet
	// HashSet is faster but no ordering, meanwhile TreeSet could give better
	// experience when changing the word, because the change will ordered by lexicographic
	// this will slow the a little bit but the difference is not significant
	private Set<String> resultOfDictionary = new TreeSet<String>();
	
	// list of words that have been written (finished) and entered by user
	private List<String> listWords = new ArrayList<String>();
	
	// iterator to iterate resultOfDictionary to get words in index currentIndex
	private Iterator<String> wordGetterIterator = null;
	
	// word that still being typed by the user (still not finished and entered)
	private String beingTypedWord = null;
	
	/**
	 * Constructor of this class (empty constructor)
	 */
	public PredictiveDictionaryModel() {}
	
	/**
	 * Method to add signature from user input
	 * @param signature : char signature that have been inputed by user
	 */
	public void addSignature(char signature)
	{
		// add signature, then do search with the new signature and reset the iterator
		// to get the first word found in search result
		// resultOfDictionary convert dictionary search result HashSet to TreeSet for better
		// user experience, this will slow a little bit but not very significant
		this.signature.append(signature);
		this.resultOfDictionary.clear();
		this.resultOfDictionary.addAll(this.dictionary.signatureToWords(this.signature.toString()));
		this.wordGetterIterator = this.resultOfDictionary.iterator();
		
		// change the words of the still typed words
		this.changeWords();
	}
	
	/**
	 * Method to do backspace for signature
	 */
	public void backspaceSignature()
	{
		if (this.signature.length() == 0)
		{
			if (this.listWords.size() > 0)
			{
				// if the signature length is 0, that means going back to the previous words
				// and the listWords size must more than zero (>0) to make the going back to previous words
				// if not, this will throw exception
				
				// set the signature to the previous word signature and remove the word from
				// entered words list
				this.signature.append(dictionary.wordToSignature(this.listWords.get(this.listWords.size() - 1)));
				this.listWords.remove(this.listWords.size() - 1);
				
				// resultOfDictionary convert dictionary search result HashSet to TreeSet for better
				// user experience, this will slow a little bit but not very significant
				this.resultOfDictionary.clear();
				this.resultOfDictionary.addAll(this.dictionary.signatureToWords(this.signature.toString()));
				this.wordGetterIterator = this.resultOfDictionary.iterator();
			}
			
			// no need to do anything when no words have been written
		}
		else
		{
			// if doing backspace after input a character on being-typed-word-by-user
			// it just need to delete the last signature and update the result from dictionary
			// and reset the iterator for new words
			this.signature.deleteCharAt(this.signature.length()-1);
			this.resultOfDictionary.clear();
			this.resultOfDictionary.addAll(this.dictionary.signatureToWords(this.signature.toString()));
			this.wordGetterIterator = this.resultOfDictionary.iterator();
		}
		
		this.changeWords();
	}
	
	/**
	 * Method to change the word to the next word with the same prefix signature
	 */
	public void changeWords()
	{
		if (this.resultOfDictionary.size() > 0)
		{
			// must check if resultOfDictionary size is more than 0
			// because if the size is 0, the word can't be changed
			
			// return to the first element if iterator at last element
			if (this.wordGetterIterator.hasNext() == false)
			{
				this.wordGetterIterator = this.resultOfDictionary.iterator();
			}
			this.beingTypedWord = this.wordGetterIterator.next(); // get next word
		}
		else
		{
			// iterator and typed word will be null to tell the result is empty
			this.wordGetterIterator = null;
			this.beingTypedWord = null;
		}
		
		// change the text showed in UI
		this.getText();
	}
	
	/**
	 * Method to finish and enter the written word by user to the list of entered word
	 */
	public void enterUserInputWord()
	{
		if (this.resultOfDictionary.size() > 0 && this.beingTypedWord != null)
		{
			// if resultOfDictionary is empty, no need to enter the new word because nothing
			// will be entered into the list
			
			// add the being-typed-word to the list of written word
			this.listWords.add(this.beingTypedWord);
		}
		
		// reset the signature, resultOfDictionary, and beingTypedWord to become ready for next text
		this.signature.delete(0, signature.length());
		this.resultOfDictionary.clear();
		this.wordGetterIterator = null;
		this.beingTypedWord = null;
					
		this.getText();
	}
	
	/**
	 * Method to notify the observer some value already changed and the text for UI
	 * ready to showed by sending it to observer
	 */
	private void getText()
	{
		// StringBuffer to make text that will be showed in the UI
		StringBuffer text = new StringBuffer();
		
		// inserting all the entered word
		for(String element : this.listWords)
		{
			text.append(element);
			text.append(" ");
		}
		
		// taking the being-typed-word-by-user if user already write something
		if (this.signature.length() > 0 && this.beingTypedWord != null)
		{
			text.append(this.beingTypedWord);
		}
		
		setChanged(); // don't forget this, if forgotten the observer will not update the UI
		notifyObservers(text.toString()); // send text that will be shown in UI to the observer (controller)
	}
}
