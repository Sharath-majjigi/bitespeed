package com.bitespeed.assignment.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "contact", indexes = {
        @Index(columnList = "email"),
        @Index(columnList = "phone_number")
})
public class Contact extends BaseEntity {

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "email")
    String email;

    @Column(name = "linked_id")
    Long linkedId;

    @Column(name = "link_precedence")
    @Enumerated(EnumType.STRING)
    LinkPrecedence linkPrecedence;
}
