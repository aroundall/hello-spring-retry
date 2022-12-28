package org.amuji.hellospringcloudretry;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@ActiveProfiles("spring-lb")
class SpringLoadBalancerBaiduGatewayTest {
    public static WireMockServer wiremock = new WireMockServer(WireMockSpring.options().port(26693));

    @Autowired
    SpringLoadBalancerBaiduGateway gateway;

    @Test
    void name() {
        wiremock.stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(504)
                        .withBody("Gateway timeout, please retry...")
                ));

        try {
            String resp = gateway.homePage();
        } catch (Exception e) {

        }

        wiremock.verify(4, getRequestedFor(urlEqualTo("/")));
    }

    @BeforeAll
    static void setupClass() {
        wiremock.start();
    }

    @AfterEach
    void after() {
        wiremock.resetAll();
    }

    @AfterAll
    static void clean() {
        wiremock.shutdown();
    }
}