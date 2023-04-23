package com.dataportal.dataportal.repository;

import com.dataportal.dataportal.entity.UserInfoContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoContactRepository extends JpaRepository<UserInfoContact, String> {

    Optional<UserInfoContact> findByUserInfoUid(String userInfoUid);
}
