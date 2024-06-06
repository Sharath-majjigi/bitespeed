package com.bitespeed.assignment.controller;

import com.bitespeed.assignment.dto.request.ContactRequest;
import com.bitespeed.assignment.dto.response.ContactResponse;
import com.bitespeed.assignment.service.ContactService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ContactController {

    ContactService contactService;

    @PostMapping("/identify")
    public ContactResponse identify(@RequestBody ContactRequest contactRequest) {
        return contactService.identifyContact(contactRequest);
    }
}
