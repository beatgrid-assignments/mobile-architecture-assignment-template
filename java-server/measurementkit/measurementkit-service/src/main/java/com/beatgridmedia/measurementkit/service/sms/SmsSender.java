package com.beatgridmedia.measurementkit.service.sms;

/**
 * Sends SMS messages.
 * <p>
 * The default implementation ({@link LoggingSmsSender}) only logs the message, which is what you want for local
 * development. Selected with the property {@code measurementkit.sms.provider}.
 * <p>
 * TODO(assignment): which service (and how is it set up) sends the actual SMS messages in production?
 */
public interface SmsSender {

    /**
     * @param phoneNumber phone number in E.164 format.
     * @param message     the message text.
     */
    void send(String phoneNumber, String message);
}
