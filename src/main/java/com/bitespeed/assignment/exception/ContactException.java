package com.bitespeed.assignment.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ContactException extends RuntimeException{

    HttpStatus status;
    String msg;

    public ContactException(HttpStatus status, String msg) {
        super();
        this.status = status;
        this.msg = msg;
    }
}
