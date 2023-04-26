package com.dataportal.dataportal.repository;

import com.dataportal.dataportal.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByAuthUid(final String authUid);
}
