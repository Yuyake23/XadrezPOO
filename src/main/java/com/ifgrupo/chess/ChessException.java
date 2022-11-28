package com.ifgrupo.chess;

import com.ifgrupo.boardgame.BoardException;

public class ChessException extends BoardException {

	private static final long serialVersionUID = 1L;

	public ChessException(String msg) {
		super(msg);
	}
}
