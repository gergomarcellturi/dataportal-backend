package com.dataportal.dataportal.model.auth;

import lombok.Data;

import java.time.Instant;

@Data
public class AuthStatus {
    private LoggedInStatus loggedInStatus;
    private Instant lastLoggedIn;
}
