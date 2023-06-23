package edu.home42.sockets.app;

import edu.home42.sockets.config.SocketsApplicationConfig;
import edu.home42.sockets.models.User;
import edu.home42.sockets.repositories.UsersRepositoryImpl;
import edu.home42.sockets.services.UsersServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        User usr = new User();
        usr.setUsername("Tiranossauro");
        usr.setPassword("dinossauro");

        UsersServiceImpl usrService = context.getBean(UsersServiceImpl.class);
        usrService.signUp(usr);
    }
}
