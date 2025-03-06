package com.example.ti_mark2;

public class VigenerCipher {

    private static final String RUSSIAN_ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

    // Helper function to shift text by a given amount
    private static String getShiftedText(String text, int shift) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            int index = RUSSIAN_ALPHABET.indexOf(c);
            if (index != -1) { // Ensure the character is in the alphabet
                int shiftedIndex = (index + shift) % RUSSIAN_ALPHABET.length();
                result.append(RUSSIAN_ALPHABET.charAt(shiftedIndex));
            }
        }

        return result.toString();
    }

    // Helper function to pad the Vigenère key to match the text length
    private static String getPaddedVigenereKey(String key, int lengthToPad) {
        StringBuilder paddedKey = new StringBuilder(key);
        int shift = 1;

        while (paddedKey.length() < lengthToPad) {
            paddedKey.append(getShiftedText(key, shift));
            shift++;
        }

        return paddedKey.substring(0, lengthToPad);
    }

    // Encrypt function
    public static String encrypt(String text, String key) {
        text = text.toUpperCase().replaceAll("[^А-ЯЁ]", ""); // Remove non-Cyrillic characters
        key = key.toUpperCase().replaceAll("[^А-ЯЁ]", ""); // Remove non-Cyrillic characters

        if (key.isEmpty()) {
            throw new IllegalArgumentException("Ключ не может быть пустым");
        }

        String paddedKey = getPaddedVigenereKey(key, text.length());
        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char textChar = text.charAt(i);
            int textIndex = RUSSIAN_ALPHABET.indexOf(textChar);
            int keyIndex = RUSSIAN_ALPHABET.indexOf(paddedKey.charAt(i));

            if (textIndex != -1 && keyIndex != -1) {
                int encryptedIndex = (textIndex + keyIndex) % RUSSIAN_ALPHABET.length();
                encryptedText.append(RUSSIAN_ALPHABET.charAt(encryptedIndex));
            }
        }

        return encryptedText.toString();
    }

    // Decrypt function
    public static String decrypt(String text, String key) {
        text = text.toUpperCase().replaceAll("[^А-ЯЁ]", ""); // Remove non-Cyrillic characters
        key = key.toUpperCase().replaceAll("[^А-ЯЁ]", ""); // Remove non-Cyrillic characters

        if (key.isEmpty()) {
            throw new IllegalArgumentException("Ключ не может быть пустым");
        }

        String paddedKey = getPaddedVigenereKey(key, text.length());
        StringBuilder decryptedText = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char textChar = text.charAt(i);
            int textIndex = RUSSIAN_ALPHABET.indexOf(textChar);
            int keyIndex = RUSSIAN_ALPHABET.indexOf(paddedKey.charAt(i));

            if (textIndex != -1 && keyIndex != -1) {
                int decryptedIndex = (textIndex - keyIndex + RUSSIAN_ALPHABET.length()) % RUSSIAN_ALPHABET.length();
                decryptedText.append(RUSSIAN_ALPHABET.charAt(decryptedIndex));
            }
        }

        return decryptedText.toString();
    }
}
