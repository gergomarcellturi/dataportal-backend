package com.dataportal.dataportal.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

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

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] profilePicture;

    @Column
    private String info;

    @Column
    private Instant lastModified;

}
