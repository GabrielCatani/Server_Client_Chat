package edu.home42.sockets.server;

import edu.home42.sockets.models.ChatRoom;
import edu.home42.sockets.models.Message;
import edu.home42.sockets.models.User;
import edu.home42.sockets.services.ChatRoomServiceImpl;
import edu.home42.sockets.services.MessageServiceImpl;
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
    private MessageServiceImpl msgService;
    private ChatRoomServiceImpl chatRoomService;
    private ServerSocket serverSocket;
    private Integer port;
    private List<User> loggedUsers;
    private List<ChatRoom> availableRooms;


    @Autowired
    public Server(UsersServiceImpl usersService, MessageServiceImpl msgService , ChatRoomServiceImpl chatRoomService,Integer port) {
        this.usersService = usersService;
        this.msgService = msgService;
        this.chatRoomService = chatRoomService;
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
        //Persist user
        System.out.println(user.toString());
        this.usersService.signUp(user);
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

        if (this.usersService.signIn(user)) {
            this.logUser(user, clientSocket);
            this.sendMessage("Log in successful!", clientSocket);
        }
        else {
            this.sendMessage("Log in Failed!", clientSocket);
        }

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

            Message message = new Message(msgText, user);
            System.out.println(message.toString());

            //persist message
            this.msgService.saveMessage(message);

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

    public void broadcastMessage(String message) throws IOException {
        ListIterator<User> iterator = this.loggedUsers.listIterator(0);
        while(iterator.hasNext()) {
            this.sendMessage(message, iterator.next().getClientSocket());
        }
    }

    public User initialMenu(Socket clientSocket) throws IOException {
        this.sendMessage("Hello from Server!", clientSocket);
        this.sendMessage("1. Sign In\n2. Sign up\n3. Exit\n> ", clientSocket);

        Integer option = Integer.parseInt(this.receiveMessage(clientSocket));
        User user = null;
        switch(option) {
            case 1:
                user = this.signInClient(clientSocket);
                break;
            case 2:
                this.signUpClient(clientSocket);
                break;
            default:
                break;
        }
        return user;
    }

    public Long roomMenu(Socket clientSocket) throws IOException {
        this.sendMessage("1. Create Room\n2. Choose Room\n3. Exit\n> ", clientSocket);
        Integer usrOption = Integer.parseInt(this.receiveMessage(clientSocket));

        switch (usrOption) {
            case 1:
                this.createNewRoom(clientSocket);
                break;
            case 2:
                return this.listRoomsToChoose(clientSocket);
            default:
                break;
        }

        return -1L;
    }

    public void createNewRoom(Socket clientSocket) throws IOException {
        this.sendMessage("Enter new room name: ", clientSocket);
        ChatRoom newChatRoom = new ChatRoom();
        newChatRoom.setName(this.receiveMessage(clientSocket));
        this.chatRoomService.addNewChatRoom(newChatRoom);
    }

    public Long listRoomsToChoose(Socket clientSocket) throws IOException {
        this.availableRooms = this.chatRoomService.getAllChatRooms();

        StringBuilder availableRoomsToShow = new StringBuilder();

        int index = 0;
        String line = null;
        for (ChatRoom room: this.availableRooms) {
            index++;
            line = index + "." + room.getName() + "|";
            availableRoomsToShow.append(line);
        }
        availableRoomsToShow.append(++index + ".Exit");

        this.sendMessage(availableRoomsToShow.toString(), clientSocket);
        Integer usrOption = Integer.parseInt(this.receiveMessage(clientSocket));

        if (usrOption <= index) {
            Long roomId = this.availableRooms.get(usrOption - 1).getId();
            this.sendMessage(roomId.toString(), clientSocket);
            return roomId;
        }

        return -1l;
    }
}
