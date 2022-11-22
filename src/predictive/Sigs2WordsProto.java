package predictive;

/**
 * This class is used to test PredictivePrototype.signatureToWords() method
 * @author Hans Sean Nathanael
 *
 */
public class Sigs2WordsProto {

	public static void main(String[] args) {
		
		for (String argument : args)
		{
			System.out.println(argument + " : " + PredictivePrototype.signatureToWords(argument));
		}
	}
}
