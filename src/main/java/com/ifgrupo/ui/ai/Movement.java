package com.ifgrupo.ui.ai;

import com.ifgrupo.boardgame.Piece;
import com.ifgrupo.chess.ChessPosition;

public class Movement {
	ChessPosition sourcePosition;
	ChessPosition targetPosition;
	Piece capturedPiece;

	Movement(ChessPosition sourcePosition, ChessPosition targetPosition, Piece capturedPiece) {
		this.sourcePosition = sourcePosition;
		this.targetPosition = targetPosition;
		this.capturedPiece = capturedPiece;
	}
}
