package com.beatgridmedia.measurementkit.controller.authentication;

import com.beatgridmedia.measurementkit.controller.authentication.dto.CompletePhoneNumberLoginRequest;
import com.beatgridmedia.measurementkit.controller.authentication.dto.StartPhoneNumberLoginRequest;
import com.beatgridmedia.measurementkit.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login with phone number.
 * <p>
 * TODO(assignment): placeholder endpoints (currently all respond with 501 Not Implemented). Paths, request and
 *  response bodies, status codes and headers are all part of the design and may be changed freely.
 */
@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/phone-number/start")
    public void startPhoneNumberLogin(@RequestBody StartPhoneNumberLoginRequest request) {
        authenticationService.startPhoneNumberLogin(request.phoneNumber());
    }

    @PostMapping("/phone-number/complete")
    public void completePhoneNumberLogin(@RequestBody CompletePhoneNumberLoginRequest request) {
        authenticationService.completePhoneNumberLogin();
    }

    @PostMapping("/refresh")
    public void refresh() {
        authenticationService.refresh();
    }

    @PostMapping("/logout")
    public void logout() {
        authenticationService.logout();
    }
}
