package server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

class ChatServerTest {

    @Test
    void testServerSocketCloses() throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        ChatServer chatServer = new ChatServer(serverSocket);
        chatServer.closeServerSocket();

        assertTrue(serverSocket.isClosed());
    }
}
