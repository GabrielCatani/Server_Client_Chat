package edu.home42.sockets.services;

import edu.home42.sockets.models.User;
import edu.home42.sockets.repositories.UsersRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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

    @Override
    public boolean signIn(Object entity) {
        User usr = (User)entity;

        //Find user by name
        Optional<User> opt = this.usrRepo.findByName(usr);
        if (opt.isPresent()) {
            User matchedUser = opt.get();
            if (this.passwordEncoder.matches(usr.getPassword(), matchedUser.getPassword())) {
                return true;
            }
        }

        return false;
    }
}
