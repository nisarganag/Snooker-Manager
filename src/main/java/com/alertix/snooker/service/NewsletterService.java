package com.alertix.snooker.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alertix.snooker.domain.NewsletterSubscriber;
import com.alertix.snooker.repository.NewsletterSubscriberRepository;

@Service
public class NewsletterService {

    @Autowired
    private NewsletterSubscriberRepository repository;

    public void subscribe(String email) {
        Optional<NewsletterSubscriber> existing = repository.findByEmail(email);
        if (existing.isPresent()) {
            NewsletterSubscriber sub = existing.get();
            if (!sub.isActive()) {
                sub.setActive(true);
                repository.save(sub);
            }
        } else {
            repository.save(new NewsletterSubscriber(email));
        }
    }

    public void unsubscribe(String email) {
        Optional<NewsletterSubscriber> existing = repository.findByEmail(email);
        existing.ifPresent(sub -> {
            sub.setActive(false);
            repository.save(sub);
        });
    }
}
