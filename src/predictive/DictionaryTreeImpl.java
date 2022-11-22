package predictive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to store all words in dictionary using Tree (or Trie?)
 * Tree's node have 8 child for representing signature range (2-9)
 * Could do search up of words by prefix of it's signature
 * @author Hans Sean Nathanael
 *
 */
public class DictionaryTreeImpl implements Dictionary {

	// this array signatureOfChar used to simplify function wordToSignature
	// The length is 26 representing all the alphabet a-z
	// index 0 is for 'a', index 1 for 'b', and so on until last index (index 25) for 'z'
	private char signatureOfChar[] = {'2', '2', '2',
									  '3', '3', '3',
									  '4', '4', '4',
									  '5', '5', '5',
									  '6', '6', '6',
									  '7', '7', '7', '7',
									  '8', '8', '8',
									  '9', '9', '9', '9'};

	// setDictionaryWords used to store all words that have the match signature
	private Set<String> setDictionaryWords = null;
	
	// array to store next node (or child node)
	// the size is 8 (node have 8 child) because representing signature of next character
	// index 0 is for signature 2, 1 for signature 3, and so on, last index (index 7) for signature 9
	// So, this array to represent signature of next character from signature 2 until 9 (8 signature)
	private DictionaryTreeImpl nextNode[] = new DictionaryTreeImpl[8];
	
