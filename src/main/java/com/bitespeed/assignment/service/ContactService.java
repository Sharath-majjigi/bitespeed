package com.bitespeed.assignment.service;

import com.bitespeed.assignment.dto.request.ContactRequest;
import com.bitespeed.assignment.dto.response.ContactResponse;
import com.bitespeed.assignment.exception.ContactException;
import com.bitespeed.assignment.mapper.ContactMapper;
import com.bitespeed.assignment.models.Contact;
import com.bitespeed.assignment.models.LinkPrecedence;
import com.bitespeed.assignment.repository.ContactRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactService {

    ContactRepository contactRepository;
    ContactMapper mapper;

    public ContactResponse identifyContact(ContactRequest contactRequest) {
        if (contactRequest.getEmail().isEmpty() && contactRequest.getPhoneNumber().isEmpty()) {
            throw new ContactException(HttpStatus.BAD_REQUEST,"Either email or phoneNumber must be provided");
        }

        List<Contact> contacts = contactRepository.findByEmailOrPhoneNumber(
                contactRequest.getEmail(), contactRequest.getPhoneNumber());

        if (contacts.isEmpty()) {
            Contact newContact = mapper.buildPrimaryContact(contactRequest);
            newContact = contactRepository.save(newContact);
            return mapper.getContactResponse(newContact, new ArrayList<>());
        }

        Contact primaryContact = mapper.getPrimaryContact(contacts);

        // If the contact request has new email or phone number information
        boolean result = shouldCreateSecondaryContact(contacts, contactRequest);
        if (result) {
            Contact newSecondaryContact = mapper.buildSecondaryContact(contactRequest, primaryContact);
            contactRepository.save(newSecondaryContact);
            contacts.add(newSecondaryContact);
        }

        primaryContact = updatePrimaryContactIfNeeded(contacts, primaryContact);
        List<Contact> secondaryContacts = getSecondaryContacts(contacts, primaryContact);

        return mapper.getContactResponse(primaryContact, secondaryContacts);
    }

    private boolean shouldCreateSecondaryContact(List<Contact> contacts, ContactRequest contactRequest) {
        String requestEmail = contactRequest.getEmail();
        String requestPhoneNumber = contactRequest.getPhoneNumber();

        return contacts.stream().anyMatch(contact -> {
            String contactEmail = contact.getEmail();
            String contactPhoneNumber = contact.getPhoneNumber();

            boolean sameEmailDifferentPhone = requestEmail != null && requestEmail.equals(contactEmail) &&
                    (requestPhoneNumber == null || !requestPhoneNumber.equals(contactPhoneNumber));
            boolean samePhoneDifferentEmail = requestPhoneNumber != null && requestPhoneNumber.equals(contactPhoneNumber) &&
                    (requestEmail == null || !requestEmail.equals(contactEmail));

            return sameEmailDifferentPhone || samePhoneDifferentEmail;
        });
    }

    private Contact updatePrimaryContactIfNeeded(List<Contact> contacts, Contact primaryContact) {
        for (Contact contact : contacts) {
            if (contact.getLinkPrecedence() == LinkPrecedence.PRIMARY && !contact.equals(primaryContact)) {
                if (contact.getCreatedAt().before(primaryContact.getCreatedAt())) {
                    primaryContact.setLinkPrecedence(LinkPrecedence.SECONDARY);
                    primaryContact.setLinkedId(contact.getId());
                    contactRepository.save(primaryContact);
                    primaryContact = contact;
                } else {
                    contact.setLinkPrecedence(LinkPrecedence.SECONDARY);
                    contact.setLinkedId(primaryContact.getId());
                    contactRepository.save(contact);
                }
            }
        }
        return primaryContact;
    }

    private List<Contact> getSecondaryContacts(List<Contact> contacts, Contact primaryContact) {
        return contacts.stream()
                .filter(contact -> !contact.equals(primaryContact) && contact.getLinkPrecedence() == LinkPrecedence.SECONDARY)
                .collect(Collectors.toList());
    }
}
