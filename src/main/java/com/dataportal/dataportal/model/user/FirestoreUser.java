package com.dataportal.dataportal.model.user;

import lombok.Data;

import java.time.Instant;

@Data
public class FirestoreUser {

    public static String ONLINE = "ONLINE";
    public static String OFFLINE = "OFFLINE";

    private Instant lastOnline;
    private String loggedInStatus;
}
