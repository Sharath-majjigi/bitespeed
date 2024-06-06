package com.bitespeed.assignment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactData {

    @JsonProperty("primary_contact_id")
    Long primaryContactId;

    @JsonProperty("emails")
    List<String> emails;

    @JsonProperty("phone_numbers")
    List<String> phoneNumbers;

    @JsonProperty("secondary_contact_ids")
    List<Long> secondaryContactIds;
}
