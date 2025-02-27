ByteStuffingServer.java--

import java.io.*;
import java.net.*;

class ByteStuffingServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            System.out.println("Server is running... Waiting for client...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String stuffedData = inFromClient.readLine();
            System.out.println("Received Stuffed Data: " + stuffedData);

            // Perform byte unstuffing
            String unstuffedData = byteUnstuffing(stuffedData);
            System.out.println("Original Data After Unstuffing: " + unstuffedData);

            inFromClient.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String byteUnstuffing(String stuffedData) {
        StringBuilder result = new StringBuilder();
        boolean escapeNext = false;

        for (char c : stuffedData.toCharArray()) {
            if (escapeNext) {
                result.append(c); // Add the escaped character as it is
                escapeNext = false;
            } else if (c == 'E') {
                escapeNext = true; // Escape detected, next character is stuffed
            } else {
                result.append(c); // Normal character
            }
        }
        return result.toString();
    }
}


ByteStuffingClient.java--

import java.io.*;
import java.net.*;
import java.util.Scanner;

class ByteStuffingClient {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 6789);
            System.out.println("Connected to Server!");

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter a sequence of D (Data), E (Escape), F (Flag): ");
            String inputData = scanner.nextLine();

            // Perform byte stuffing
            String stuffedData = byteStuffing(inputData);
            System.out.println("Stuffed Data Sent: " + stuffedData);

            // Send stuffed data
            outToServer.writeBytes(stuffedData + "\n");

            scanner.close();
            outToServer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String byteStuffing(String data) {
        StringBuilder stuffed = new StringBuilder();

        for (char c : data.toCharArray()) {
            if (c == 'F' || c == 'E') {
                stuffed.append("E"); // Add escape before special characters
            }
            stuffed.append(c); // Add actual character
        }

        return stuffed.toString();
    }
}


