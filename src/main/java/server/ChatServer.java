package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer {

    private static final Logger logger = Logger.getLogger(ChatServer.class.getName());
    private final ServerSocket serverSocket;

    public ChatServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        logger.info("Server started. Waiting for clients...");

        try {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                ServerConnectionHandler clientHandler = new ServerConnectionHandler(clientSocket);

                logger.info("Client connected: " + clientHandler.getClientUsername());

                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error accepting client connection", e);
        } finally {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                logger.info("Server socket closed.");
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing server socket", e);
        }
    }
}