package com.bitespeed.assignment.mapper;

import com.bitespeed.assignment.dto.request.ContactRequest;
import com.bitespeed.assignment.dto.response.ContactData;
import com.bitespeed.assignment.dto.response.ContactResponse;
import com.bitespeed.assignment.models.Contact;
import com.bitespeed.assignment.models.LinkPrecedence;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContactMapper {

    public Contact buildPrimaryContact(ContactRequest contactRequest) {
        return Contact.builder()
                .email(contactRequest.getEmail() != null ? contactRequest.getEmail() : "")
                .phoneNumber(contactRequest.getPhoneNumber() != null ? contactRequest.getPhoneNumber() : "")
                .linkPrecedence(LinkPrecedence.PRIMARY)
                .build();
    }

    public ContactResponse getContactResponse(Contact primaryContact, List<Contact> secondaryContacts) {
        List<String> emails = new ArrayList<>();
        List<String> phoneNumbers = new ArrayList<>();

        emails.add(primaryContact.getEmail());
        phoneNumbers.add(primaryContact.getPhoneNumber());

        for (Contact secondaryContact : secondaryContacts) {
            if (!emails.contains(secondaryContact.getEmail())) {
                emails.add(secondaryContact.getEmail());
            }
            if (!phoneNumbers.contains(secondaryContact.getPhoneNumber())) {
                phoneNumbers.add(secondaryContact.getPhoneNumber());
            }
        }

        List<Long> secondaryContactIds = secondaryContacts.stream()
                .map(Contact::getId)
                .collect(Collectors.toList());

        return ContactResponse.builder()
                .contact(ContactData.builder()
                        .primaryContactId(primaryContact.getId())
                        .emails(emails)
                        .phoneNumbers(phoneNumbers)
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
