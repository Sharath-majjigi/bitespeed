package com.bitespeed.assignment.others;

import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Utils {


    public static <T> T getOrNull(Supplier<T> codeBlock) {
        try {
            return codeBlock.get();
        } catch (Exception ex) {
            return null;
        }
    }

}
