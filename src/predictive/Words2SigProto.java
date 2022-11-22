package predictive;

/**
 * This class is used to test PredictivePrototype.wordToSignature() method
 * @author Hans Sean Nathanael
 *
 */
public class Words2SigProto {

	public static void main(String[] args) {
		
		for(String argument : args)
		{
			System.out.print(PredictivePrototype.wordToSignature(argument) + " ");
		}
	}
}
