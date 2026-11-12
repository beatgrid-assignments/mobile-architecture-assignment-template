package com.beatgridmedia.measurementkit.service.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Development implementation: writes the SMS to the log instead of sending it.
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "measurementkit.sms.provider", havingValue = "log", matchIfMissing = true)
public class LoggingSmsSender implements SmsSender {

    @Override
    public void send(String phoneNumber, String message) {
        log.info("SMS to {}: {}", phoneNumber, message);
    }
}
