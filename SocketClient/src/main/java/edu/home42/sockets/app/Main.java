package edu.home42.sockets.app;

import edu.home42.sockets.model.Client;
import edu.home42.sockets.model.ClientServerOutputReader;
import edu.home42.sockets.model.ClientUserInputReaderAndSender;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Client client = null;
        ClientServerOutputReader messageGetter = null;
        ClientUserInputReaderAndSender messageSender = null;

        try {
            client = new Client("127.0.0.1", 6666);
            messageSender = new ClientUserInputReaderAndSender(client);
            messageGetter = new ClientServerOutputReader(client);
        }
        catch (IOException e) {
            System.err.println("Unable to connect to server!");
            System.exit(1);
        }
        try {
            client.signInClientToServer();
            messageSender.start();
            messageGetter.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
