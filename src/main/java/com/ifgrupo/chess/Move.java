package com.ifgrupo.chess;

import com.ifgrupo.boardgame.Piece;

public record Move(
        ChessPosition sourcePosition,
        ChessPosition targetPosition,
        Piece capturedPiece,
        String promotedPiece) {
}
