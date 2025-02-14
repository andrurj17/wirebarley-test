package com.wirebarley.test.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

@BaseTestEnvironment
public class BaseIntTest {

    @Autowired
    protected WebTestClient webTestClient;
}
