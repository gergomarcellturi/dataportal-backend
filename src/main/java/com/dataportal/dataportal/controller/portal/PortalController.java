package com.dataportal.dataportal.controller.portal;

import com.dataportal.dataportal.model.entity.UserInfo;
import com.dataportal.dataportal.model.entity.UserInfoContact;
import com.dataportal.dataportal.model.common.FileInitRequest;
import com.dataportal.dataportal.model.common.Response;
import com.dataportal.dataportal.model.datastorage.Metadata;
import com.dataportal.dataportal.service.PortalService;
import com.dataportal.dataportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "portal")
public class PortalController {

    @Autowired
    private PortalService portalService;

    @Autowired
    private UserService userService;


    @CrossOrigin
    @PostMapping("/initupload")
    public Response<Metadata> initUpload(@RequestBody FileInitRequest initRequest) {
        return new Response<>(this.portalService.initRequest(initRequest));
    }

    @CrossOrigin
    @PutMapping("/userInfo")
    public Response<UserInfo> updateUserInfo(@RequestBody UserInfo userInfo) {
        return new Response<>(this.userService.updateUserInfo(userInfo));
    }

    @CrossOrigin
    @PutMapping("/userInfoContact")
    public Response<UserInfoContact> updateUserInfo(@RequestBody UserInfoContact userInfoContact) {
        return new Response<>(this.userService.updateUserInfoContact(userInfoContact));
    }

    @CrossOrigin
    @GetMapping("/userInfo/user/{userUid}")
    public Response<UserInfo> getUserInfoByUserUid(@PathVariable String userUid) {
        return new Response<>(this.userService.getUserInfoByUserUid(userUid));
    }

    @CrossOrigin
    @GetMapping("/userInfo/info/{userUid}")
    public Response<String> getUserInfoIntroductionByUserUid(@PathVariable String userUid) {
        return Response.ok(this.userService.getUserInfoIntroductionByUserUid(userUid));
    }

    @CrossOrigin
    @GetMapping("/contact/user/{userUid}")
    public Response<UserInfoContact> getUserContactsByUserUid(@PathVariable String userUid) {
        return new Response<>(this.userService.getUserContactsByUserUid(userUid));
    }

}
