package com.dataportal.dataportal.model.apiKey;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "api_keys")
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    @Column
    private String userUid;

    @Column
    private String key;

}
