package edu.home42.sockets.app;

import edu.home42.sockets.config.SocketsApplicationConfig;
import edu.home42.sockets.models.User;
import edu.home42.sockets.repositories.UsersRepositoryImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        UsersRepositoryImpl usrRepo = context.getBean(UsersRepositoryImpl.class);

        User usr = new User();
        usr.setUsername("Beni");
        usr.setPassword("fera123");
        usrRepo.save(usr);
    }
}
