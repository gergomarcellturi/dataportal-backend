package com.dataportal.dataportal.controller.auth;

import com.dataportal.dataportal.entity.User;
import com.dataportal.dataportal.model.common.Response;
import com.dataportal.dataportal.service.AuthService;
import com.dataportal.dataportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    public AuthController() {

    }

    @CrossOrigin
    @PostMapping("/login")
    public Response<String> login() throws ExecutionException, InterruptedException {
        return this.authService.login();
    }


    @PostMapping("/logout")
    public Response<String> logout(@RequestParam final String authUid) throws ExecutionException, InterruptedException {
        return this.authService.logout(authUid);
    }

    @CrossOrigin
    @GetMapping("current")
    public Response<User> getCurrentPortalUser() {
        return new Response<>(this.authService.getCurrentUser());
    }

    @CrossOrigin
    @GetMapping("current/key")
    public Response<String> getCurrentApiKey() {
        return Response.ok( this.userService.getCurrentApiKey());
    }

    @CrossOrigin
    @GetMapping("/user/{userUid}")
    public Response<User> getUserByUid(@PathVariable String userUid) {
        return new Response<>(this.userService.getLimitedUserByUid(userUid));
    }

    @CrossOrigin
    @GetMapping("apikey")
    public Response<String> getUserUidByApiKey(@RequestParam String apikey) {
        return Response.ok(this.userService.getUserUidByApiKey(apikey));
    }
}
