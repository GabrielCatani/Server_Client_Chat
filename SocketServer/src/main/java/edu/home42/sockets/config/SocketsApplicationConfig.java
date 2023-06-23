package edu.home42.sockets.config;

import com.zaxxer.hikari.HikariDataSource;
import edu.home42.sockets.repositories.UsersRepositoryImpl;
import edu.home42.sockets.services.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
}
