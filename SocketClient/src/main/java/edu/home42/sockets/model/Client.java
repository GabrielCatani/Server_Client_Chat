package edu.home42.sockets.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket serverSocket;
    private InputStreamReader inputStream;
    private BufferedReader inputBuffer;

    private PrintWriter outputWriter;

    public Client() {
    }

    public Client(String ip, Integer port) throws IOException {
        this.serverSocket = new Socket(ip, port);
        this.inputStream = new InputStreamReader(this.serverSocket.getInputStream());
        this.inputBuffer = new BufferedReader(this.inputStream);
        this.outputWriter = new PrintWriter(this.serverSocket.getOutputStream());
    }

    public void sendMessage(String msg) {
        this.outputWriter.println(msg);
        this.outputWriter.flush();
    }

    public String receiveMessage() throws IOException {
        String line = new String();

        line = this.inputBuffer.readLine();

        return line;
    }

    public void close() throws IOException {
        this.outputWriter.close();
        this.inputBuffer.close();
        this.inputStream.close();
        this.serverSocket.close();
    }

    public void signUpClientToServer() throws IOException {
        System.out.println(this.receiveMessage());
        System.out.println(this.receiveMessage());
        System.out.println(this.receiveMessage());

        Scanner sc = new Scanner(System.in);
        this.sendMessage(sc.nextLine());

        System.out.println(this.receiveMessage());
        this.sendMessage(sc.nextLine());

        System.out.println(this.receiveMessage());
    }

    public void signInClientToServer() throws IOException {
        System.out.println(this.receiveMessage());
        System.out.println(this.receiveMessage());
        System.out.println(this.receiveMessage());

        Scanner sc = new Scanner(System.in);
        this.sendMessage(sc.nextLine());
        System.out.println(this.receiveMessage());
        this.sendMessage(sc.nextLine());
    }

    public void startMessaging() throws IOException {
        System.out.println(this.receiveMessage());
        Scanner sc = new Scanner(System.in);
        String msg;

        while (sc.hasNextLine()) {
            msg = sc.nextLine();
            if (msg.compareTo("Exit") == 0) {
                System.out.println("You have left the chat.");
                break;
            }
            this.sendMessage(msg);
        }
    }

    public Socket getServerSocket() {
        return serverSocket;
    }
}
