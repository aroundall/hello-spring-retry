package org.amuji.hellospringcloudretry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

@LoadBalancerClient(name = "baidu-client")
public class SpringLoadBalancerBaiduGateway {
    private WebClient client;

    @Autowired
    public SpringLoadBalancerBaiduGateway(WebClient.Builder builder) {
        this.client = builder.build();
    }

    public String homePage() {
        return this.client.get().uri("http://baidu-service/")
                .retrieve()
                .toEntity(String.class)
                .block()
                .getBody();
    }

    public ResponseEntity<String> fallbackAfterRetry(RuntimeException ex) {
        return ResponseEntity.status(599).body("all retries have exhausted");
    }
}
