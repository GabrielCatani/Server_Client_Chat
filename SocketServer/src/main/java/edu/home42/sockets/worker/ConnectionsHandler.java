package edu.home42.sockets.worker;

import edu.home42.sockets.models.User;
import edu.home42.sockets.server.Server;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.Socket;


public class ConnectionsHandler extends Thread{

    private Server server;
    private Socket clientSocket;

    @Autowired
    public ConnectionsHandler(Server server) {
        this.server = server;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            User newUser = server.signInClient(this.clientSocket);
            server.startMessenger(newUser, this.clientSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
