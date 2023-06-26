package edu.home42.sockets.worker;

import edu.home42.sockets.server.Server;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;


public class ConnectionsHandler extends Thread{

    private Server server;

    @Autowired
    public ConnectionsHandler(Server server) {
        this.server = server;
    }
    @Override
    public void run() {
        try {
            server.greetings();
            server.signInClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
