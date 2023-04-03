package com.dataportal.dataportal.controller.auth;

import com.dataportal.dataportal.model.auth.AuthStatus;
import com.dataportal.dataportal.model.common.Response;
import com.dataportal.dataportal.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    public AuthController() {

    }

    @CrossOrigin
    @PostMapping("/login")
    public Response<String> login() throws ExecutionException, InterruptedException {
        return this.authService.login();
    }


    @PostMapping("/logout")
    public Response<String> logout() throws ExecutionException, InterruptedException {
        return this.authService.logout();
    }

    @PostMapping("/busy")
    public Response<AuthStatus> busy() {
        return null;
    }

    @PostMapping("/invisible")
    public Response<AuthStatus> invisible() {
        return null;
    }

}
