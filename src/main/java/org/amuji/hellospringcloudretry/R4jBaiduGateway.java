package org.amuji.hellospringcloudretry;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class R4jBaiduGateway {
    private RestTemplate rest;

    @Autowired
    public R4jBaiduGateway(RestTemplate rest) {
        this.rest = rest;
    }

    @Retry(name = "homePage", fallbackMethod = "fallbackAfterRetry")
    public ResponseEntity<String> homePage() {
        return this.rest.getForEntity("https://www.baidu.com", String.class);
    }

    public ResponseEntity<String> fallbackAfterRetry(Throwable ex) {
        return ResponseEntity.status(599).body("all retries have exhausted");
    }
}
