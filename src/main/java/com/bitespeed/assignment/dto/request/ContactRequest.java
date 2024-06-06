package com.bitespeed.assignment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactRequest {

    @JsonProperty("email")
    String email;

    @JsonProperty("phone_number")
    String phoneNumber;
}
