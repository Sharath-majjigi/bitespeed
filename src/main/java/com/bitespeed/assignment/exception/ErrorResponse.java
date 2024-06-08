package com.bitespeed.assignment.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("timestamp")
    private String timeStamp;

    @JsonProperty("status")
    private String status;

    @JsonProperty("path")
    private String path;
}

