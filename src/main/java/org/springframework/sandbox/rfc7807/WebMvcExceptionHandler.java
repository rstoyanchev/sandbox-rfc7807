package org.springframework.sandbox.rfc7807;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.sandbox.rfc7807.exception.ProblemDetailException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class WebMvcExceptionHandler extends ResponseEntityExceptionHandler {

	@Value("${hostname}")
	private String host;


	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		// Customize type and detail for MissingServletRequestParameterException

		ProblemDetail body = ex.getBody()
				.withType(URI.create("/problem/bad-request"))
				.withDetail("Expected parameter: " + ex.getParameterName());

		return handleExceptionInternal(ex, body, headers, status, request);
	}


	// More overrides to customize other Spring MVC exceptions + any ErrorResponseException...


	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

		// ProblemDetail extended for all exceptions

		if (body instanceof ProblemDetail detail) {
			body = new ExtendedProblemDetail(detail, this.host);
		}

		return super.handleExceptionInternal(ex, body, headers, statusCode, request);
	}

    @ExceptionHandler(value
        = {ProblemDetailException.class})
    protected ResponseEntity<Object> handleProblemDetailException(
        RuntimeException ex, WebRequest request) throws Exception {

        var problemDetail = ((ProblemDetailException) ex).getDetail();
        if (clientAcceptsProblem(request)) {
            return processExceptionAsProblem(ex, request, problemDetail);
        } else {
            return this.handleExceptionInternal(ex, ((ProblemDetailException) ex).getDetail(), new HttpHeaders(),
                                                HttpStatusCode.valueOf(problemDetail.getStatus()),
                                                request);
        }
    }

    private ResponseEntity<Object> processExceptionAsProblem(RuntimeException ex, WebRequest request, ProblemDetail problemDetail) {
        return handleExceptionInternal(ex, problemDetail,
                                       new HttpHeaders(), HttpStatusCode.valueOf(problemDetail.getStatus()), request);
    }

    private boolean clientAcceptsProblem(WebRequest request) {
        return "application/problem+json".equals(request.getHeader("Accept"));
    }

}
