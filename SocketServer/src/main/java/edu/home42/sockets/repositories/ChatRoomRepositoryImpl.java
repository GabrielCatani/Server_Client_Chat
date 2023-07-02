package edu.home42.sockets.repositories;

import edu.home42.sockets.models.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ChatRoomRepositoryImpl implements ChatRoomRepository{

    private JdbcTemplate template;

    private String table;

    @Autowired
    public ChatRoomRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.table = new String("chat_users.rooms");
    }

    @Override
    public Optional findById(Long id) {
        String sql = "SELECT * FROM " + this.table + " WHERE id = ?";

        RowMapper<ChatRoom> rowMapper = (rs, rowNum)->{
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setId(id);
            chatRoom.setName(rs.getString("name"));

            return chatRoom;
        };

        try {
            return Optional.of(this.template.queryForObject(sql, rowMapper, id));
        }
        catch(EmptyResultDataAccessException exception){
            return null;
        }
    }

    @Override
    public List findAll() throws SQLException {
        String sql = "SELECT * FROM " + this.table;
        RowMapper<ChatRoom> rowMapper = (rs, rowNum) -> {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setId(rs.getLong("id"));
            chatRoom.setName(rs.getString("name"));
            return chatRoom;
        };
        return this.template.query(sql, rowMapper);
    }

    @Override
    public void save(Object entity) {
        String sql = "INSERT INTO " + this.table + "(name) VALUES"
                + "(:name)";

        ChatRoom chatRoom = (ChatRoom)entity;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", chatRoom.getName());
        this.template.update(sql, params);
    }

    @Override
    public void update(Object entity) {
        ChatRoom chatRoom = (ChatRoom) entity;

        try {
            this.findById(chatRoom.getId());
        }
        catch(Exception e) {
            return;
        }

        String sql = "UPDATE " + this.table + " SET "
                + "name = :name";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", chatRoom.getName());
        this.template.update(sql, params);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM " + this.table + " WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        this.template.update(sql, params);
    }
}
