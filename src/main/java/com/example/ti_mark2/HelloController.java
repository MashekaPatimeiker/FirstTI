package com.example.ti_mark2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class HelloController {
    @FXML
    private ComboBox<String> algorithmComboBox;

    @FXML
    private TextField keyField;

    @FXML
    private TextField inputField;

    @FXML
    private TextArea outputArea;

    @FXML
    private Button encryptButton;

    @FXML
    private Button decryptButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button readFileButton;
    @FXML
    private Button writeFileButton;

    @FXML
    public void initialize() {
        algorithmComboBox.setValue("Шифр Плейфейра (английский)");
        encryptButton.setOnAction(event -> encryptText());
        decryptButton.setOnAction(event -> decryptText());
        clearButton.setOnAction(event -> clearText());
        readFileButton.setOnAction(event -> readFromFile());
        writeFileButton.setOnAction(event -> writeToExistingFile());
    }

    private void encryptText() {
        String algorithm = algorithmComboBox.getValue();
        String key = cleanInput(algorithm, keyField.getText());
        String inputText = cleanInput(algorithm, inputField.getText());

        // Update the input fields with cleaned values
        keyField.setText(key);
        inputField.setText(inputText);

        // Check if cleaned input is empty
        if (key.isEmpty() || inputText.isEmpty()) {
            outputArea.setText("Пожалуйста, проверьте вводимые значения. Используйте только допустимые символы.");
            return;
        }

        if (algorithm.equals("Шифр Плейфейра (английский)")) {
            PlayfairCipher cipher = new PlayfairCipher(key);
            String encryptedText = cipher.encrypt(inputText);
            outputArea.setText(encryptedText);
        } else if (algorithm.equals("Алгоритм Виженера (русский)")) {
            String encryptedText = VigenerCipher.encrypt(inputText, key);
            outputArea.setText(encryptedText);
        } else {
            outputArea.setText("Алгоритм не поддерживается.");
        }
    }

    private void decryptText() {
        String algorithm = algorithmComboBox.getValue();
        String key = cleanInput(algorithm, keyField.getText());
        String encryptedText = cleanInput(algorithm, outputArea.getText());
        keyField.setText(key);
        outputArea.setText(encryptedText);
        if (key.isEmpty() || encryptedText.isEmpty()) {
            outputArea.setText("Пожалуйста, проверьте вводимые значения. Используйте только допустимые символы.");
            return;
        }
        if (algorithm.equals("Шифр Плейфейра (английский)")) {
            PlayfairCipher cipher = new PlayfairCipher(key);
            String decryptedText = cipher.decrypt(encryptedText);
            outputArea.setText(decryptedText);
        } else if (algorithm.equals("Алгоритм Виженера (русский)")) {
            String decryptedText = VigenerCipher.decrypt(encryptedText, key);
            outputArea.setText(decryptedText);
        } else {
            outputArea.setText("Алгоритм не поддерживается.");
        }
    }

    private String cleanInput(String algorithm, String text) {
        if (algorithm.equals("Шифр Плейфейра (английский)")) {
            return text.toUpperCase().replaceAll("[^A-Z]", "");
        } else if (algorithm.equals("Алгоритм Виженера (русский)")) {
            return text.toUpperCase().replaceAll("[^А-ЯЁ]", "");
        }
        return text;
    }

    private void clearText() {
        outputArea.clear();
        inputField.clear();
        keyField.clear();
    }
    private void readFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл для чтения");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                inputField.setText(content);
                outputArea.setText(content);
            } catch (IOException e) {
                outputArea.setText("Ошибка при чтении файла: " + e.getMessage());
            }
        }
    }
    private void writeToExistingFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите существующий файл для записи");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file, false)) { // `false` ensures overwriting the file
                writer.write(outputArea.getText());
            } catch (IOException e) {
                outputArea.setText("Ошибка при записи файла: " + e.getMessage());
            }
        }
    }
}
