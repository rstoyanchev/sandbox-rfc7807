package org.springframework.sandbox.rfc7807.exception;

import org.springframework.http.ProblemDetail;

public abstract class ProblemDetailException extends RuntimeException {

    ProblemDetail detail;

    ProblemDetailException(ProblemDetail detail) {
        this.detail = detail;
    }

    public ProblemDetail getDetail() {
        return detail;
    }
}
