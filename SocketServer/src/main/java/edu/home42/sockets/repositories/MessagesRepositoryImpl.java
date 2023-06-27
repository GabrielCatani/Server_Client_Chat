package edu.home42.sockets.repositories;

import edu.home42.sockets.models.Message;
import edu.home42.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MessagesRepositoryImpl implements MessagesRepository{

    private JdbcTemplate template;
    private String table;

    @Autowired
    public MessagesRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.table = "chat_users.messages";
    }

    @Override
    public Optional findById(Long id) {
        String sql = "SELECT * FROM " + this.table + " WHERE id = ?";

        RowMapper<Message> rowMapper = (rs, rowNum) -> {
            Message msg = new Message();
            msg.setId(rs.getLong("id"));
            msg.setMessage(rs.getString("message"));
            msg.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            return msg;
        };

        try {
            return Optional.of(this.template.queryForObject(sql, rowMapper, id));
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List findAll() throws SQLException {
        String sql = "SELECT * FROM " + this.table;
        RowMapper<Message> rowMapper = (rs, rowNum) -> {
            Message message = new Message();
            message.setId(rs.getLong("id"));
            message.setMessage(rs.getString("message"));
            message.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            return message;
        };

        return this.template.query(sql, rowMapper);
    }

    @Override
    public void save(Object entity) {
        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(this.template);
        String sql = "INSERT INTO " + this.table + " (username, password)" +
                "VALUES(:message, :timestamp)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        Message msg = (Message)entity;
        params.addValue("message", msg.getMessage())
                .addValue("timestamp", msg.getTimestamp());

        namedTemplate.update(sql, params);
    }

    @Override
    public void update(Object entity) {
        Message msg = (Message)entity;
        try {
            this.findById(msg.getId());
        }
        catch (EmptyResultDataAccessException e) {
            return;
        }

        String sql = "UPDATE " + this.table + " SET "
                + "message = :message,"
                + "timestamp = :timestamp"
                + " WHERE id = :id";

        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(this.template);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("message", msg.getMessage())
                .addValue("timestamp", msg.getTimestamp())
                .addValue("id", msg.getId());

        namedTemplate.update(sql, params);
    }

    @Override
    public void delete(Long id) {
        Message msg;
        Optional opt = this.findById(id);
        if (opt.isPresent()) {
            msg = (Message) opt.get();


            String sql = "DELETE FROM " + this.table + " WHERE id = :id";

            NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(this.template);
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", msg.getId());

            namedTemplate.update(sql, params);
        }
    }
}
