package edu.home42.sockets.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientServerOutputReader extends Thread {

    private Client client;

    public ClientServerOutputReader(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = this.client.receiveMessage()) != null) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
