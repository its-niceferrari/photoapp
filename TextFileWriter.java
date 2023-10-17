package com.niceferrari.photoapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFileWriter {

    private String fileName = "appLog.txt";

    public TextFileWriter() {
    }

    public void writeMessageToFile(String message) {
        try {
            // Specify the path of the file
            Path filePath = Paths.get(fileName);

            // Create the file if it doesn't exist
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            // Get the current date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateFormat.format(new Date());

            // Append the message with date and time to the file
            String formattedMessage = formattedDateTime + " - " + message + "\n";
            Files.write(filePath, formattedMessage.getBytes(), StandardOpenOption.APPEND);

            System.out.println("Message written to the file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
