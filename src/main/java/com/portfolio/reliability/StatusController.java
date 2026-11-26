package com.portfolio.reliability;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatusController {
    private final AtomicLong requests = new AtomicLong();

    @GetMapping("/status")
    public Map<String, Object> status() {
        long requestNumber = requests.incrementAndGet();
        return Map.of(
            "service", "reliable-delivery-platform",
            "status", "UP",
            "message", "Application is responding normally",
            "requestNumber", requestNumber,
            "timestamp", Instant.now().toString()
        );
    }

    @GetMapping("/version")
    public Map<String, String> version() {
        return Map.of("application", "reliable-delivery-platform", "version", "1.0.0");
    }
}
