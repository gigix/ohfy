package org.thoughtworkers.ohfm.domain;

@SuppressWarnings("serial")
public class OhfmException extends RuntimeException {

	public OhfmException(Exception e) {
		super(e);
	}

	public OhfmException(String message, Exception e) {
		super(message, e);
	}

}
