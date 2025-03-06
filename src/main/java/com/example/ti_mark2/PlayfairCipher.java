package com.example.ti_mark2;

import java.util.ArrayList;
import java.util.List;

public class PlayfairCipher {
    private char[][] matrix;

    public PlayfairCipher(String key) {
        this.matrix = generateMatrix(key);
    }
    public String decrypt(String text) {
        text = text.toUpperCase().replace("J", "I").replaceAll("[^A-Z]", "");
        List<String> pairs = createPairs(text);

        StringBuilder decryptedText = new StringBuilder();
        for (String pair : pairs) {
            decryptedText.append(decryptPair(pair));
        }

        // Remove the trailing 'X' if it was added as padding
        if (decryptedText.length() > 1 && decryptedText.charAt(decryptedText.length() - 1) == 'X') {
            decryptedText.deleteCharAt(decryptedText.length() - 1);
        }

        // Remove filler 'X' between repeated letters
        for (int i = 1; i < decryptedText.length() - 1; i++) {
            if (decryptedText.charAt(i) == 'X' && decryptedText.charAt(i - 1) == decryptedText.charAt(i + 1)) {
                decryptedText.deleteCharAt(i);
            }
        }

        return decryptedText.toString();
    }

    private String decryptPair(String pair) {
        char first = pair.charAt(0);
        char second = pair.charAt(1);

        int[] firstPos = findPosition(first);
        int[] secondPos = findPosition(second);

        if (firstPos[0] == secondPos[0]) { // Same row
            return "" + matrix[firstPos[0]][(firstPos[1] + 4) % 5] +
                    matrix[secondPos[0]][(secondPos[1] + 4) % 5];
        } else if (firstPos[1] == secondPos[1]) { // Same column
            return "" + matrix[(firstPos[0] + 4) % 5][firstPos[1]] +
                    matrix[(secondPos[0] + 4) % 5][secondPos[1]];
        } else { // Rectangle
            return "" + matrix[firstPos[0]][secondPos[1]] +
                    matrix[secondPos[0]][firstPos[1]];
        }
    }

    private char[][] generateMatrix(String key) {
        String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ"; // 'J' is omitted
        key = key.toUpperCase().replace("J", "I").replaceAll("[^A-Z]", "");
        StringBuilder uniqueKey = new StringBuilder();

        // Remove duplicates from the key
        for (char c : key.toCharArray()) {
            if (uniqueKey.indexOf(String.valueOf(c)) == -1) {
                uniqueKey.append(c);
            }
        }

        // Add remaining letters of the alphabet
        for (char c : alphabet.toCharArray()) {
            if (uniqueKey.indexOf(String.valueOf(c)) == -1) {
                uniqueKey.append(c);
            }
        }

        // Fill the 5x5 matrix
        char[][] matrix = new char[5][5];
        int index = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                matrix[row][col] = uniqueKey.charAt(index++);
            }
        }
        return matrix;
    }

    public String encrypt(String text) {
        text = text.toUpperCase().replace("J", "I").replaceAll("[^A-Z]", "");
        List<String> pairs = createPairs(text);

        StringBuilder encryptedText = new StringBuilder();
        for (String pair : pairs) {
            encryptedText.append(encryptPair(pair));
        }
        return encryptedText.toString();
    }

    private List<String> createPairs(String text) {
        List<String> pairs = new ArrayList<>();
        for (int i = 0; i < text.length(); i += 2) {
            char first = text.charAt(i);
            char second = (i + 1 < text.length()) ? text.charAt(i + 1) : 'X';

            if (first == second) {
                pairs.add("" + first + 'X');
                i--; // Recheck the second character in the next iteration
            } else {
                pairs.add("" + first + second);
            }
        }
        return pairs;
    }

    private String encryptPair(String pair) {
        char first = pair.charAt(0);
        char second = pair.charAt(1);

        int[] firstPos = findPosition(first);
        int[] secondPos = findPosition(second);

        if (firstPos[0] == secondPos[0]) { // Same row
            return "" + matrix[firstPos[0]][(firstPos[1] + 1) % 5] +
                    matrix[secondPos[0]][(secondPos[1] + 1) % 5];
        } else if (firstPos[1] == secondPos[1]) { // Same column
            return "" + matrix[(firstPos[0] + 1) % 5][firstPos[1]] +
                    matrix[(secondPos[0] + 1) % 5][secondPos[1]];
        } else { // Rectangle
            return "" + matrix[firstPos[0]][secondPos[1]] +
                    matrix[secondPos[0]][firstPos[1]];
        }
    }

    private int[] findPosition(char c) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (matrix[row][col] == c) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }
}
