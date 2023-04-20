package com.dataportal.dataportal.repository;

import com.dataportal.dataportal.model.apiKey.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {

    Optional<ApiKey> findByKey(final String key);
    Optional<ApiKey> findByUserUid(final String userUid);

}
