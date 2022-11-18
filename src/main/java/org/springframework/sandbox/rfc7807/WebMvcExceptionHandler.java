package org.springframework.sandbox.rfc7807;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class WebMvcExceptionHandler extends ResponseEntityExceptionHandler {

	@Value("${hostname}")
	private String host;


	@Override
	protected ResponseEntity<Object> createResponseEntity(
			Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

		if (body instanceof ProblemDetail detail) {
			detail.setProperty("host", this.host);
		}

		return super.createResponseEntity(body, headers, statusCode, request);
	}

}
