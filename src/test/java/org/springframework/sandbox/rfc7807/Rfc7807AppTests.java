package org.springframework.sandbox.rfc7807;

import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Rfc7807AppTests {

	@LocalServerPort
	private int port;

	private WebClient webClient;

	private RestTemplate restTemplate;

	@BeforeEach
	void setUp() {
		String baseUrl = "http://localhost:" + this.port;
		this.webClient = WebClient.builder().baseUrl(baseUrl).build();
		this.restTemplate = new RestTemplate();
		this.restTemplate.setUriTemplateHandler(new RootUriTemplateHandler(baseUrl));
	}

	@Test
	void missingRequestParamWithWebClient() {
		this.webClient.get().uri("/greeting")
				.retrieve()
				.bodyToMono(String.class)
				.doOnNext(System.out::println)
				.onErrorResume(WebClientResponseException.class, ex -> {
					ExtendedProblemDetail body = ex.getResponseBodyAs(ExtendedProblemDetail.class);
					System.out.println(format(ex.getStatusCode(), ex.getHeaders(), body));
					return Mono.empty();
				})
				.block();
	}

	@Test
	void missingRequestParamWithRestTemplate() {
		try {
			String result = this.restTemplate.getForObject("/greeting", String.class);
			System.out.println(result);
		}
		catch (RestClientResponseException ex) {
			ExtendedProblemDetail body = ex.getResponseBodyAs(ExtendedProblemDetail.class);
			System.out.println(format(ex.getStatusCode(), ex.getResponseHeaders(), body));
		}
	}

	private String format(HttpStatusCode statusCode, @Nullable HttpHeaders headers, ExtendedProblemDetail body) {
		return "\nHTTP Status " + statusCode + "\n\n" +
				(headers != null ? headers : HttpHeaders.EMPTY).entrySet().stream()
						.map(entry -> entry.getKey() + ":" + entry.getValue())
						.collect(Collectors.joining("\n")) +
				"\n\n" + body;
	}


}
