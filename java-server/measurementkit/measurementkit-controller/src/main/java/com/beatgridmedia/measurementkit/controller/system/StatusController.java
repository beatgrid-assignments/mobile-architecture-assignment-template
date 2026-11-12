package com.beatgridmedia.measurementkit.controller.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Unauthenticated status endpoint, used by the app to verify connectivity (see {@code ServerStatusPlugin}).
 */
@RestController
@RequestMapping("/api/v1/status")
public class StatusController {

    private final String version;

    public StatusController(@Value("${measurementkit.version}") String version) {
        this.version = version;
    }

    @GetMapping
    public ServerStatus getStatus() {
        return new ServerStatus("UP", version, System.currentTimeMillis());
    }
}
