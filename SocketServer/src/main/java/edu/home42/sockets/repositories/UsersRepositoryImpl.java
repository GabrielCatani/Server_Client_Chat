package edu.home42.sockets.repositories;

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
public class UsersRepositoryImpl implements UsersRepository{

    private JdbcTemplate template;
    private String usrTable;

    @Autowired
    public UsersRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.usrTable = "chat_users.users";
    }

    @Override
    public Optional findById(Long id) {
        String sql = "SELECT * FROM " + this.usrTable + " WHERE id = ?";

        RowMapper<User> rowMapper = (rs, rowNum) -> {
            User usr = new User();
            usr.setId(rs.getLong("id"));
            usr.setUsername(rs.getString("username"));
            usr.setPassword(rs.getString("password"));
            return usr;
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
        String sql = "SELECT * FROM " + this.usrTable;
        RowMapper<User> rowMapper = (rs, rowNum) -> {
            User usr = new User();
            usr.setId(rs.getLong("id"));
            usr.setUsername(rs.getString("username"));
            usr.setPassword(rs.getString("password"));
            return usr;
        };

        return this.template.query(sql, rowMapper);
    }

    @Override
    public void save(Object entity) {
        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(this.template);
        String sql = "INSERT INTO " + this.usrTable + " (username, password)" +
                "VALUES(:username, :password)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        User user = (User)entity;
        params.addValue("username", user.getUsername())
                .addValue("password", user.getPassword());

        namedTemplate.update(sql, params);
    }

    @Override
    public void update(Object entity) {
        User usr = (User)entity;
        try {
            this.findById(usr.getId());
        }
        catch (EmptyResultDataAccessException e) {
            return;
        }

        String sql = "UPDATE " + this.usrTable + " SET "
                + "username = :username,"
                + "password = :password"
                + " WHERE id = :id";

        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(this.template);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", usr.getUsername())
                .addValue("password", usr.getPassword())
                .addValue("id", usr.getId());

        namedTemplate.update(sql, params);
    }

    @Override
    public void delete(Long id) {
        User usr;
        Optional opt = this.findById(id);
        if (opt.isPresent()) {
            usr = (User) opt.get();


            String sql = "DELETE FROM " + this.usrTable + " WHERE id = :id";

            NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(this.template);
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", usr.getId());

            namedTemplate.update(sql, params);
        }
    }

    @Override
    public Optional<User> findByName(User user) {
        String sql = "SELECT * FROM " + this.usrTable + " WHERE username = ?";

        RowMapper<User> rowMapper = (rs, rowNum) -> {
            User matchedUser = new User();
            matchedUser.setId(rs.getLong("id"));
            matchedUser.setUsername(rs.getString("username"));
            matchedUser.setPassword(rs.getString("password"));
            return matchedUser;
        };

        List<User> list = this.template.query(sql, rowMapper, user.getUsername());

        return Optional.of(list.get(0));
    }
}
