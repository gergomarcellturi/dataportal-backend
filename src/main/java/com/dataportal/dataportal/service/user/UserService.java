package com.dataportal.dataportal.service.user;

import com.dataportal.dataportal.entity.User;
import com.dataportal.dataportal.exception.ApplicationException;
import com.dataportal.dataportal.model.apiKey.ApiKey;
import com.dataportal.dataportal.model.user.UserStatus;
import com.dataportal.dataportal.repository.ApiKeyRepository;
import com.dataportal.dataportal.repository.UserRepository;
import com.dataportal.dataportal.service.auth.AuthService;
import com.dataportal.dataportal.service.base.BaseService;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

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
        User savedUser = this.userRepository.save(user);
        createApiKeyForUser(savedUser);
        return savedUser;
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

    public ApiKey createApiKeyForUser(User user) {
        String apiKeyString = generateApiKey(32);
        ApiKey apiKey = new ApiKey();
        apiKey.setKey(apiKeyString);
        apiKey.setUserUid(user.getUid());
        return apiKeyRepository.save(apiKey);
    }

    public String generateApiKey(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public ApiKey getApiKeyByKey(final String apikey) {
        return this.apiKeyRepository.findByKey(apikey).orElseThrow(
                () -> new ApplicationException(String.format("ApiKey not found with key: %s", apikey)));
    }

    public String getUserUidByApiKey(final String apikey) {
        ApiKey key = getApiKeyByKey(apikey);
        return key.getUserUid();
    }

    public ApiKey getApiKeyByUserUid(final String userUid) {
        User user = getUserByUid(userUid);
        Optional<ApiKey> optional = this.apiKeyRepository.findByUserUid(user.getUid());
        return optional.orElseGet(() -> createApiKeyForUser(user));
    }

    public String getCurrentApiKey() {
        User currUser = this.getCurrentUser();
        ApiKey key = getApiKeyByUserUid(currUser.getUid());
        return key.getKey();
    }

}
