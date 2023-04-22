package com.dataportal.dataportal.service;

import com.dataportal.dataportal.entity.User;
import com.dataportal.dataportal.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public abstract class BaseService {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    private FirebaseAuth firebaseAuth;

    @Autowired
    private UserService userService;

    public FirebaseToken getAuthToken() {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Extract the token from the header
            try {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
                if (decodedToken.getUid() != null) {
                    return decodedToken;
                }
                return null;
            } catch (FirebaseAuthException e) {
                return null;
            }
        }
        return null;
    }

    public User getCurrentUser() {
        return this.userService.getByAuthUid(this.getAuthToken());
    }
}
