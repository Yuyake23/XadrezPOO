package com.ifgrupo.chess;

public record Move(
        ChessPosition sourcePosition,
        ChessPosition targetPosition,
        String promotedPiece) {
}
