package edu.home42.sockets.server;

import edu.home42.sockets.models.Message;
import edu.home42.sockets.models.User;
import edu.home42.sockets.services.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Server {
    private UsersServiceImpl usersService;
    private ServerSocket serverSocket;
    private Integer port;
    private List<User> loggedUsers;

    @Autowired
    public Server(UsersServiceImpl usersService, Integer port) {
        this.usersService = usersService;
        this.port = port;
        this.loggedUsers = new LinkedList<>();
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

    public Socket listenAndAccept() throws IOException {
        Socket clientSocket = this.serverSocket.accept();
        return clientSocket;
    }

    public void sendMessage(String msg, Socket clientSocket) throws IOException {
        PrintWriter outputWriter = new PrintWriter(clientSocket.getOutputStream());
        outputWriter.println(msg);
        outputWriter.flush();
    }

    public String receiveMessage(Socket clientSocket) throws IOException {
        String line = new String();
        BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        line = inputBuffer.readLine();

        return line;
    }

    public void close()  {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void signUpClient(Socket clientSocket) throws IOException {
        User user = new User();
        this.sendMessage(this.greetings(), clientSocket);
        this.sendMessage("> signUp", clientSocket);
        this.sendMessage("Enter username:", clientSocket);
        user.setUsername(this.receiveMessage(clientSocket));
        this.sendMessage("Enter password:", clientSocket);
        user.setPassword(this.receiveMessage(clientSocket));
        System.out.println(user.toString());
        this.sendMessage("Successful!", clientSocket);
    }

    public String greetings() {
        return new String("Hello from Server!");
    }

    public User signInClient(Socket clientSocket) throws IOException {
        User user = new User();
        this.sendMessage(this.greetings(), clientSocket);
        this.sendMessage(">signIn", clientSocket);
        this.sendMessage("Enter username:", clientSocket);
        user.setUsername(this.receiveMessage(clientSocket));
        this.sendMessage("Enter password:", clientSocket);
        user.setPassword(this.receiveMessage(clientSocket));

        System.out.println(this.usersService.signIn(user));
        this.logUser(user, clientSocket);

        return user;
    }

    public void startMessenger(User user, Socket clientSocket) throws IOException, InterruptedException {
        this.sendMessage("Logged", clientSocket);
        String msgText;
        this.sendMessage("Start messaging", clientSocket);
        while ((msgText = this.receiveMessage(clientSocket)) != null) {
            if (msgText.compareTo("Exit") == 0) {
                break;
            }

            Message message = this.wrapMessage(msgText, user);
            System.out.println(message.toString());
            //persist message
            //broadcast message
            this.broadcastMessage(message.toString());
        }
    }

    public String logUser(User user, Socket clientSocket) {
        if (user.isLogged()) {
            return user.getUsername() + " is already logged in.";
        }
        user.setLogged(true);
        user.setClientSocket(clientSocket);
        this.loggedUsers.add(user);
        return user.getUsername() + " logged!";
    }

    public Message wrapMessage(String msgText, User usr) {
        Message message = new Message(msgText);
        message.setSender(usr);
        return message;
    }

    public void broadcastMessage(String message) throws IOException {
        ListIterator<User> iterator = this.loggedUsers.listIterator(0);
        while(iterator.hasNext()) {
            this.sendMessage(message, iterator.next().getClientSocket());
        }
    }
}
