package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerApp {

    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());

    public static void main(String[] args) {
        final int PORT = 8080;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Starting server on port " + PORT + "...");
            ChatServer chatServer = new ChatServer(serverSocket);
            chatServer.startServer();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to start server on port " + PORT, e);
        }
    }
}
