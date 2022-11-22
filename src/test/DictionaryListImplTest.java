package test;

import predictive.DictionaryListImpl;

public class DictionaryListImplTest {

	public static void main(String[] args) {
		
		DictionaryListImpl dictionary = new DictionaryListImpl();
		
		System.out.println(dictionary.signatureToWords("2665"));
		System.out.println(dictionary.signatureToWords("43556"));
		System.out.println(dictionary.signatureToWords("4663"));
		System.out.println(dictionary.signatureToWords("96753"));
		System.out.println(dictionary.signatureToWords("26653288737"));
		System.out.println(dictionary.signatureToWords("2264"));
		System.out.println(dictionary.signatureToWords("2263245"));
		System.out.println(dictionary.signatureToWords("aabcd"));
		System.out.println(dictionary.signatureToWords("384667e"));
		System.out.println(dictionary.signatureToWords("3846673"));
		System.out.println(dictionary.signatureToWords("329"));
		
		System.out.println();
		System.out.println(dictionary.signatureToWords("4663"));
		System.out.println(dictionary.signatureToWords("43556"));
		System.out.println(dictionary.signatureToWords("96753"));
		System.out.println(dictionary.signatureToWords("69"));
		System.out.println(dictionary.signatureToWords("6263"));
		System.out.println(dictionary.signatureToWords("47"));
		
		System.out.println();
		System.out.println(dictionary.signatureToWords("123"));
		System.out.println(dictionary.signatureToWords("2267444625"));
	}

}
