package edu.home42.sockets.repositories;

import edu.home42.sockets.models.User;

import java.util.Optional;

public interface UsersRepository<T> extends CrudRepository{
    Optional<T> findByName(User user);
}
