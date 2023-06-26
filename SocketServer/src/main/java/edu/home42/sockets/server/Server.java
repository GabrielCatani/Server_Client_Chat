package edu.home42.sockets.server;

import edu.home42.sockets.models.User;
import edu.home42.sockets.services.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private UsersServiceImpl usersService;
    private ServerSocket serverSocket;
    private Integer port;
    private InputStreamReader inputStream;
    private BufferedReader inputBuffer;

    private PrintWriter outputWriter;

    @Autowired
    public Server(UsersServiceImpl usersService, Integer port) {
        this.usersService = usersService;
        this.port = port;
    }

    public Server() {
    }
    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public UsersServiceImpl getUsersService() {
        return usersService;
    }

    public void setUsersService(UsersServiceImpl usersService) {
        this.usersService = usersService;
    }

    public void init() throws IOException {
        this.serverSocket = new ServerSocket(this.port);
    }

    public void listenAndAccept() throws IOException {
        Socket clientSocket = this.serverSocket.accept();
        inputStream = new InputStreamReader(clientSocket.getInputStream());
        inputBuffer = new BufferedReader(inputStream);
        outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
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
        this.inputBuffer.close();
        this.serverSocket.close();
    }

    public void signUpClient() throws IOException {
        User user = new User();
        this.sendMessage(this.greetings());
        this.sendMessage("> signUp");
        this.sendMessage("Enter username:");
        user.setUsername(this.receiveMessage());
        this.sendMessage("Enter password:");
        user.setPassword(this.receiveMessage());
        System.out.println(user.toString());
        this.sendMessage("Successful!");
    }

    public String greetings() {
        return new String("Hello from Server!");
    }
}
