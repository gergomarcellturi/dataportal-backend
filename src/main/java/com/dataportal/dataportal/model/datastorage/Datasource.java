package com.dataportal.dataportal.model.datastorage;

import lombok.Data;

import java.time.Instant;

@Data
public class Datasource {

    private String uid;

    private Metadata metadata;

    private byte[] data;

    private Instant createdAt;

    private Instant lastModified;

}
