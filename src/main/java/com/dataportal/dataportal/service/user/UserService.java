package com.dataportal.dataportal.service.user;

import com.dataportal.dataportal.entity.User;
import com.dataportal.dataportal.exception.ApplicationException;
import com.dataportal.dataportal.model.user.UserStatus;
import com.dataportal.dataportal.repository.user.UserRepository;
import com.dataportal.dataportal.service.auth.AuthService;
import com.dataportal.dataportal.service.base.BaseService;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;


    public User getByAuthUid(final String authUid) {
        return this.userRepository.findByAuthUid(authUid).orElseThrow(
                () -> new ApplicationException(String.format("User not found with AuthId: %s", authUid)));
    }

    public User getByAuthUid(final FirebaseToken firebaseToken) {
        Optional<User> optional = this.userRepository.findByAuthUid(firebaseToken.getUid());

        if (optional.isPresent()) {
            return optional.get();
        } else {
            User user = new User();
            user.setAuthUid(firebaseToken.getUid());
            user.setEmail(firebaseToken.getEmail());
            user.setUsername(firebaseToken.getName());
            user.setStatus(UserStatus.ACTIVE);
            user.setCreatedAt(Instant.now());
            user.setLastModified(Instant.now());
            user.setAuthProvider(authService.getAuthProviderFromToken(firebaseToken));
            return this.createUser(user);
        }

    }

    public User createUser(final User user) {
      return this.userRepository.save(user);
    };

    public User getUserByUid(final String userUid) {
        return this.userRepository.findById(userUid).orElseThrow(
                () -> new ApplicationException(String.format("User not found with Uid: %s", userUid)));
    }

    public User getLimitedUserByUid(final String userUid) {
        User user = getUserByUid(userUid);
        User responseUser = new User();
        responseUser.setUsername(user.getUsername());
        responseUser.setUid(user.getUid());
        responseUser.setEmail(user.getEmail());
        return responseUser;
    }


}
