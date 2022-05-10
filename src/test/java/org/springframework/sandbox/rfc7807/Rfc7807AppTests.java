package org.springframework.sandbox.rfc7807;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Rfc7807AppTests {

	@LocalServerPort
	private int port;

	private WebClient webClient;

	@BeforeEach
	void setUp(@Autowired WebClient.Builder webClientBuilder) {
		String baseUrl = "http://localhost:" + this.port;
		this.webClient = webClientBuilder.baseUrl(baseUrl).build();
	}

	@Test
	void missingRequestParamWithWebClient() {
		this.webClient.get().uri("/greeting")
				.retrieve()
				.bodyToMono(String.class)
				.doOnNext(System.out::println)
				.onErrorResume(WebClientResponseException.class, ex -> {
					System.out.println(ex.getResponseBodyAs(ExtendedProblemDetail.class));
					return Mono.empty();
				})
				.block();
	}

}
