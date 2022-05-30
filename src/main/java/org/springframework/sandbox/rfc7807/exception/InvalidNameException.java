package org.springframework.sandbox.rfc7807.exception;

import org.springframework.http.ProblemDetail;

public class InvalidNameException extends ProblemDetailException {

    public InvalidNameException(String detail) {

        super(ProblemDetail.forStatus(400).withTitle("Invalid name passed").withDetail(detail));
    }
}
