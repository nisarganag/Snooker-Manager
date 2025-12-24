package com.alertix.snooker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alertix.snooker.payload.request.NewsletterRequest;
import com.alertix.snooker.payload.response.MessageResponse;
import com.alertix.snooker.service.NewsletterService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/newsletter")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@Valid @RequestBody NewsletterRequest request) {
        newsletterService.subscribe(request.getEmail());
        return ResponseEntity.ok(new MessageResponse("Subscribed successfully!"));
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@Valid @RequestBody NewsletterRequest request) {
        newsletterService.unsubscribe(request.getEmail());
        return ResponseEntity.ok(new MessageResponse("Unsubscribed successfully!"));
    }
}
