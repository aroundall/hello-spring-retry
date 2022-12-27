package org.amuji.hellospringcloudretry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SpringRetryBaiduGateway {
    private RestTemplate rest;

    @Autowired
    public SpringRetryBaiduGateway(RestTemplate rest) {
        this.rest = rest;
    }

    @Retryable(value = RuntimeException.class)
    public ResponseEntity<String> homePage() {
        return this.rest.getForEntity("https://www.baidu.com", String.class);
    }

    @Recover
    public ResponseEntity<String> fallbackAfterRetry(RuntimeException ex) {
        return ResponseEntity.status(599).body("all retries have exhausted");
    }
}
