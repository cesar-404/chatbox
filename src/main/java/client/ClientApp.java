package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientApp {

    private static final Logger logger = Logger.getLogger(ClientApp.class.getName());

    public static void main(String[] args) {
        new ClientApp().runClient();
    }

    private void runClient() {
        final String host = "localhost";
        final int port = 8080;

        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket(host, port)) {

            System.out.print("Please enter your username: ");
            String username = scanner.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Exiting...");
                return;
            }

            ClientConnection client = new ClientConnection(socket, username);
            client.start();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not connect to server at " + host + ":" + port, e);
        }
    }
}
