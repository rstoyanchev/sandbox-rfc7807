package org.springframework.sandbox.rfc7807;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Rfc7807AppTests {

	private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE = new ParameterizedTypeReference<>() {};

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
		testRequestParameterNotPresent(url ->
				this.webClient.get().uri("/greeting").retrieve().bodyToMono(MAP_TYPE).block());
	}

	@Test
	void missingRequestParamWithRestTemplate() {
		testRequestParameterNotPresent(url ->
				this.restTemplate.exchange(RequestEntity.get(url).build(), MAP_TYPE));
	}

	private static void testRequestParameterNotPresent(Consumer<String> requestExecutor) {

		Map<String, Object> expected = new LinkedHashMap<>();
		expected.put("type", "about:blank");
		expected.put("title", "Bad Request");
		expected.put("status", 400);
		expected.put("detail", "Required parameter 'name' is missing.");
		expected.put("instance", "/greeting");
		expected.put("host", "sandbox-rfc7807.spring.io");

		try {
			requestExecutor.accept("/greeting");
		}
		catch (WebClientResponseException ex) {
			Map<String, Object> actual = ex.getResponseBodyAs(MAP_TYPE);
			assertThat(actual).containsExactlyEntriesOf(expected);
		}
		catch (RestClientResponseException ex) {
			Map<String, Object> actual = ex.getResponseBodyAs(MAP_TYPE);
			assertThat(actual).containsExactlyEntriesOf(expected);
		}
	}

}
