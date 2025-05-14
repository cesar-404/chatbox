package server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnectionHandler implements Runnable {

    private static final Logger logger = Logger.getLogger(ServerConnectionHandler.class.getName());
    private static final List<ServerConnectionHandler> connectionHandlers = new CopyOnWriteArrayList<>();

    private final Socket socket;
    private BufferedReader inputReader;
    private BufferedWriter outputWriter;
    private String clientUsername;

    public ServerConnectionHandler(Socket socket) {
        this.socket = socket;
        try {
            initializeStreams();
            this.clientUsername = inputReader.readLine();
            connectionHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error initializing client handler", e);
            closeConnection();
        }
    }

    private void initializeStreams() throws IOException {
        this.inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    private void broadcastMessage(String message) {
        for (ServerConnectionHandler connectionHandler : connectionHandlers) {
            if (connectionHandler != this) {
                connectionHandler.sendMessage(message);
            }
        }
    }

    private void sendMessage(String message) {
        try {
            outputWriter.write(message);
            outputWriter.newLine();
            outputWriter.flush();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error sending message to " + clientUsername, e);
            closeConnection();
        }
    }

    private void removeClientHandler() {
        connectionHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat.");
    }

    private void closeConnection() {
        removeClientHandler();
        closeResources();
    }

    private void closeResources() {
        try {
            if (inputReader != null) inputReader.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing input stream", e);
        }
        try {
            if (inputReader != null) inputReader.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing output stream", e);
        }
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing socket", e);
        }
    }

    public String getClientUsername() {
        return clientUsername;
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = inputReader.readLine()) != null) {
                if (message.equalsIgnoreCase("Exit")) {
                    break;
                }
                broadcastMessage(message);
            }
        } catch (IOException e) {
            logger.log(Level.INFO, "Connection lost with " + clientUsername, e);
        } finally {
            closeConnection();
        }
    }
}

