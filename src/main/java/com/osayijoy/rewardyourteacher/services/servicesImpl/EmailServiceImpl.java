package com.decagon.rewardyourteacher.services.servicesImpl;
import com.decagon.rewardyourteacher.dto.EmailResponse;
import com.decagon.rewardyourteacher.services.EmailService;
import com.decagon.rewardyourteacher.utils.EmailDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public EmailResponse sendSimpleMail(EmailDetails details, String sender) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return new EmailResponse(true, "Mail sent successfully");
        }
        catch (MailException e) {
            return new EmailResponse(false, "Error while sending mail\n" + e.getMessage());
        }
    }

    @Override
    public EmailResponse sendMailWithAttachment(EmailDetails details, String sender) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

            javaMailSender.send(mimeMessage);
            return new EmailResponse(true, "Mail sent successfully");
        }

        catch (MessagingException e) {
            return new EmailResponse(false, "Error while sending mail!!!");
        }
    }
}
