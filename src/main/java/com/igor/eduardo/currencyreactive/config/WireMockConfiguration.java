package com.igor.eduardo.currencyreactive.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.cloud.contract.wiremock.WireMockSpring.options;
import static org.springframework.http.HttpStatus.OK;

@Configuration
public class WireMockConfiguration {

    @Bean
    public WireMockServer wiremock() throws IOException {
        WireMockServer mockServer = new WireMockServer(options().port(9001));
        mockServer.stubFor(get(urlPathEqualTo("/users"))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody("""
                        {"name":"Igor"}""")
                        ));


        mockServer.stubFor(post("/bc")
                .willReturn(ok("Success response body")));

        mockServer.stubFor(post("/bc-error")
                .willReturn(aResponse().withStatus(429).withBody("Error response body")));
        mockServer.start();
        return mockServer;
    }
}
