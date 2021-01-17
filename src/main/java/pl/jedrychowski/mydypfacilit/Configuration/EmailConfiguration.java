package pl.jedrychowski.mydypfacilit.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailConfiguration {
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private Integer port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${admin.mail.address}")
    private String fromAddress;

    public String getHost() {
        return host;
    }

    @Bean
    public SimpleMailMessage templateCreateAccount() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Twoje konto zostało utworzone | MyDypFacilit");
        message.setFrom(fromAddress);
        message.setText(
                "Twoje konto w aplikacji MyDypFacilit zostało utworzone! \n Twój login: %s\n Twoje hasło: %s\n\n Pamiętaj, że hasło możesz zmienić po zalogowaniu się pod zakładką \"Ustawienia\"");
        return message;
    }

    @Bean
    public SimpleMailMessage templateStatusChange() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Status twojej pracy został zmieniony | MyDypFacilit");
        message.setFrom(fromAddress);
        message.setText(
                "Status twojej pracy w aplikacji MyDypFacilit zostało zmieniony!\n poprzedni status: %s\n nowy status: %s\n\n");
        return message;
    }

    @Bean
    public SimpleMailMessage templatePromoterChangeDiplomaTopicStatus() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Promotor zmienił status twojej pracy | MyDypFacilit");
        message.setFrom(fromAddress);
        message.setText(
                "Twój promotor zmienił status twojej pracy!\n poprzedni status: %s\n nowy status: %s\n\n %s");
        return message;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "EmailConfiguration [host=" + host + ", port=" + port + ", username=" + username + ", password="
                + password + "]";
    }
}
