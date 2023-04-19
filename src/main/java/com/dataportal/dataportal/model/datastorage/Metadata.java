package com.dataportal.dataportal.model.datastorage;


import lombok.Data;

import java.time.Instant;

@Data
public class Metadata {

    private String uid;

    private String userUid;

    private String filename;

    private Long size;

    private FileType type;

    private Instant createdAt;

    private Instant lastModified;

    private DatasourceStatus status;

    private Instant datePublished;

    private Instant dateDeleted;
}
