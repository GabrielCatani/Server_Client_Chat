package edu.home42.sockets.services;

import edu.home42.sockets.models.ChatRoom;
import edu.home42.sockets.repositories.ChatRoomRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ChatRoomServiceImpl<T> implements ChatRoomService{
    public ChatRoomRepository<T> chatRoomRepository;

    public ChatRoomServiceImpl(ChatRoomRepository<T> chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public void addNewChatRoom(Object entity) {
        this.chatRoomRepository.save(entity);
    }

    @Override
    public Long getChatRoomId(String roomName) {
        return null;
    }

    @Override
    public void changeNameOfChatRoom(Object entity) {
        ChatRoom chatRoomUpdated = (ChatRoom)entity;

        Optional<ChatRoom> chatRoomOpt = this.chatRoomRepository.findById(chatRoomUpdated.getId());
        chatRoomUpdated.setId(chatRoomOpt.get().getId());
        if (chatRoomOpt.isPresent()) {
            this.chatRoomRepository.update(chatRoomUpdated);
        }
    }

    @Override
    public List getAllChatRooms() {
        try {
            return this.chatRoomRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
