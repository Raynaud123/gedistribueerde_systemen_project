package com.example.matchingservice;

import java.time.LocalDateTime;

public class Token {
    private String token;
    private String status;

    public Token(String token, String status, LocalDateTime date) {
        this.token = token;
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
