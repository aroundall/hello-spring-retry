package org.amuji.hellospringcloudretry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("r4j-retry")
public class BaiduGatewayRetryTest {
    @Autowired
    RestTemplate rest;

    @Autowired
    BaiduGateway gateway;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(rest);
    }

    @Test
    void when_the_access_failed_Spring_should_retry() throws Exception {
        mockServer.expect(times(3), requestTo(new URI("https://www.baidu.com")))
                .andExpect(method(GET))
                .andRespond(withStatus(GATEWAY_TIMEOUT)
                        .contentType(TEXT_PLAIN)
                        .body("Gateway timeout, please retry..."));

        ResponseEntity<String> resp = gateway.homePage();
        mockServer.verify();
        assertThat(resp.getStatusCodeValue()).isEqualTo(599);
    }
}
