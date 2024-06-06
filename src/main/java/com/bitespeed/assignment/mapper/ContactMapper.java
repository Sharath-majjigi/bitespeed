package com.bitespeed.assignment.mapper;

import com.bitespeed.assignment.dto.request.ContactRequest;
import com.bitespeed.assignment.dto.response.ContactData;
import com.bitespeed.assignment.dto.response.ContactResponse;
import com.bitespeed.assignment.models.Contact;
import com.bitespeed.assignment.models.LinkPrecedence;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ContactMapper {

    public Contact buildPrimaryContact(ContactRequest contactRequest) {
        return Contact.builder()
            .email(contactRequest.getEmail())
            .phoneNumber(contactRequest.getPhoneNumber())
            .linkPrecedence(LinkPrecedence.PRIMARY)
            .build();
    }

    public ContactResponse getContactResponse(Contact newContact,List<Long> secondaryContactIds) {
        return ContactResponse.builder()
                .contact(ContactData.builder()
                        .primaryContactId(newContact.getId())
                        .emails(List.of(newContact.getEmail()))
                        .phoneNumbers(List.of(newContact.getPhoneNumber()))
                        .secondaryContactIds(secondaryContactIds)
                        .build())
                .build();
    }
}
