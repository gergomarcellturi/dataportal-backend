package com.dataportal.dataportal.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_info")
@Data
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    @Column
    private String userUid;

    @Column()
    private String profilePicture;

    @Column
    private String info;

    @Column
    private Instant lastModified;
}
