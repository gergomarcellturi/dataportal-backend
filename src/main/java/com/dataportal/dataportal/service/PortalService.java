package com.dataportal.dataportal.service;

import com.dataportal.dataportal.model.common.FileInitRequest;
import com.dataportal.dataportal.model.common.Response;
import com.dataportal.dataportal.model.datastorage.DatasourceStatus;
import com.dataportal.dataportal.model.datastorage.Metadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class PortalService extends BaseService {

    @Value("${storage.url}")
    private String storageUrl;


    public Metadata initRequest(final FileInitRequest fileInitRequest) {
        final Instant now = Instant.now();
        final Metadata metadata = new Metadata();
        metadata.setCreatedAt(now);
        metadata.setLastModified(now);
        metadata.setFilename(fileInitRequest.getFilename());
        metadata.setSize(fileInitRequest.getFileSize());
        metadata.setStatus(DatasourceStatus.NEW);
        metadata.setType(fileInitRequest.getFileType());
        metadata.setUserUid(this.getCurrentUser().getUid());
        metadata.setDateDeleted(null);
        metadata.setDatePublished(null);

        RestTemplate restTemplate = new RestTemplate();

        ParameterizedTypeReference<Response<Metadata>> responseType =
                new ParameterizedTypeReference<>() {};
        ResponseEntity<Response<Metadata>> responseEntity =
                restTemplate.exchange(storageUrl + "/data/create",
                        HttpMethod.POST, new HttpEntity<>(metadata),
                        responseType);
        Response<Metadata> response = responseEntity.getBody();
        assert response != null;
        return response.getData();
    }

}
