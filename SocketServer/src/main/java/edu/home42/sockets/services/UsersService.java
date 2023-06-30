package edu.home42.sockets.services;

public interface UsersService<T> {
    void signUp(T entity);
    boolean signIn(T entity);
}
