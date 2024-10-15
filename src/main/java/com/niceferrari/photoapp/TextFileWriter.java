package com.niceferrari.photoapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles writing a message to a log file.
 *
 * @see SimpleDateFormat
 */
public class TextFileWriter {

    private String fileName = "appLog.txt";

    public TextFileWriter() {
    }

    /**
     * Writes a message to a log file.
     *
     * @param message The message to write to the log file.
     */
    public void writeMessageToFile(String message) {
        try {
            // Specifies the path of the file.
            Path filePath = Paths.get(fileName);

            // Creates the file if it doesn't exist.
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            // Gets the current date and time.
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateFormat.format(new Date());

            // Appends the message with date and time to the file.
            String formattedMessage = formattedDateTime + " - " + message + "\n";
            Files.write(filePath, formattedMessage.getBytes(), StandardOpenOption.APPEND);

            System.out.println("Message written to the file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}