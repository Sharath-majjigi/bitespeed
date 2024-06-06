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
            return mapper.getContactResponse(newContact,new ArrayList<>());
        }

        Contact primaryContact = contacts.stream()
                .filter(contact -> contact.getLinkPrecedence() == LinkPrecedence.PRIMARY)
                .findFirst()
                .orElse(contacts.get(0));

        List<Long> secondaryContactIds = getSecondaryContactIds(contacts, primaryContact);

        if (contacts.stream().noneMatch(contact -> contact.getId().equals(primaryContact.getId()))) {
            primaryContact.setEmail(contactRequest.getEmail());
            primaryContact.setPhoneNumber(contactRequest.getPhoneNumber());
            contactRepository.save(primaryContact);
        }

        return mapper.getContactResponse(primaryContact, secondaryContactIds);
    }

    private List<Long> getSecondaryContactIds(List<Contact> contacts, Contact primaryContact) {
//        Set<String> emails = new HashSet<>();
//        Set<String> phoneNumbers = new HashSet<>();
        List<Long> secondaryContactIds = new ArrayList<>();

        for (Contact contact : contacts) {
//            if (contact.getEmail() != null) emails.add(contact.getEmail());
//            if (contact.getPhoneNumber() != null) phoneNumbers.add(contact.getPhoneNumber());
            if (!contact.getId().equals(primaryContact.getId())) {
                secondaryContactIds.add(contact.getId());
            }
        }
        return secondaryContactIds;
    }
}
