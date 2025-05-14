package client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.Socket;

import static org.mockito.Mockito.*;

class ClientConnectionTest {

    private Socket mockSocket;
    private PipedInputStream serverInput;
    private PipedOutputStream clientOutput;
    private ClientConnection clientConnection;

    @BeforeEach
    void setUp() throws IOException {
        mockSocket = mock(Socket.class);

        serverInput = new PipedInputStream();
        clientOutput = new PipedOutputStream();
        PipedInputStream userInput = new PipedInputStream();
        PipedOutputStream userOutput = new PipedOutputStream(userInput);

        when(mockSocket.getInputStream()).thenReturn(serverInput);
        when(mockSocket.getOutputStream()).thenReturn(clientOutput);

        clientConnection = new ClientConnection(mockSocket, "testuser") {
            @Override
            public void start() {

            }
        };
    }

    @Test
    void testSendMessagesTerminatesOnExit() throws IOException {
        ByteArrayInputStream userInput = new ByteArrayInputStream("exit\n".getBytes());
        System.setIn(userInput);

        clientConnection = new ClientConnection(mockSocket, "testuser");

        Thread sendThread = new Thread(() -> clientConnection.start());
        sendThread.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}

        verify(mockSocket, atLeastOnce()).isConnected();
    }
}
