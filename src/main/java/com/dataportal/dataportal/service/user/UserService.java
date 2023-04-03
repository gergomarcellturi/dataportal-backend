package com.dataportal.dataportal.service.user;

import com.dataportal.dataportal.entity.User;
import com.dataportal.dataportal.exception.ApplicationException;
import com.dataportal.dataportal.repository.user.UserRepository;
import com.dataportal.dataportal.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;


    public User getByAuthUid(final String authUid) {
        return this.userRepository.findByAuthUid(authUid).orElseThrow(
                () -> new ApplicationException(String.format("User not found with AuthId: %s", authUid)));
    }


}
