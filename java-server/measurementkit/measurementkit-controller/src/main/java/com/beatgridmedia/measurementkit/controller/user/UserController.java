package com.beatgridmedia.measurementkit.controller.user;

import com.beatgridmedia.measurementkit.service.common.NotImplementedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Example of an endpoint that requires an authenticated user (currently always 401, see {@code SecurityConfiguration}).
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    /**
     * TODO(assignment): how does this endpoint learn who the authenticated user is?
     */
    @GetMapping("/me")
    public void getCurrentUser() {
        throw new NotImplementedException("getCurrentUser");
    }
}
