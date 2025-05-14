package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.mockito.Mockito.*;

class ServerConnectionHandlerTest {

    private Socket mockSocket;
    private BufferedReader inputReader;
    private BufferedWriter outputWriter;
    private PipedOutputStream clientOut;

    @BeforeEach
    void setUp() throws IOException {
        mockSocket = mock(Socket.class);

        ByteArrayInputStream clientInput = new ByteArrayInputStream("testuser\nHello there\nexit\n".getBytes());
        clientOut = new PipedOutputStream();

        when(mockSocket.getInputStream()).thenReturn(clientInput);
        when(mockSocket.getOutputStream()).thenReturn(clientOut);
    }

    @Test
    void testHandlerReceivesMessages() {
        ServerConnectionHandler handler = new ServerConnectionHandler(mockSocket);
        Thread t = new Thread(handler);
        t.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}

        assert handler.getClientUsername().equals("testuser");
    }
}
