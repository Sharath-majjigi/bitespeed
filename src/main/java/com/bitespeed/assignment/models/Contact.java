package com.bitespeed.assignment.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contact extends BaseEntity{

    String phoneNumber;

    String email;

    Long linkedId;

    @Enumerated(EnumType.STRING)
    LinkPrecedence linkPrecedence;
}
