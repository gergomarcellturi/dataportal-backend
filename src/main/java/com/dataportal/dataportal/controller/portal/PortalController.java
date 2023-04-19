package com.dataportal.dataportal.controller.portal;

import com.dataportal.dataportal.model.common.FileInitRequest;
import com.dataportal.dataportal.model.common.Response;
import com.dataportal.dataportal.model.datastorage.Metadata;
import com.dataportal.dataportal.service.portal.PortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "portal")
public class PortalController {

    @Autowired
    private PortalService portalService;


    @CrossOrigin
    @PostMapping("/initupload")
    public Response<Metadata> initUpload(@RequestBody FileInitRequest initRequest) {
        return new Response<>(this.portalService.initRequest(initRequest));
    }

}
