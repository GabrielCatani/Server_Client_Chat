package edu.home42.sockets.config;

import com.zaxxer.hikari.HikariDataSource;
import edu.home42.sockets.repositories.ChatRoomRepositoryImpl;
import edu.home42.sockets.repositories.MessagesRepositoryImpl;
import edu.home42.sockets.repositories.UsersRepositoryImpl;
import edu.home42.sockets.server.Server;
import edu.home42.sockets.services.ChatRoomServiceImpl;
import edu.home42.sockets.services.MessageServiceImpl;
import edu.home42.sockets.services.UsersServiceImpl;
import edu.home42.sockets.worker.ConnectionsHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:db.properties")
public class SocketsApplicationConfig {
    @Value("${db.url}")
    private String url;
    @Value("${db.user}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.driver.name}")
    private String driverClassName;

    @Bean
    public HikariDataSource hikariDao() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(this.url);
        hikariDataSource.setUsername(this.username);
        hikariDataSource.setPassword(this.password);
        hikariDataSource.setDriverClassName(this.driverClassName);

        return hikariDataSource;
    }

    @Bean
    public UsersRepositoryImpl usersRepository(HikariDataSource hikariDao) {
        return new UsersRepositoryImpl(hikariDao);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UsersServiceImpl usersService(PasswordEncoder passwordEncoder, UsersRepositoryImpl usrRepo) {
        return new UsersServiceImpl(passwordEncoder, usrRepo);
    }

    @Bean
    public MessagesRepositoryImpl messagesRepository(DataSource dataSource) {
        return new MessagesRepositoryImpl(dataSource);
    }

    @Bean
    public MessageServiceImpl messageService(MessagesRepositoryImpl msgRepo) {
        return new MessageServiceImpl(msgRepo);
    }

    @Bean
    public ChatRoomRepositoryImpl chatRoomRepository (DataSource dataSource) {
        return new ChatRoomRepositoryImpl(dataSource);
    }

    @Bean
    public ChatRoomServiceImpl chatRoomService(ChatRoomRepositoryImpl chatRoomRepository) {
        return new ChatRoomServiceImpl(chatRoomRepository);
    }

    @Bean
    public ConnectionsHandler connectionsHandler(Server server) {
        return new ConnectionsHandler(server);
    }

    @Bean
    public Server server() {
        return new Server();
    }

}
