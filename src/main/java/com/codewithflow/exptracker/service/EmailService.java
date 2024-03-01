package com.codewithflow.exptracker.service;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

}
