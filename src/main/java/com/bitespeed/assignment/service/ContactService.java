package com.bitespeed.assignment.service;

import com.bitespeed.assignment.dto.request.ContactRequest;
import com.bitespeed.assignment.dto.response.ContactResponse;
import com.bitespeed.assignment.mapper.ContactMapper;
import com.bitespeed.assignment.models.Contact;
import com.bitespeed.assignment.models.LinkPrecedence;
import com.bitespeed.assignment.repository.ContactRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ContactService {

    ContactRepository contactRepository;
    ContactMapper mapper;

    public ContactResponse identifyContact(ContactRequest contactRequest) {
        List<Contact> contacts = contactRepository.findByEmailOrPhoneNumber(
                contactRequest.getEmail(), contactRequest.getPhoneNumber());

        if (contacts.isEmpty()) {
            Contact newContact = mapper.buildPrimaryContact(contactRequest);
            contactRepository.save(newContact);
            return mapper.getContactResponse(newContact, new ArrayList<>());
        }

        Contact primaryContact = mapper.getPrimaryContact(contacts);

        if (!isContactInfoExists(contacts, contactRequest)) {
            Contact newSecondaryContact = mapper.buildSecondaryContact(contactRequest, primaryContact);
            contactRepository.save(newSecondaryContact);
            contacts.add(newSecondaryContact);
        }

        primaryContact = updatePrimaryContactIfNeeded(contacts, primaryContact);
        List<Long> secondaryContactIds = getSecondaryContactIds(contacts, primaryContact);

        return mapper.getContactResponse(primaryContact, secondaryContactIds);
    }

    private boolean isContactInfoExists(List<Contact> contacts, ContactRequest contactRequest) {
        return contacts.stream().anyMatch(contact ->
                contact.getEmail().equals(contactRequest.getEmail()) ||
                contact.getPhoneNumber().equals(contactRequest.getPhoneNumber()));
    }

    private Contact updatePrimaryContactIfNeeded(List<Contact> contacts, Contact primaryContact) {
        for (Contact contact : contacts) {
            if (contact.getLinkPrecedence() == LinkPrecedence.PRIMARY && !contact.equals(primaryContact)) {
                primaryContact.setEmail(contact.getEmail());
                primaryContact.setPhoneNumber(contact.getPhoneNumber());
                contactRepository.save(primaryContact);
            }
        }
        return primaryContact;
    }

    private List<Long> getSecondaryContactIds(List<Contact> contacts, Contact primaryContact) {
        return contacts.stream()
                .filter(contact -> !contact.equals(primaryContact) && contact.getLinkPrecedence() == LinkPrecedence.SECONDARY)
                .map(Contact::getId)
                .collect(Collectors.toList());
    }


}
