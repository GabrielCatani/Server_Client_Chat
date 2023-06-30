package edu.home42.sockets.services;

import edu.home42.sockets.repositories.MessagesRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService{
    private MessagesRepositoryImpl messagesRepository;

    public MessageServiceImpl(MessagesRepositoryImpl messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @Override
    public void saveMessage(Object entity) {
        this.messagesRepository.save(entity);
    }
}
