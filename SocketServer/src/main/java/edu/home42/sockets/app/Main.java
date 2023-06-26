package edu.home42.sockets.app;

import edu.home42.sockets.config.SocketsApplicationConfig;
import edu.home42.sockets.models.User;
import edu.home42.sockets.repositories.UsersRepositoryImpl;
import edu.home42.sockets.server.Server;
import edu.home42.sockets.services.UsersServiceImpl;
import edu.home42.sockets.worker.ConnectionsHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        UsersServiceImpl usrService = context.getBean(UsersServiceImpl.class);

        Server server = new Server(usrService, 6666);

        try {
            server.init();
            while(true) {
                Socket clientSocket = server.listenAndAccept();
                ConnectionsHandler connectionsHandler = new ConnectionsHandler(server);
                connectionsHandler.setClientSocket(clientSocket);
                connectionsHandler.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
