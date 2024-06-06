package com.bitespeed.assignment.mapper;

import com.bitespeed.assignment.dto.request.ContactRequest;
import com.bitespeed.assignment.dto.response.ContactData;
import com.bitespeed.assignment.dto.response.ContactResponse;
import com.bitespeed.assignment.models.Contact;
import com.bitespeed.assignment.models.LinkPrecedence;
import com.bitespeed.assignment.others.Utils;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ContactMapper {

    public Contact buildPrimaryContact(ContactRequest contactRequest) {
        return Contact.builder()
            .email(Utils.getOrNull(contactRequest::getEmail))
            .phoneNumber(Utils.getOrNull(contactRequest::getPhoneNumber))
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

    public Contact buildSecondaryContact(ContactRequest contactRequest, Contact primaryContact) {
        return Contact.builder()
                .email(contactRequest.getEmail())
                .phoneNumber(contactRequest.getPhoneNumber())
                .linkedId(primaryContact.getId())
                .linkPrecedence(LinkPrecedence.SECONDARY)
                .build();
    }

    public Contact getPrimaryContact(List<Contact> contacts) {
        return contacts.stream()
                .filter(contact -> contact.getLinkPrecedence() == LinkPrecedence.PRIMARY)
                .findFirst()
                .orElse(contacts.get(0));
    }
}
