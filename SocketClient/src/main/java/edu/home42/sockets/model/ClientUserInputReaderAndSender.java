package edu.home42.sockets.model;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientUserInputReaderAndSender extends Thread{
    private Client client;

    public ClientUserInputReaderAndSender(Client client) {

        this.client = client;
    }

    @Override
    public void run() {
        try {
            this.client.startMessaging();
            this.client.getServerSocket().shutdownInput();
            this.client.getServerSocket().shutdownOutput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
