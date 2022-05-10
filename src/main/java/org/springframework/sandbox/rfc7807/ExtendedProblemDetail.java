package org.springframework.sandbox.rfc7807;

import org.springframework.http.ProblemDetail;

public class ExtendedProblemDetail extends ProblemDetail {

	private String host;

	public ExtendedProblemDetail(ProblemDetail other, String host) {
		super(other);
		this.host = host;
	}

	public ExtendedProblemDetail() {
	}


	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return this.host;
	}

}
