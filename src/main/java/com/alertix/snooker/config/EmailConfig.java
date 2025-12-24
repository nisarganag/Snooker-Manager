package com.alertix.snooker.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

    // Info User
    @Value("${app.mail.info.host}")
    private String infoHost;
    @Value("${app.mail.info.port}")
    private int infoPort;
    @Value("${app.mail.info.username}")
    private String infoUsername;
    @Value("${app.mail.info.password}")
    private String infoPassword;
    @Value("${app.mail.info.properties.mail.smtp.auth}")
    private String infoAuth;
    @Value("${app.mail.info.properties.mail.smtp.ssl.enable}")
    private String infoSsl;

    // Support User
    @Value("${app.mail.support.host}")
    private String supportHost;
    @Value("${app.mail.support.port}")
    private int supportPort;
    @Value("${app.mail.support.username}")
    private String supportUsername;
    @Value("${app.mail.support.password}")
    private String supportPassword;
    @Value("${app.mail.support.properties.mail.smtp.auth}")
    private String supportAuth;
    @Value("${app.mail.support.properties.mail.smtp.ssl.enable}")
    private String supportSsl;

    // Promo User
    @Value("${app.mail.promo.host}")
    private String promoHost;
    @Value("${app.mail.promo.port}")
    private int promoPort;
    @Value("${app.mail.promo.username}")
    private String promoUsername;
    @Value("${app.mail.promo.password}")
    private String promoPassword;
    @Value("${app.mail.promo.properties.mail.smtp.auth}")
    private String promoAuth;
    @Value("${app.mail.promo.properties.mail.smtp.ssl.enable}")
    private String promoSsl;

    @Bean
    public JavaMailSender infoMailSender() {
        return createMailSender(infoHost, infoPort, infoUsername, infoPassword, infoAuth, infoSsl);
    }

    @Bean
    public JavaMailSender supportMailSender() {
        return createMailSender(supportHost, supportPort, supportUsername, supportPassword, supportAuth, supportSsl);
    }

    @Bean
    public JavaMailSender promoMailSender() {
        return createMailSender(promoHost, promoPort, promoUsername, promoPassword, promoAuth, promoSsl);
    }

    private JavaMailSender createMailSender(String host, int port, String username, String password, String auth,
            String ssl) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.ssl.enable", ssl);
        props.put("mail.debug", "false"); // Disable debug in production

        return mailSender;
    }
}
