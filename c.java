import java.net.*;

public class ChatServer {
    public static void main(String[] args) {
        int port = 5002;
        byte[] buffer = new byte[1024];

        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            System.out.println("UDP Server started on port " + port);

            while (true) {
                // Receive message from client
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(receivePacket);
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                System.out.println("Client: " + receivedMessage);

                // If client sends "exit", stop responding
                if (receivedMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Client disconnected.");
                    break;
                }

                // Sending response to client
                String serverMessage = "Server received: " + receivedMessage;
                byte[] sendData = serverMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
                                                               receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
            }

        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}


_______________________________________________________________________________________________________________________________



import java.net.*;
import java.io.*;

public class ChatClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 5002;
        byte[] buffer = new byte[1024];

        try (DatagramSocket clientSocket = new DatagramSocket();
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            InetAddress serverIP = InetAddress.getByName(serverAddress);
            System.out.println("Connected to UDP server. Type messages to send:");

            while (true) {
                // Read user input
                String message = userInput.readLine();
                byte[] sendData = message.getBytes();

                // Send message to server
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIP, port);
                clientSocket.send(sendPacket);

                // If "exit" is entered, terminate the client
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Client terminated.");
                    break;
                }

                // Receive response from server
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(receivePacket);
                String serverMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Server: " + serverMessage);
            }

        } catch (Exception e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}