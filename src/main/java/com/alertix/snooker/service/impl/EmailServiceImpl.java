package com.alertix.snooker.service.impl;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alertix.snooker.domain.NewsletterSubscriber;
import com.alertix.snooker.repository.NewsletterSubscriberRepository;
import com.alertix.snooker.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    @Qualifier("infoMailSender")
    private JavaMailSender infoSender;

    @Autowired
    @Qualifier("supportMailSender")
    private JavaMailSender supportSender;

    @Autowired
    @Qualifier("promoMailSender")
    private JavaMailSender promoSender;

    @Value("${app.mail.info.username}")
    private String infoFrom;

    @Value("${app.mail.support.username}")
    private String supportFrom;

    @Value("${app.mail.promo.username}")
    private String promoFrom;

    @Autowired
    private NewsletterSubscriberRepository newsletterSubscriberRepository;

    @Override
    @Async
    public void sendInfoEmail(String to, String subject, String content) {
        sendEmail(infoSender, infoFrom, to, subject, content);
    }

    @Override
    @Async
    public void sendSupportEmail(String to, String subject, String content) {
        sendEmail(supportSender, supportFrom, to, subject, content);
    }

    @Override
    @Async
    public void sendPromoEmail(String to, String subject, String content) {
        sendEmail(promoSender, promoFrom, to, subject, content);
    }

    @Override
    @Async
    public void broadcastMatchResult(String subject, String content) {
        List<NewsletterSubscriber> subscribers = newsletterSubscriberRepository.findByIsActiveTrue();
        for (NewsletterSubscriber sub : subscribers) {
            // Using Promo sender for newsletters
            sendEmail(promoSender, promoFrom, sub.getEmail(), subject, content);
        }
    }

    private void sendEmail(JavaMailSender sender, String from, String to, String subject, String content) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(Objects.requireNonNull(from));
            helper.setTo(Objects.requireNonNull(to));
            helper.setSubject(Objects.requireNonNull(subject));
            helper.setText(Objects.requireNonNull(content), true);

            sender.send(message);
            logger.info("Email sent to {} from {}", to, from);
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
