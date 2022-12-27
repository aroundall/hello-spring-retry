package org.amuji.hellospringcloudretry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class R4jBaiduGatewayTest {
    @Autowired
    R4jBaiduGateway gateway;

    @Test
    void access_home_page() {
        ResponseEntity<String> resp = gateway.homePage();
        assertThat(resp.getBody()).hasSizeGreaterThan(2);
    }
}