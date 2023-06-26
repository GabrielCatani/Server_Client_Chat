package edu.home42.sockets.app;

import edu.home42.sockets.model.Client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Client client = null;
        try {
            client = new Client("127.0.0.1", 6666);
        }
        catch (IOException e) {
            System.err.println("Unable to connect to server!");
            System.exit(1);
        }
        //Intro Server
        try {
            client.signUpToClient();
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
