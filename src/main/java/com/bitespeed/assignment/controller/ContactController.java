package com.bitespeed.assignment.controller;

import com.bitespeed.assignment.service.ContactService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ContactController {

    ContactService contactService;
    @PostMapping("/identify")
    public Map<String, Object> identify(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String phoneNumber = request.get("phoneNumber");
        return contactService.identifyContact(email, phoneNumber);
    }
}
