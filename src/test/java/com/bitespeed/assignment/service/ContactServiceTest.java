package com.bitespeed.assignment.service;

import com.bitespeed.assignment.dto.request.ContactRequest;
import com.bitespeed.assignment.dto.response.ContactResponse;
import com.bitespeed.assignment.models.Contact;
import com.bitespeed.assignment.models.LinkPrecedence;
import com.bitespeed.assignment.repository.ContactRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testIdentifyWithNewContact() throws Exception {
        ContactRequest request = ContactRequest.builder()
                .email("newuser@example.com")
                .phoneNumber("12345")
                .build();

        when(contactRepository.findByEmailOrPhoneNumber(anyString(), anyString()))
                .thenReturn(Collections.emptyList());

        when(contactRepository.save(any(Contact.class)))
                .thenAnswer(invocation -> {
                    Contact savedContact = invocation.getArgument(0);
                    savedContact.setId(1L);
                    return savedContact;
                });

        mockMvc.perform(post("/identify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    ContactResponse contactResponse = objectMapper.readValue(response, ContactResponse.class);
                    Assertions.assertEquals(1, contactResponse.getContact().getEmails().size());
                    Assertions.assertEquals(1, contactResponse.getContact().getPhoneNumbers().size());
                    Assertions.assertEquals(0, contactResponse.getContact().getSecondaryContactIds().size());
                });
    }

    @Test
    void testIdentifyWithExistingContact() throws Exception {
        Contact primaryContact = new Contact();
        primaryContact.setId(1L);
        primaryContact.setEmail("existinguser@example.com");
        primaryContact.setPhoneNumber("54321");
        primaryContact.setLinkPrecedence(LinkPrecedence.PRIMARY);

        when(contactRepository.findByEmailOrPhoneNumber(anyString(), anyString()))
                .thenReturn(Collections.singletonList(primaryContact));

        ContactRequest request = ContactRequest.builder()
                .email("existinguser@example.com")
                .phoneNumber("54321")
                .build();

        mockMvc.perform(post("/identify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    ContactResponse contactResponse = objectMapper.readValue(response, ContactResponse.class);
                    Assertions.assertEquals(1, contactResponse.getContact().getEmails().size());
                    Assertions.assertEquals(1, contactResponse.getContact().getPhoneNumbers().size());
                    Assertions.assertEquals(0, contactResponse.getContact().getSecondaryContactIds().size());
                });
    }
}