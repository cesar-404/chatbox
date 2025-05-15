package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnection {

    private static final Logger logger = Logger.getLogger(ClientConnection.class.getName());

    private final Socket socket;
    private final BufferedReader inputReader;
    private final BufferedWriter outputWriter;
    private final String username;
    private final Scanner scanner;
    private volatile boolean isClosed = false;

    public ClientConnection(Socket socket, String username) throws IOException {
        this.socket = socket;
        this.username = username;
        this.inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        listenForMessages();
        sendMessages();
    }

    private void sendMessages() {
        try {
            outputWriter.write(username);
            outputWriter.newLine();
            outputWriter.flush();

            while (socket.isConnected()) {
                String message = scanner.nextLine().trim();
                if (message.equalsIgnoreCase("exit")) {
                    closeConnection();
                    break;
                }

                outputWriter.write(username + ": " + message);
                outputWriter.newLine();
                outputWriter.flush();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error sending message", e);
            closeConnection();
        }
    }

    private void listenForMessages() {
        Thread listenerThread = new Thread(() -> {
            String messageFromServer;
            try {
                while (socket.isConnected() && (messageFromServer = inputReader.readLine()) != null) {
                    System.out.println(messageFromServer);
                }
            } catch (IOException e) {
                logger.log(Level.FINE, "Connection closed by server.", e);
                closeConnection();
            }
        });

        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private synchronized void closeConnection() {
        if (isClosed) return;
        isClosed = true;

        try {
            if (!socket.isClosed()) socket.close();
            if (inputReader != null) inputReader.close();
            if (outputWriter != null) outputWriter.close();
            scanner.close();
            logger.info("Connection closed.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error closing resources", e);
        }
    }
}