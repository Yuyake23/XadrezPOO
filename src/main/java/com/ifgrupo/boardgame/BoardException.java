package com.ifgrupo.boardgame;

import java.io.Serial;

public class BoardException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 5387442010037533494L;

	public BoardException(String msg) {
		super(msg);
	}
}
