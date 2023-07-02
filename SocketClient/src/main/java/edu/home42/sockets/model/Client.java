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

    public boolean signInClientToServer() throws IOException {
        System.out.println(this.receiveMessage());
        System.out.println(this.receiveMessage());
        System.out.println(this.receiveMessage());

        Scanner sc = new Scanner(System.in);
        this.sendMessage(sc.nextLine());
        System.out.println(this.receiveMessage());
        this.sendMessage(sc.nextLine());
        String loginStatus = this.receiveMessage();

        System.out.println(loginStatus);
        if (loginStatus.compareTo("Log in Failed!") == 0) {
            return false;
        }
        return true;
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

    public void logout() throws IOException {
        this.serverSocket.shutdownInput();
        this.serverSocket.shutdownOutput();
    }

    public boolean initialMenu() throws IOException {
        System.out.println(this.receiveMessage());
        System.out.println(this.receiveMessage());
        System.out.println(this.receiveMessage());
        System.out.println(this.receiveMessage());
        System.out.print(this.receiveMessage());

        Scanner sc = new Scanner(System.in);
        String msg = sc.nextLine();
        this.sendMessage(msg);

        switch (Integer.parseInt(msg)) {
            case 1:
                return this.signInClientToServer();
            case 2:
                this.signUpClientToServer();
                break;
            default:
                this.logout();
                break;
        }
        return false;
    }

    public boolean roomMenu() throws IOException {
        System.out.println(this.receiveMessage());
        System.out.println(this.receiveMessage());
        System.out.println(this.receiveMessage());
        System.out.print(this.receiveMessage());

        Scanner sc = new Scanner(System.in);
        String option = sc.nextLine();
        this.sendMessage(option);

        switch (Integer.parseInt(option)) {
            case 1:
                this.createNewRoom();
                break;
            case 2:
                return this.listRoomsToChoose();
            default:
                break;
        }

        return false;
    }

    public void createNewRoom() throws IOException {
        System.out.print(this.receiveMessage());
        Scanner sc = new Scanner(System.in);

        this.sendMessage(sc.nextLine());
    }

    public boolean listRoomsToChoose() throws IOException {
        StringBuilder fullMsg = new StringBuilder();
        fullMsg.append(this.receiveMessage());

        String outputForClient = fullMsg.toString().replaceAll("\\|", "\n");

        System.out.println(outputForClient);
        Scanner sc = new Scanner(System.in);

        this.sendMessage(sc.nextLine());
        Long roomId = Long.parseLong(this.receiveMessage());
        if (roomId != -1L) {
            return true;
        }

        return false;
    }
}
