package org.springframework.sandbox.rfc7807;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

@ControllerAdvice
public class WebFluxExceptionHandler extends ResponseEntityExceptionHandler {

	@Value("${hostname}")
	private String host;


	@Override
	protected Mono<ResponseEntity<Object>> createResponseEntity(
			Object body, HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {

		if (body instanceof ProblemDetail detail) {
			detail.setProperty("host", this.host);
		}

		return super.createResponseEntity(body, headers, status, exchange);
	}

}
