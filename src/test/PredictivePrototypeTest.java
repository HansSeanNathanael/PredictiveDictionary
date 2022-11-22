package test;

import predictive.PredictivePrototype;

public class PredictivePrototypeTest {

	public static void main(String[] args) {
		
		System.out.println(PredictivePrototype.wordToSignature("Hello"));
		System.out.println(PredictivePrototype.wordToSignature("Book"));
		System.out.println(PredictivePrototype.wordToSignature("gone"));
		System.out.println(PredictivePrototype.wordToSignature("home"));
		System.out.println(PredictivePrototype.wordToSignature("world"));
		System.out.println(PredictivePrototype.wordToSignature(""));
		System.out.println(PredictivePrototype.wordToSignature("1010a1234567890[;./'[bf"));
		
		System.out.println(PredictivePrototype.signatureToWords("2665"));
		System.out.println(PredictivePrototype.signatureToWords("43556"));
		System.out.println(PredictivePrototype.signatureToWords("4663"));
		System.out.println(PredictivePrototype.signatureToWords("96753"));
		System.out.println(PredictivePrototype.signatureToWords("26653288737"));
		System.out.println(PredictivePrototype.signatureToWords("2264"));
		System.out.println(PredictivePrototype.signatureToWords("2263245"));
		System.out.println(PredictivePrototype.signatureToWords("aabcd"));
		System.out.println(PredictivePrototype.signatureToWords("384667e"));
		System.out.println(PredictivePrototype.signatureToWords("3846673"));
		System.out.println(PredictivePrototype.signatureToWords("329"));
	}
}
