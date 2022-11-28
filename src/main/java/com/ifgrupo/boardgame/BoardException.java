package com.ifgrupo.boardgame;

import java.io.Serial;
import java.io.Serializable;

public class BoardException extends RuntimeException implements Serializable {
	@Serial
	private static final long serialVersionUID = 5387442010037533494L;

	public BoardException(String msg) {
		super(msg);
	}
}
