package com.bitespeed.assignment.others;

import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Utils {

    public static <T> T getOrDefault(Supplier<T> codeBlock, T defaultValue) {
        try {
            return codeBlock.get();
        } catch (Exception ex) {
            return defaultValue;
        }
    }

}
