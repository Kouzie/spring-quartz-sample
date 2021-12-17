package com.example.quartz.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Value("${test.string}")
    private String testString;

    public void test() {
        System.out.println(testString);
    }
}
