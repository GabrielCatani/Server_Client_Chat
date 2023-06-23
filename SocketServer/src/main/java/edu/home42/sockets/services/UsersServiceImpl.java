package edu.home42.sockets.services;

import edu.home42.sockets.models.User;
import edu.home42.sockets.repositories.UsersRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class UsersServiceImpl implements UsersService{

    PasswordEncoder passwordEncoder;
    UsersRepositoryImpl usrRepo;

    @Autowired
    public UsersServiceImpl(PasswordEncoder passwordEncoder, UsersRepositoryImpl usrRepo) {
        this.passwordEncoder = passwordEncoder;
        this.usrRepo = usrRepo;
    }

    @Override
    public void signUp(Object entity) {
        User usr = (User)entity;

        //Encrypt password
        String encodedPassword = passwordEncoder.encode(usr.getPassword());
        usr.setPassword(encodedPassword);

        //Call save, from UserRepo
        usrRepo.save(usr);
    }
}
