package com.dataportal.dataportal.model.common;

import com.dataportal.dataportal.model.datastorage.FileType;
import lombok.Data;

@Data
public class FileInitRequest {
    private String filename;
    private FileType fileType;
    private Long fileSize;
}
