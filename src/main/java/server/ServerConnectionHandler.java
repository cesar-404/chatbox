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

    @Override
    public void run() {

    }
}

