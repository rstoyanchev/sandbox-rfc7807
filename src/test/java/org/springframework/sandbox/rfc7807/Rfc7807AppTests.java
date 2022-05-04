package org.springframework.sandbox.rfc7807;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureMockMvc
class Rfc7807AppTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void missingRequestParam() {
		this.webTestClient.get().uri("/greeting").exchange().expectBody().consumeWith(System.out::println);
	}

}
