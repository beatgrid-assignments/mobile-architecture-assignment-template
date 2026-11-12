package com.beatgridmedia.measurementkit.service.authentication;

import com.beatgridmedia.measurementkit.service.common.NotImplementedException;
import com.beatgridmedia.measurementkit.service.sms.SmsSender;
import com.beatgridmedia.measurementkit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Login with phone number.
 * <p>
 * TODO(assignment): placeholder. Design the flow (and the signatures of these methods); the method split below is
 *  only a starting point and may be changed freely.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final SmsSender smsSender;

    /**
     * First step: the user entered their phone number.
     */
    public void startPhoneNumberLogin(String phoneNumber) {
        throw new NotImplementedException("startPhoneNumberLogin");
    }

    /**
     * Second step: the user entered (or the app auto-filled) the code received by SMS.
     */
    public void completePhoneNumberLogin() {
        throw new NotImplementedException("completePhoneNumberLogin");
    }

    public void refresh() {
        throw new NotImplementedException("refresh");
    }

    public void logout() {
        throw new NotImplementedException("logout");
    }
}
