package pl.jedrychowski.mydypfacilit;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.jedrychowski.mydypfacilit.Configuration.EmailConfiguration;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class EmailService  {

    @Autowired
    private EmailConfiguration emailConfiguration;

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(emailConfiguration.getHost());
        mailSenderImpl.setPort(emailConfiguration.getPort());
        mailSenderImpl.setUsername(emailConfiguration.getUsername());
        mailSenderImpl.setPassword(emailConfiguration.getPassword());
        return mailSenderImpl;
    }

    public void sendEmail(String from, String to, String body, String topic){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(topic);
        simpleMailMessage.setText(body);
        getJavaMailSender().send(simpleMailMessage);
    }

    public void sendMessageWithAttachment(String from,String to, String subject, String text, MultipartFile file) throws MessagingException, IOException {
        // ...

        MimeMessage message = getJavaMailSender().createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("noreply@baeldung.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        DataSource source = new FileDataSource(new File(file.getName()));
        byte[] sourceBytes = file.getBytes();
        OutputStream sourceOS = source.getOutputStream();
        sourceOS.write(sourceBytes);
        String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "nofilename";
        helper.addAttachment(fileName, source);

        getJavaMailSender().send(message);
    }
}
