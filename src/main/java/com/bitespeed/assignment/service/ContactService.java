package com.bitespeed.assignment.service;

import com.bitespeed.assignment.models.Contact;
import com.bitespeed.assignment.models.LinkPrecedence;
import com.bitespeed.assignment.repository.ContactRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public Map<String, Object> identifyContact(String email, String phoneNumber) {
        List<Contact> contacts = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);
        if (contacts.isEmpty()) {
            Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence(LinkPrecedence.PRIMARY);
            contactRepository.save(newContact);
            return createResponse(newContact, new ArrayList<>());
        }

        Contact primaryContact = contacts.stream()
                .filter(contact -> contact.getLinkPrecedence() == LinkPrecedence.PRIMARY)
                .findFirst()
                .orElse(contacts.get(0));

        List<Long> secondaryContactIds = getSecondaryContactIds(contacts, primaryContact);

        if (contacts.stream().noneMatch(contact -> contact.getId().equals(primaryContact.getId()))) {
            primaryContact.setEmail(email);
            primaryContact.setPhoneNumber(phoneNumber);
            contactRepository.save(primaryContact);
        }

        return createResponse(primaryContact, secondaryContactIds);
    }

    private static List<Long> getSecondaryContactIds(List<Contact> contacts, Contact primaryContact) {
        Set<String> emails = new HashSet<>();
        Set<String> phoneNumbers = new HashSet<>();
        List<Long> secondaryContactIds = new ArrayList<>();

        for (Contact contact : contacts) {
            if (contact.getEmail() != null) emails.add(contact.getEmail());
            if (contact.getPhoneNumber() != null) phoneNumbers.add(contact.getPhoneNumber());
            if (!contact.getId().equals(primaryContact.getId())) {
                secondaryContactIds.add(contact.getId());
            }
        }
        return secondaryContactIds;
    }

    private Map<String, Object> createResponse(Contact primaryContact, List<Long> secondaryContactIds) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> contactData = new HashMap<>();
        contactData.put("primaryContactId", primaryContact.getId());
        contactData.put("emails", Collections.singletonList(primaryContact.getEmail()));
        contactData.put("phoneNumbers", Collections.singletonList(primaryContact.getPhoneNumber()));
        contactData.put("secondaryContactIds", secondaryContactIds);
        response.put("contact", contactData);
        return response;
    }
}
