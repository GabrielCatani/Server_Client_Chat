package edu.home42.sockets.services;

import java.util.List;

public interface ChatRoomService<T> {
    void addNewChatRoom(T entity);
    void changeNameOfChatRoom(T entity);
    List<T> getAllChatRooms();
}
