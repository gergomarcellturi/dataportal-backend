package com.dataportal.dataportal.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_info_contacts")
@Data
public class UserInfoContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    @Column
    private String userInfoUid;

   @Column
    private String github;

   @Column
    private String facebook;

   @Column
    private String email;

   @Column
    private String twitter;

   @Column
    private String website;



//    CONTACT_NAME varchar,
//    CONTACT_VALUE varchar,

}
