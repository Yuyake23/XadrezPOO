package com.ifgrupo.chess.pieces;

public enum PieceType {
	BISHOP("B"),
	KING("K"),
	KNIGHT("N"),
	PAWN("P"),
	QUEEN("Q"),
	ROOK("R");

	public final String LETTER;

	PieceType(String type) {
		this.LETTER = type;
	}

	public static PieceType pieceTypeByChar(String letter) {
		for (PieceType pt : values()) {
			if (pt.LETTER.equalsIgnoreCase(letter))
				return pt;
		}
		return null;
	}
}
