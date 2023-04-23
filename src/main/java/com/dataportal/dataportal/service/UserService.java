package com.dataportal.dataportal.service;

import com.dataportal.dataportal.entity.User;
import com.dataportal.dataportal.entity.UserInfo;
import com.dataportal.dataportal.entity.UserInfoContact;
import com.dataportal.dataportal.exception.ApplicationException;
import com.dataportal.dataportal.model.apiKey.ApiKey;
import com.dataportal.dataportal.model.user.UserStatus;
import com.dataportal.dataportal.repository.ApiKeyRepository;
import com.dataportal.dataportal.repository.UserInfoContactRepository;
import com.dataportal.dataportal.repository.UserInfoRepository;
import com.dataportal.dataportal.repository.UserRepository;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoContactRepository userInfoContactRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

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
            return this.createUser(user, firebaseToken.getPicture());
        }

    }

    public User createUser(final User user, final String pictureUrl) {
        User savedUser = this.userRepository.save(user);
        createUserInfoForUser(savedUser, pictureUrl);
        createApiKeyForUser(savedUser);
        return savedUser;
    };

    public void createUserInfoContactForUserInfo(UserInfo userInfo) {
        UserInfoContact contact = new UserInfoContact();
        contact.setFacebook("");
        contact.setGithub("");
        contact.setTwitter("");
        contact.setWebsite("");
        contact.setEmail("");
        contact.setUserInfoUid(userInfo.getUid());
        userInfoContactRepository.save(contact);
    }

    public UserInfo createUserInfoForUser(User user, String profileUrl) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserUid(user.getUid());
        userInfo.setLastModified(Instant.now());
        UserInfo savedUserInfo = this.userInfoRepository.save(userInfo);
        createUserInfoContactForUserInfo(savedUserInfo);
        try {
            savedUserInfo.setProfilePicture(downloadImageAsBase64(profileUrl));
            savedUserInfo.setLastModified(Instant.now());
            return this.userInfoRepository.save(savedUserInfo);
        } catch (IOException ioException) {
            return userInfo;
        }
    }

    public UserInfo updateUserInfo(UserInfo userInfo) {
        userInfo.setLastModified(Instant.now());
        return this.userInfoRepository.save(userInfo);
    }

    public UserInfoContact updateUserInfoContact(UserInfoContact userInfoContact) {
        return this.userInfoContactRepository.save(userInfoContact);
    }

    public UserInfo getUserInfoByUserUid(final String userUid) {
        return  this.userInfoRepository.findByUserUid(userUid).orElseThrow(
                () -> new ApplicationException(String.format("UserInfo not found with UserUid: %s", userUid)));
    }

    public String getUserInfoIntroductionByUserUid(final String userUid) {
        UserInfo userInfo = getUserInfoByUserUid(userUid);
        return userInfo.getInfo();
    }

    public UserInfoContact getUserContactsByUserUid(final String userUid) {
        UserInfo userInfo = getUserInfoByUserUid(userUid);
        return userInfoContactRepository.findByUserInfoUid(userInfo.getUid()).orElseThrow(
                () -> new ApplicationException(String.format("Contact not found with UserUid: %s", userUid)));
    }

    public String downloadImageAsBase64(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String contentType = connection.getContentType();
        String imageFormat = contentType.split("/")[1];

        try (InputStream inputStream = url.openStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
        }

        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
        return "data:image/" + imageFormat + ";base64," + encodedImage;
    }

    public UserInfo getUserInfoForUser(User user) {
        return this.userInfoRepository.findByUserUid(user.getUid()).orElseThrow(
                () -> new ApplicationException(String.format("UserInfo not found with Uid: %s", user.getUid())));
    }

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

    public User updateUserNickname(final String userUid, final String username) {
        User user = getUserByUid(userUid);
        user.setLastModified(Instant.now());
        user.setUsername(username);
        userRepository.save(user);
        return getLimitedUserByUid(userUid);
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
