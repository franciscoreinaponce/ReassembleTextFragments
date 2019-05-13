package fragment.submissions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FranciscoReinaPonce {

	private static final String SEPARATOR = ";";

	public static void main(String[] args) throws IOException {

		try (BufferedReader in = new BufferedReader(new FileReader("E:/input.txt"))) {
			in.lines().map(FranciscoReinaPonce::reassemble).forEach(System.out::println);
		}

	}

	/**
	 * Given a line of text which contains text fragments separated by a semicolon, ';'.
	 * Look for the pair of fragments with the maximum overlap to merge them.
	 * Repeat the process until only one fragment remains.
	 * 
	 * @param line Text line to reassemble
	 * 
	 * @return Reassembled text line
	 * 
	 */
	public static String reassemble(String line) {
		List<String> textPieces = new LinkedList<String>(Arrays.asList(line.split(SEPARATOR)));

		int numberPendingText = textPieces.size();
		while (numberPendingText > 1) {

			List<String> pairTextWithMaximalOverlap = locatePairWithMaximalOverlap(textPieces);

			if (pairTextWithMaximalOverlap != null && pairTextWithMaximalOverlap.size() == 2) {

				String reassemblyText = reassembleTwoFragments(pairTextWithMaximalOverlap.get(0), pairTextWithMaximalOverlap.get(1));
				textPieces.removeAll(pairTextWithMaximalOverlap);
				textPieces.add(reassemblyText);

			}

			// To avoid possible infinite loops
			if (numberPendingText == textPieces.size()) {
				break;
			}

			numberPendingText = textPieces.size();
		}

		return textPieces.get(0);
	}
	
	/**
	 * Given a line of text which contains text fragments separated by a semicolon, ';'.
	 * Return the pair of fragments with the maximum overlap to merge them.
	 * 
	 * @param line Text line to reassemble
	 * 
	 * @return Pair of fragments with the maximum overlap
	 * 
	 */
	public static List<String> locatePairWithMaximalOverlap(List<String> textPieces) {
		HashMap<Integer, List<String>> overlapNumberPerText = new HashMap<Integer, List<String>>();

		for (String textPiece : textPieces) {
			// Scrolls through each item in the list, with unprocessed items
			for (int i = textPieces.indexOf(textPiece) + 1; i < textPieces.size(); i++) {

				String nextTextPiece = textPieces.get(i);
				overlapNumberPerText.put(calculateOverlapsNumberForTwoFragments(textPiece, nextTextPiece),
						Arrays.asList(textPiece, nextTextPiece));
			}
		}
		
		Integer maximumOverlap = overlapNumberPerText.keySet().stream().mapToInt(Integer::valueOf).max().orElse(0);

		return overlapNumberPerText.get(maximumOverlap);
	}

	/**
	 * Given two text fragments calculates the maximum overlap to merge them.
	 * 
	 * @param first Text to compare
	 * @param second Text to compare
	 * 
	 * @return Maximum overlap between both fragments
	 * 
	 */
	public static int calculateOverlapsNumberForTwoFragments(String first, String second) {

		// Case "ABCDEF" and "BCDE"
		if (first.contains(second)) {
			return second.length();
		} else if (second.contains(first)) {
			return first.length();
		}

		boolean isFirstShortestThanSecond = first.length() < second.length();
		int shortestWordSize = isFirstShortestThanSecond ? first.length() : second.length();
		String shortestWord = isFirstShortestThanSecond ? first : second;
		String largestWord = !isFirstShortestThanSecond ? first : second;

		// Case "ABCDEF" and "DEFG"
		// Case "ABCDEF" and "XYZABC"
		for (int index = 0; index < shortestWordSize; index++) {

			if (largestWord.startsWith(shortestWord.substring(index))
					|| largestWord.endsWith(shortestWord.substring(0, shortestWordSize - index))) {

				return shortestWordSize - index;
			}

		}

		// Case "ABCDEF" and "XCDEZ" 
		return 0;
	}

	/**
	 * Given two text fragments, these are reassembled.
	 * 
	 * @param first Text to reassemble
	 * @param second Text to reassemble
	 * 
	 * @return Reassembled text
	 * 
	 */
	public static String reassembleTwoFragments(String first, String second) {
		
		// To reassemble the case "ABCDEF" and "BCDE"
		if (first.contains(second)) {
			return first;
		} else if (second.contains(first)) {
			return second;
		}

		boolean isFirstShortestThanSecond = first.length() < second.length();
		int shortestWordSize = isFirstShortestThanSecond ? first.length() : second.length();
		String shortestWord = isFirstShortestThanSecond ? first : second;
		String largestWord = !isFirstShortestThanSecond ? first : second;

		// To reassemble the case "ABCDEF" and "DEFG"
		// To reassemble the case "ABCDEF" and "XYZABC"
		for (int index = 0; index < shortestWordSize; index++) {

			if (largestWord.startsWith(shortestWord.substring(index))) {
				return shortestWord.substring(0, index) + largestWord;
			}

			if (largestWord.endsWith(shortestWord.substring(0, shortestWordSize - index))) {
				return largestWord + shortestWord.substring(shortestWordSize - index, shortestWordSize);
			}

		}

		// To reassemble the case "ABCDEF" and "XCDEZ"
		return "";
	}
	
}