	/**
	 * Constructor of the dictionary tree, this will create the full tree containing
	 * all valid words from dictionary file
	 * @param path : directory path to dictionary file, if null it will just make the tree node, if not null
	 * 		  the tree will be initialized
	 */
	public DictionaryTreeImpl(String path)
	{
		if (path != null)
		{
			try 
			{
				// fileReader is used to read all line of dictionary file
				// BufferedReader used because it is faster than Scanner when used to
				// read each line of file
				BufferedReader fileReader = new BufferedReader(new FileReader(path));
				
				// string that used to store word that have been read from file
				String fileOneLine = null;
				
				while((fileOneLine = fileReader.readLine()) != null)
				{
					// changed to lower case for easier comparison and because
					// all words need to stored in lower case
					fileOneLine = fileOneLine.toLowerCase();
					
					// must check is the word not contain non alphabet, because 
					// words that contain non alphabet doesn't need to be stored
					// and can't be retrieved even if stored
					if (isValidWord(fileOneLine) && fileOneLine.length() > 0)
					{
						// store the word in tree
						this.insertNext((short)-1, fileOneLine);
					}
				}
				
				// Don't forget to close the BufferedReader
				fileReader.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Recursive method to insert node to the tree, the tree algorithm are different than
	 * binary search tree or normal tree, because this tree will make all node until the
	 * the required location of the leaf, so this tree could make 10 node to reach the required
	 * location of leaf
	 * @param depth : index of word-character's signature
	 * @param words : words to be inserted to the tree dictionary (all character must lower case)
	 */
	private void insertNext(short depth, String words)
	{
		if (depth == words.length() - 1)
		{
			// this node represent the last signature of the word
			// because this is the last index, the word stored in this setDictionaryWords

			// initialize setDictionaryWords if not yet initialized
			// don't forget, this could lead to exception
			if (this.setDictionaryWords == null)
			{
				// HashSet used because it is faster for lookup than TreeSet (O(log n) because self balancing tree) 
				// and LinkedHashSet (similar to HashSet but use LinkedList for collision)
				// HashSet use hashing and collision stored in self balancing tree
				this.setDictionaryWords = new HashSet<String>();
			}
			
			setDictionaryWords.add(words);
		}
		else if (this.nextNode[signatureOfChar[words.charAt(depth + 1)-'a'] -'2'] == null)
		{
			// the if condition may be really hard to understand, here's explanation
			// it's basically check is the next node that represent next signature still null (not exist)
			// this.depth + 1 represent next character index
			// it takes character at this.depth + 1 index and subtracted by 'a' 
			// to become index for signatureOfChar (this actually convert char into signature)
			// then the char of index subtracted by '2' to get index for nextNode
			// because nextNode first index to represent signature '2'
			 
			// because it is null, then it must be created first and then walk again to
			// the required location of the leaf
			this.nextNode[signatureOfChar[words.charAt(depth + 1)-'a'] -'2'] = new DictionaryTreeImpl(null);
			this.nextNode[signatureOfChar[words.charAt(depth + 1)-'a'] -'2'].insertNext((short)(depth + 1), words);
		}
		else
		{
			// because next node already exist, it just need to call this recursive again
			this.nextNode[signatureOfChar[words.charAt(depth + 1)-'a'] -'2'].insertNext((short)(depth + 1), words);
		}
	}
	
	/**
	 * Method to get words with a certain signature that exist in dictionary file
	 * this function do search by the prefix of a signature of a word and trimmed to
	 * the length of the signature
	 * @param signature : prefix signature that represent prefix signature of the word
	 * @param length : length of signature
	 * @return Set (in form of HashSet) of String that contain all word in dictionary that have same 
	 *         prefix signature and the word will be trimmed to length of signature
	 */
	private Set<String> getWordsFromSignature(String signature, short length)
	{
		if (signature.length() == 0)
		{
			// this means, this node is the node for last signature
			// then it just need to add all words that stored by the sub branch tree node
			
			// HashSet to store all words with the same prefix
			Set<String> allWordsWithSamePrefix = new HashSet<String>();
			if (this.setDictionaryWords != null)
			{
				// don't forget to add the current node words if it is exist
				for (String element : this.setDictionaryWords)
				{
					allWordsWithSamePrefix.add(element.substring(0, length));						
				}
			}
			
			// add all element that have the same prefix from next node
			for (DictionaryTreeImpl nextNodeElement : this.nextNode)
			{
				// don't forget to check is the next node exist, if not this could lead to exception
				if (nextNodeElement != null)
				{
					for (String nextNodeStringElement : nextNodeElement.getWordsFromSignature(signature, length))
					{
						// add all words with same prefix and the length trimmed to the length of signature
						allWordsWithSamePrefix.add(nextNodeStringElement.substring(0, length));							
					}
				}
			}
			return allWordsWithSamePrefix;
		}
		else if (this.nextNode[signature.charAt(0) - '2'] != null)
		{
			// this condition is to traversing to the desired tree node that represent the prefix signature
			// the first index signature is pop out to get the next signature character
			return this.nextNode[signature.charAt(0) - '2'].getWordsFromSignature(signature.substring(1), length);
		}
		
		// return empty HashSet if the signature doesn't exist in dictionary
		return new HashSet<String>();
	}
	
	@Override
	public String wordToSignature(String word)
	{
		// Convert a string of word to a signature (number)
		// "Hello" become "43556" not "4433555555666"
		// This algorithm use array signatureOfChar rather than 'nested if'
		// 'Nested if' make program longer, by this it will easier to read
		
		// signatureStringBuffer is used to make the signature of the word
		// StringBuffer is used to make signature string rather than using String
		// because it is faster to append a string especially appending many times using loop
		// when append a string using '+', the string converted to StringBuffer and then
		// append the StringBuffer, then convert it back to String, it is faster
		// by just using StringBuffer, no need convert it to String every time it is appended
		StringBuffer signatureStringBuffer = new StringBuffer("");
		
		// the word must not null to be converted
		if (word == null)
		{
			return word;
		}
		word = word.toLowerCase(); // to the word become lower case so it's easier to converted
		
		for(int i = 0; i < word.length(); i++)
		{
			if (word.charAt(i) >= 'a' && word.charAt(i) <= 'z')
			{
				signatureStringBuffer.append(signatureOfChar[word.charAt(i)- 'a' ]);
			}
			else
			{
				signatureStringBuffer.append(" "); // for non alphabet changed to " " (space)
			}
		}
		
		return signatureStringBuffer.toString();
	}
	
	@Override
	public Set<String> signatureToWords(String signature)
	{
		// all signature character must numeric with range from 2-9, if not then no need to convert it
		// because it can't be converted (1 doesn't used because not representing any alphabet) and no
		// word will be represented by that signature
		if (signature.length() > 0 && isNumericWord(signature))
		{
			return this.getWordsFromSignature(signature, (short)signature.length());
		}
		
		// return empty HashSet if the signature isn't valid (containing non numeric characters)
		// or the signature is empty (this must be done because if not, the empty signature will return
		// non empty set with just one element that is an empty string or "", not null, this happen
		// because this class search by prefix, not by fixed signature)
		return new HashSet<String>();
	}
	
	/**
	 * Method to check if a string just contain alphabet characters
	 * @param word : string to be checked
	 * @return true if the word just contain alphabet, false if it contain non alphabet
	 */
	private boolean isValidWord(String word)
	{
		for (int i = 0; i < word.length(); i++)
		{
			if (word.charAt(i) < 'a' || word.charAt(i) > 'z')
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method to check if a all string characters are numeric with range from 2 to 9
	 * @param word : string to be checked
	 * @return true if string just contain numeric, false if there's one or more non numeric or '1' in string
	 */
	private boolean isNumericWord(String word)
	{
		for (int i = 0; i < word.length(); i++)
		{
			if (word.charAt(i) < '2' || word.charAt(i) > '9')
			{
				return false;
			}
		}
		return true;
	}
}


// below is the other implementation
// The algorithm use inner class TreeNode for the node, rather than DictionaryTreeImpl act as the node
// I think it is better to be like this, rather than every node have method
// wordToSignature and signatureToWords, i think it's a waste of memory
// with inner Class TreeNode, every node doesn't need to implement Dictionary method
// and it is easier to maintain
// the constructor doesn't need parameter argument, it just need to remove constructor argument

//package predictive;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * This class is used to store all words in dictionary using Tree (or Trie?)
// * Tree's node have 8 child for representing signature range (2-9)
// * Could do search up of words by prefix of it's signature
// * @author Hans Sean Nathanael
// *
// */
//public class DictionaryTreeImpl implements Dictionary {
//
//	// this array signatureOfChar used to simplify function wordToSignature
//	// The length is 26 representing all the alphabet a-z
//	// index 0 is for 'a', index 1 for 'b', and so on until last index (index 25) for 'z'
//	private char signatureOfChar[] = {'2', '2', '2',
//									  '3', '3', '3',
//									  '4', '4', '4',
//									  '5', '5', '5',
//									  '6', '6', '6',
//									  '7', '7', '7', '7',
//									  '8', '8', '8',
//									  '9', '9', '9', '9'};
//
//	/**
//	 * Inner class for tree's node, the node have depth
//	 * depth is used to tell this node representing which index of signature of words characters
//	 * @author Hans Sean Nathanael
//	 *
//	 */
//	private class TreeNode {
//		
//		// setDictionaryWords used to store all words that have the match signature
//		Set<String> setDictionaryWords = null;
//		
//		// depth is used to tell this node representing which index of signature of words characters
//		// that means if the depth is 2 and there's signature '7954', this represent signature'5'
//		// -1 represent the root of the tree
//		short depth = -1; 
//		
//		// array to store next node (or child node)
//		// the size is 8 (node have 8 child) because representing signature of next character
//		// index 0 is for signature 2, 1 for signature 3, and so on, last index (index 7) for signature 9
//		// So, this array to represent signature of next character from signature 2 until 9 (8 signature)
//		TreeNode nextNode[] = new TreeNode[8];
//		
//		/**
//		 * Constructor of node with it's depth
//		 * Depth is important because it is tell which index of signature of words it's representing
//		 * @param depth : depth of node (-1 is root of node)
//		 */
//		TreeNode(short depth)
//		{
//			this.depth = depth;
//		}
//		
//		/**
//		 * Recursive method to insert node to the tree, the tree algorithm are different than
//		 * binary search tree or normal tree, because this tree will make all node until the
//		 * the required location of the leaf, so this tree could make 10 node to reach the required
//		 * location of leaf
//		 * @param words : words to be inserted to the tree dictionary (all character must lower case)
//		 * @return this root of sub tree branch (root if called by tree root)
//		 */
//		TreeNode insertNext(String words)
//		{
//			if (this.depth == words.length() - 1)
//			{
//				// this node represent the last signature of the word
//				// because this is the last index, the word stored in this setDictionaryWords
//
//				// initialize setDictionaryWords if not yet initialized
//				// don't forget, this could lead to exception
//				if (this.setDictionaryWords == null)
//				{
//					// HashSet used because it is faster for lookup than TreeSet (O(log n) because self balancing tree) 
//					// and LinkedHashSet (similar to HashSet but use LinkedList for collision)
//					// HashSet use hashing and collision stored in self balancing tree
//					this.setDictionaryWords = new HashSet<String>();
//				}
//				
//				setDictionaryWords.add(words);
//			}
//			else if (this.nextNode[signatureOfChar[words.charAt(this.depth + 1)-'a'] -'2'] == null)
//			{
//				// the if condition may be really hard to understand, here's explanation
//				// it's basically check is the next node that represent next signature still null (not exist)
//				// this.depth + 1 represent next character index
//				// it takes character at this.depth + 1 index and subtracted by 'a' 
//				// to become index for signatureOfChar (this actually convert char into signature)
//				// then the char of index subtracted by '2' to get index for nextNode
//				// because nextNode first index to represent signature '2'
//				 
//				// because it is null, then it must be created first and then walk again to
//				// the required location of the leaf
//				TreeNode newNode = new TreeNode((short)(this.depth + 1));
//				this.nextNode[signatureOfChar[words.charAt(this.depth + 1)-'a'] -'2'] = newNode.insertNext(words);
//			}
//			else
//			{
//				// because next node already exist, it just need to call this recursive again
//				
//				this.nextNode[signatureOfChar[words.charAt(this.depth + 1)-'a'] -'2'] = 
//						this.nextNode[signatureOfChar[words.charAt(this.depth + 1)-'a'] -'2'].insertNext(words);
//			}
//			
//			// return sub root of tree sub branch
//			return this;
//		}
//		
//		/**
//		 * Method to get words with a certain signature that exist in dictionary file
//		 * this function do search by the prefix of a signature of a word
//		 * @param signature : prefix signature that represent prefix signature of the word
//		 * @return Set (in form of HashSet) of String that contain all word in dictionary that have same 
//		 *         prefix signature and trimmed to the length of the signature (prefix signature)
//		 */
//		Set<String> getWordsFromSignature(String signature)
//		{
//			if (this.depth >= signature.length() -1)
//			{
//				// when this.depth == signature.length() - 1, the node is representing the signature
//				// then it just need to add all words that stored by the sub branch tree node
//				
//				// HashSet to store all words with the same prefix
//				Set<String> allWordsWithSamePrefix = new HashSet<String>();
//				if (this.setDictionaryWords != null)
//				{
//					// don't forget to add the current node words if it is exist
//					for (String element : this.setDictionaryWords)
//					{
//						allWordsWithSamePrefix.add(element.substring(0, signature.length()));						
//					}
//				}
//				
//				// add all element that have the same prefix from next node
//				for (TreeNode nextNodeElement : this.nextNode)
//				{
//					// don't forget to check is the next node exist, if not this could lead to exception
//					if (nextNodeElement != null)
//					{
//						for (String nextNodeStringElement : nextNodeElement.getWordsFromSignature(signature))
//						{
//							// add all words with same prefix and the length trimmed to the length of signature
//							allWordsWithSamePrefix.add(nextNodeStringElement.substring(0, signature.length()));							
//						}
//					}
//				}
//				return allWordsWithSamePrefix;
//			}
//			else if (this.nextNode[signature.charAt(this.depth + 1) - '2'] != null)
//			{
//				// this condition is to traversing to the desired tree node that represent the prefix signature
//				return this.nextNode[signature.charAt(this.depth + 1) - '2'].getWordsFromSignature(signature);
//			}
//			
//			// return empty HashSet if the signature doesn't exist in dictionary
//			return new HashSet<String>();
//		}
//	}
//	
//	// root of dictionary tree (it must contain depth -1)
//	private TreeNode rootOfTree = new TreeNode((short)-1);
//	
//	/**
//	 * Constructor of the dictionary tree, this will create the full tree containing
//	 * all valid words from dictionary file
//	 */
//	public DictionaryTreeImpl()
//	{
//		try 
//		{
//			// fileReader is used to read all line of dictionary file
//			// BufferedReader used because it is faster than Scanner when used to
//			// read each line of file
//			BufferedReader fileReader = new BufferedReader(new FileReader("assets/words"));
//			
//			// string that used to store word that have been read from file
//			String fileOneLine = null;
//			
//			while((fileOneLine = fileReader.readLine()) != null)
//			{
//				// changed to lower case for easier comparison and because
//				// all words need to stored in lower case
//				fileOneLine = fileOneLine.toLowerCase();
//				
//				// must check is the word not contain non alphabet, because 
//				// words that contain non alphabet doesn't need to be stored
//				// and can't be retrieved even if stored
//				if (isValidWord(fileOneLine))
//				{
//					// store the word in tree
//					rootOfTree = rootOfTree.insertNext(fileOneLine);
//				}
//			}
//			
//			// Don't forget to close the BufferedReader
//			fileReader.close();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
//	@Override
//	public String wordToSignature(String word)
//	{
//		// Convert a string of word to a signature (number)
//		// "Hello" become "43556" not "4433555555666"
//		// This algorithm use array signatureOfChar rather than 'nested if'
//		// 'Nested if' make program longer, by this it will easier to read
//		
//		// signatureStringBuffer is used to make the signature of the word
//		// StringBuffer is used to make signature string rather than using String
//		// because it is faster to append a string especially appending many times using loop
//		// when append a string using '+', the string converted to StringBuffer and then
//		// append the StringBuffer, then convert it back to String, it is faster
//		// by just using StringBuffer, no need convert it to String every time it is appended
//		StringBuffer signatureStringBuffer = new StringBuffer("");
//		
//		// the word must not null to be converted
//		if (word == null)
//		{
//			return word;
//		}
//		word = word.toLowerCase(); // to the word become lower case so it's easier to converted
//		
//		for(int i = 0; i < word.length(); i++)
//		{
//			if (word.charAt(i) >= 'a' && word.charAt(i) <= 'z')
//			{
//				signatureStringBuffer.append(signatureOfChar[word.charAt(i)- 'a' ]);
//			}
//			else
//			{
//				signatureStringBuffer.append(" "); // for non alphabet changed to " " (space)
//			}
//		}
//		
//		return signatureStringBuffer.toString();
//	}
//	
//	@Override
//	public Set<String> signatureToWords(String signature)
//	{
//		// all signature character must numeric with range from 2-9, if not then no need to convert it
//		// because it can't be converted (1 doesn't used because not representing any alphabet) and no
//		// word will be represented by that signature
//		if (signature.length() > 0 && isNumericWord(signature))
//		{
//			return rootOfTree.getWordsFromSignature(signature);
//		}
//		
//		// return empty HashSet if the signature isn't valid (containing non numeric characters)
//		// or the signature is empty (this must be done because if not, the empty signature will return
//		// non empty set with just one element that is an empty string or "", not null, this happen
//		// because this class search by prefix, not by fixed signature)
//		return new HashSet<String>();
//	}
//	
//	/**
//	 * Method to check if a string just contain alphabet characters
//	 * @param word : string to be checked
//	 * @return true if the word just contain alphabet, false if it contain non alphabet
//	 */
//	private boolean isValidWord(String word)
//	{
//		for (int i = 0; i < word.length(); i++)
//		{
//			if (word.charAt(i) < 'a' || word.charAt(i) > 'z')
//			{
//				return false;
//			}
//		}
//		return true;
//	}
//	
//	/**
//	 * Method to check if a all string characters are numeric with range from 2 to 9
//	 * @param word : string to be checked
//	 * @return true if string just contain numeric, false if there's one or more non numeric or '1' in string
//	 */
//	private boolean isNumericWord(String word)
//	{
//		for (int i = 0; i < word.length(); i++)
//		{
//			if (word.charAt(i) < '2' || word.charAt(i) > '9')
//			{
//				return false;
//			}
//		}
//		return true;
//	}
//}