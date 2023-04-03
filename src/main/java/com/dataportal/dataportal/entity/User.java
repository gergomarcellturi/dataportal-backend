package com.dataportal.dataportal.entity;

import com.dataportal.dataportal.model.user.AuthProvider;
import com.dataportal.dataportal.model.user.UserStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    @Column
    private String authUid;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column
    private Instant createdAt;

    @Column
    private Instant lastModified;
}
