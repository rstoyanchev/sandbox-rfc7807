package org.springframework.sandbox.rfc7807;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebInputException;

@ControllerAdvice
public class WebFluxExceptionHandler {

	@Value("${hostname}")
	private String host;

	@ExceptionHandler
	public ResponseEntity<ProblemDetail> handle(ErrorResponseException ex) {

		if (ex instanceof ServerWebInputException) {

			// Currently, no good way to differentiate and customize the various bad_request errors
			// see https://github.com/spring-projects/spring-framework/issues/28142

			ex.getBody().withType(URI.create("/problem/bad-request"));
		}
		else {
			// Customize other types of ResponseStatusException + any ErrorResponseException
		}

		ProblemDetail body = ex.getBody();
		body = new ExtendedProblemDetail(body, this.host);

		return new ResponseEntity<>(body, ex.getHeaders(), ex.getStatusCode());
	}

}
