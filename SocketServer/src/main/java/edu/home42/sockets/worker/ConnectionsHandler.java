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

    //TODO: Transform toStirng from Message into Json
    //TODO: Add column "RoomID" to Message Table
    //TODO: Replace all string messages between Client-Server for JSON
    @Override
    public void run() {
        try {
            User newUser = server.initialMenu(this.clientSocket);
            if (newUser != null && newUser.isLogged()) {
                Long chatRoomId = server.roomMenu(this.clientSocket);
                if (chatRoomId != -1L) {
                    newUser.setCurrentRoomId(chatRoomId);
                    server.startMessenger(newUser, clientSocket);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
